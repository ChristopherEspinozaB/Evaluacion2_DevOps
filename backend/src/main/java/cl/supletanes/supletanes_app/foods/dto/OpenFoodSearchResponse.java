package cl.supletanes.supletanes_app.foods.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Modelo para la respuesta de Open Food Facts
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenFoodSearchResponse {
    private List<OpenFoodResponse.Product> products;

    public List<OpenFoodResponse.Product> getProducts() {
        return products;
    }

    public void setProducts(List<OpenFoodResponse.Product> products) {
        this.products = products;
    }
}