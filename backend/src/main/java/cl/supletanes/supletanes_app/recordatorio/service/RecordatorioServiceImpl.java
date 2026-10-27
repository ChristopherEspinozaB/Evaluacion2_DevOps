package cl.supletanes.supletanes_app.recordatorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;
import cl.supletanes.supletanes_app.recordatorio.repository.RecordatorioRepository;

@Service
public class RecordatorioServiceImpl implements RecordatorioService {

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Override
    public List<Recordatorio> obtenerTodosLosRecordatorios() {
        return recordatorioRepository.findAll();
    }

    @Override
    public Recordatorio crearRecordatorio(Recordatorio recordatorio) {
        return recordatorioRepository.save(recordatorio);
    }

    @Override
    public Optional<Recordatorio> actualizarRecordatorio(Long id, Recordatorio recordatorioActualizado) {
        return recordatorioRepository.findById(id).map(recordatorioExistente -> {
            recordatorioExistente.setMensaje(recordatorioActualizado.getMensaje());
            recordatorioExistente.setFechaHora(recordatorioActualizado.getFechaHora());
            return recordatorioRepository.save(recordatorioExistente);
        });
    }

    @Override
    public List<Recordatorio> obtenerRecordatoriosPorUsuario(String idUsuario) {
        return recordatorioRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public Optional<Recordatorio> obtenerRecordatorioPorId(Long id) {
        return recordatorioRepository.findById(id);
    }

    @Override
    public void eliminarRecordatorio(Long id) {
        recordatorioRepository.deleteById(id);
    }
}
