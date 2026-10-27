package cl.supletanes.supletanes_app.foods.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {
    private String nombre;
    private Double calorias;
    private Double proteinas;
    private Double carbohidratos;
    private Double grasas;
}