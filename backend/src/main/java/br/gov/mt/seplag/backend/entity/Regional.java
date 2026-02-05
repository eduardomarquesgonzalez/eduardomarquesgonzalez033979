package br.gov.mt.seplag.backend.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 200)
    private Integer externalId;
    private String nome;
    private Boolean ativo = true;
    public void syncRegionais() {

    }
}
