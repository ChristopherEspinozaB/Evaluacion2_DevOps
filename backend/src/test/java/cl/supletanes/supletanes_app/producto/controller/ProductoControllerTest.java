package cl.supletanes.supletanes_app.producto.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.supletanes.supletanes_app.producto.model.Producto;
import cl.supletanes.supletanes_app.producto.service.ProductoService;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private ProductoService service;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Whey Protein");
        producto.setDescripcion("Proteína de suero");
        producto.setPrecio(25000.0);
        producto.setStock(10);
        producto.setCategoria("Proteinas");
    }

    @Test
    void obtenerTodos() throws Exception {
        when(service.getAllProductos()).thenReturn(List.of(producto));
        mockMvc.perform(get("/api/productos"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].nombre").value("Whey Protein"));
    }

    @Test
    void obtenerPorCategoria() throws Exception {
        when(service.findByCategoria("Proteinas")).thenReturn(List.of(producto));
        mockMvc.perform(get("/api/productos/categoria/Proteinas"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].categoria").value("Proteinas"));
    }

    @Test
    void obtenerPorId() throws Exception {
        when(service.getProductoById(1L)).thenReturn(producto);
        mockMvc.perform(get("/api/productos/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void crearProducto() throws Exception {
        when(service.saveProducto(any(Producto.class))).thenReturn(producto);
        mockMvc.perform(post("/api/productos")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(producto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.nombre").value("Whey Protein"));
    }

    @Test
    void actualizarProducto() throws Exception {
        when(service.updateProducto(eq(1L), any(Producto.class))).thenReturn(producto);
        mockMvc.perform(put("/api/productos/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(mapper.writeValueAsString(producto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.precio").value(25000.0));
    }

    @Test
    void eliminarProducto() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
               .andExpect(status().isNoContent());
        verify(service).deleteProducto(1L);
    }
}