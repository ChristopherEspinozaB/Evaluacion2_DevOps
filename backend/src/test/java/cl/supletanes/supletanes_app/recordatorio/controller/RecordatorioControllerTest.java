package cl.supletanes.supletanes_app.recordatorio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;
import cl.supletanes.supletanes_app.recordatorio.service.RecordatorioService;

@WebMvcTest(RecordatorioController.class)
class RecordatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private RecordatorioService service;

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
    void obtenerTodos() throws Exception {
        when(service.obtenerTodosLosRecordatorios()).thenReturn(List.of(recordatorio));
        mockMvc.perform(get("/api/v1/recordatorios"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].mensaje").value("Tomar creatina"));
    }

    @Test
    void crear() throws Exception {
        when(service.crearRecordatorio(any(Recordatorio.class))).thenReturn(recordatorio);
        mockMvc.perform(post("/api/v1/recordatorios")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(recordatorio)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.idUsuario").value("user-1"));
    }

    @Test
    void actualizar_existe() throws Exception {
        when(service.actualizarRecordatorio(eq(1L), any(Recordatorio.class))).thenReturn(Optional.of(recordatorio));
        mockMvc.perform(put("/api/v1/recordatorios/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(recordatorio)))
               .andExpect(status().isOk());
    }

    @Test
    void actualizar_noExiste() throws Exception {
        when(service.actualizarRecordatorio(eq(99L), any(Recordatorio.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/v1/recordatorios/99")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(recordatorio)))
               .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorUsuario() throws Exception {
        when(service.obtenerRecordatoriosPorUsuario("user-1")).thenReturn(List.of(recordatorio));
        mockMvc.perform(get("/api/v1/recordatorios/usuario/user-1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void eliminar_existe() throws Exception {
        when(service.obtenerRecordatorioPorId(1L)).thenReturn(Optional.of(recordatorio));
        mockMvc.perform(delete("/api/v1/recordatorios/1"))
               .andExpect(status().isNoContent());
        verify(service).eliminarRecordatorio(1L);
    }

    @Test
    void eliminar_noExiste() throws Exception {
        when(service.obtenerRecordatorioPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/v1/recordatorios/99"))
               .andExpect(status().isNotFound());
        verify(service, never()).eliminarRecordatorio(anyLong());
    }
}