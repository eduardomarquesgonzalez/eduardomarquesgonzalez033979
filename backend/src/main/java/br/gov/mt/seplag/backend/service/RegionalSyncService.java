package br.gov.mt.seplag.backend.service;

import br.gov.mt.seplag.backend.dto.RegionalDTO;
import br.gov.mt.seplag.backend.entity.Regional;
import br.gov.mt.seplag.backend.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionalSyncService {

    private final RegionalRepository repository;
    private final RestTemplate restTemplate;
    private final String ARGUS_API_URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    @Scheduled(cron = "0 0 * * * *") // Executa a cada hora
    @Transactional
    public void syncRegionais() {
        log.info("Iniciando sincronização de regionais...");

        RegionalDTO[] response = restTemplate.getForObject(ARGUS_API_URL, RegionalDTO[].class);
        if (response == null) return;
        RegionalDTO[] externalData = response;

        Map<Integer, Regional> localMap = repository.findByAtivoTrue().stream()
                .collect(Collectors.toMap(Regional::getExternalId, r -> r));

        Set<Integer> externalIdsInResponse = new HashSet<>();

        for (RegionalDTO dto : externalData) {
            externalIdsInResponse.add(dto.id().intValue());
            Regional local = localMap.get(dto.id());

            if (local == null) {
                createNewRegional(dto);
            } else if (!local.getNome().equalsIgnoreCase(dto.nome())) {
                local.setAtivo(false);
                repository.save(local);
                createNewRegional(dto);
            }
        }

        localMap.values().stream()
                .filter(r -> !externalIdsInResponse.contains(r.getExternalId()))
                .forEach(r -> {
                    r.setAtivo(false);
                    repository.save(r);
                });
    }

    private void createNewRegional(RegionalDTO dto) {
        repository.save(Regional.builder()
                .externalId(dto.id().intValue())
                .nome(dto.nome())
                .ativo(true)
                .build());
    }
}