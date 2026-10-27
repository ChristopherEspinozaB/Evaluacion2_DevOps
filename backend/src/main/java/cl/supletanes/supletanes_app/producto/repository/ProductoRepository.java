package cl.supletanes.supletanes_app.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.supletanes.supletanes_app.producto.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
    List<Producto> findByCategoria(String categoria);
}
