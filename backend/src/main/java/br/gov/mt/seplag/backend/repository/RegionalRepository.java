package br.gov.mt.seplag.backend.repository;

import br.gov.mt.seplag.backend.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalRepository extends JpaRepository<Regional, Long> {
    List<Regional> findByAtivoTrue();
}
