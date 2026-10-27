package cl.supletanes.supletanes_app.producto.service;

import java.util.List;

import cl.supletanes.supletanes_app.producto.model.Producto;

public interface ProductoService {
    Producto saveProducto(Producto producto);
    List<Producto> getAllProductos();
    List<Producto> findByCategoria(String categoria);
    Producto getProductoById(Long id);
    Producto updateProducto(Long id, Producto producto);
    void deleteProducto(Long id);
}
