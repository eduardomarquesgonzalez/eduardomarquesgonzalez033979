package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.scheduling.annotation.Scheduled;

@Entity
@Table(name = "regional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regional {
    @Id
    private Long id;
    @Column(nullable = false, length = 200)
    private String nome;
    private Boolean ativo = true;

    @Scheduled(cron = "0 0 * * * *")
    public void syncRegionais() {


    }
}
