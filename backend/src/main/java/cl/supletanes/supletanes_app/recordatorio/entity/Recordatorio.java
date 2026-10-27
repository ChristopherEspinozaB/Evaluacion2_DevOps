package cl.supletanes.supletanes_app.recordatorio.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "recordatorio")
public class Recordatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idUsuario;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fechaHora;
}