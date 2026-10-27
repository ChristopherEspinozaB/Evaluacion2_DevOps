package cl.supletanes.supletanes_app.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.supletanes.supletanes_app.producto.model.Producto;
import cl.supletanes.supletanes_app.producto.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProductos(){
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> findByCategoria(String categoria){
        return productoRepository.findByCategoria(categoria);
    }

    @Override
    public Producto getProductoById(Long id){
        return productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    public Producto saveProducto(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Long id, Producto producto) {
        Producto productoExistente = getProductoById(id);
        
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        productoExistente.setCategoria(producto.getCategoria());
        
        return productoRepository.save(productoExistente);
    }
    
    @Override
    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        productoRepository.delete(producto);
    }
}
