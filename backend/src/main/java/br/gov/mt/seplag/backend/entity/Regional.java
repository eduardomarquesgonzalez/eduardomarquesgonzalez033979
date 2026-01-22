package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;
}
