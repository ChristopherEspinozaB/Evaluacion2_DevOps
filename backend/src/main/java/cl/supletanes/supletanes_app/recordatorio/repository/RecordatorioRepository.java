package cl.supletanes.supletanes_app.recordatorio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
    // Método para buscar todos los recordatorios de un usuario específico
    List<Recordatorio> findByIdUsuario(String idUsuario);
}