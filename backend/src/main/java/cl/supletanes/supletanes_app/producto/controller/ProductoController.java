package cl.supletanes.supletanes_app.producto.controller;

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

import cl.supletanes.supletanes_app.producto.model.Producto;
import cl.supletanes.supletanes_app.producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")   //Ruta postman https://supletanes-app-final.onrender.com
//@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener todos los productos de una categoría específica")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.findByCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.getProductoById(id);
        return ResponseEntity.ok(producto);
    }
    
    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.saveProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Actualizar un producto")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id, 
            @RequestBody Producto producto) {
        Producto productoActualizado = productoService.updateProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }
    
    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
