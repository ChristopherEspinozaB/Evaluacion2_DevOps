package cl.supletanes.supletanes_app.producto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.supletanes.supletanes_app.producto.model.Producto;
import cl.supletanes.supletanes_app.producto.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoServiceImpl service;

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
    void getAllProductos() {
        when(repository.findAll()).thenReturn(List.of(producto));
        assertEquals(1, service.getAllProductos().size());
    }

    @Test
    void findByCategoria() {
        when(repository.findByCategoria("Proteinas")).thenReturn(List.of(producto));
        assertEquals(List.of(producto), service.findByCategoria("Proteinas"));
    }

    @Test
    void getProductoById_existe() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        assertEquals(producto, service.getProductoById(1L));
    }

    @Test
    void getProductoById_noExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getProductoById(99L));
        assertEquals("Producto no encontrado con id: 99", ex.getMessage());
    }

    @Test
    void saveProducto() {
        when(repository.save(producto)).thenReturn(producto);
        assertEquals(producto, service.saveProducto(producto));
    }

    @Test
    void updateProducto_existe() {
        Producto nuevo = new Producto();
        nuevo.setNombre("Creatina");
        nuevo.setDescripcion("Monohidrato");
        nuevo.setPrecio(15000.0);
        nuevo.setStock(5);
        nuevo.setCategoria("Creatinas");

        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto result = service.updateProducto(1L, nuevo);

        assertEquals(1L, result.getId());
        assertEquals("Creatina", result.getNombre());
        assertEquals(5, result.getStock());
        assertEquals("Creatinas", result.getCategoria());
    }

    @Test
    void updateProducto_noExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.updateProducto(99L, producto));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteProducto_existe() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        service.deleteProducto(1L);
        verify(repository).delete(producto);
    }

    @Test
    void deleteProducto_noExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.deleteProducto(99L));
        verify(repository, never()).delete(any());
    }
}