package cl.supletanes.supletanes_app.recordatorio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;
import cl.supletanes.supletanes_app.recordatorio.service.RecordatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/recordatorios")
@Tag(name = "Recordatorios", description = "Gestión de recordatorios personales por usuario")
//@CrossOrigin(origins = "*")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    @Operation(summary = "Obtener todos los recordatorios (Global)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de todos los recordatorios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Recordatorio>> obtenerTodosLosRecordatorios() {
        List<Recordatorio> recordatorios = recordatorioService.obtenerTodosLosRecordatorios();
        return ResponseEntity.ok(recordatorios);
    }

    @Operation(summary = "Crear un nuevo recordatorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recordatorio creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Recordatorio> crearRecordatorio(@RequestBody Recordatorio recordatorio) {
        Recordatorio nuevoRecordatorio = recordatorioService.crearRecordatorio(recordatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecordatorio);
    }

    @Operation(summary = "Actualizar un recordatorio existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recordatorio actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Recordatorio no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Recordatorio> actualizarRecordatorio(@PathVariable Long id, @RequestBody Recordatorio recordatorioActualizado) {
        return recordatorioService.actualizarRecordatorio(id, recordatorioActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los recordatorios de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de recordatorios obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Recordatorio>> obtenerRecordatoriosPorUsuario(@PathVariable String idUsuario) {
        List<Recordatorio> recordatorios = recordatorioService.obtenerRecordatoriosPorUsuario(idUsuario);
        return ResponseEntity.ok(recordatorios);
    }

    @Operation(summary = "Eliminar un recordatorio por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recordatorio eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Recordatorio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecordatorio(@PathVariable Long id) {
        if (recordatorioService.obtenerRecordatorioPorId(id).isPresent()) {
            recordatorioService.eliminarRecordatorio(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}