package cl.supletanes.supletanes_app.recordatorio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;
import cl.supletanes.supletanes_app.recordatorio.repository.RecordatorioRepository;

@ExtendWith(MockitoExtension.class)
class RecordatorioServiceImplTest {

    @Mock
    private RecordatorioRepository repository;

    @InjectMocks
    private RecordatorioServiceImpl service;

    private Recordatorio recordatorio;

    @BeforeEach
    void setUp() {
        recordatorio = new Recordatorio();
        recordatorio.setId(1L);
        recordatorio.setIdUsuario("user-1");
        recordatorio.setMensaje("Tomar creatina");
        recordatorio.setFechaHora(LocalDateTime.of(2026, 1, 1, 8, 0));
    }

    @Test
    void obtenerTodos() {
        when(repository.findAll()).thenReturn(List.of(recordatorio));
        assertEquals(1, service.obtenerTodosLosRecordatorios().size());
    }

    @Test
    void crear() {
        when(repository.save(recordatorio)).thenReturn(recordatorio);
        assertEquals(recordatorio, service.crearRecordatorio(recordatorio));
    }

    @Test
    void actualizar_existe() {
        Recordatorio nuevo = new Recordatorio();
        nuevo.setMensaje("Tomar proteína");
        nuevo.setFechaHora(LocalDateTime.of(2026, 2, 2, 9, 30));

        when(repository.findById(1L)).thenReturn(Optional.of(recordatorio));
        when(repository.save(any(Recordatorio.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Recordatorio> result = service.actualizarRecordatorio(1L, nuevo);

        assertTrue(result.isPresent());
        assertEquals("Tomar proteína", result.get().getMensaje());
        assertEquals(LocalDateTime.of(2026, 2, 2, 9, 30), result.get().getFechaHora());
    }

    @Test
    void actualizar_noExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(service.actualizarRecordatorio(99L, recordatorio).isEmpty());
    }

    @Test
    void obtenerPorUsuario() {
        when(repository.findByIdUsuario("user-1")).thenReturn(List.of(recordatorio));
        assertEquals(List.of(recordatorio), service.obtenerRecordatoriosPorUsuario("user-1"));
    }

    @Test
    void obtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(recordatorio));
        assertEquals(Optional.of(recordatorio), service.obtenerRecordatorioPorId(1L));
    }

    @Test
    void eliminar() {
        service.eliminarRecordatorio(1L);
        verify(repository).deleteById(1L);
    }
}