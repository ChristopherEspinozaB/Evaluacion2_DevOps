package cl.supletanes.supletanes_app.foods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;
import cl.supletanes.supletanes_app.foods.dto.OpenFoodResponse;
import cl.supletanes.supletanes_app.foods.dto.OpenFoodSearchResponse;

@Service
public class FoodFactsService {
    private final RestTemplate restTemplate = new RestTemplate();

    public FoodDTO getProductByBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v2/product/" + barcode + ".json";
        OpenFoodResponse response = restTemplate.getForObject(url, OpenFoodResponse.class);

        if (response != null && response.getProduct() != null) {
            OpenFoodResponse.Product p = response.getProduct();
            OpenFoodResponse.Nutriments n = p.getNutriments();

            // Fallback para calorías y demas
            Double calorias = n.getEnergy_kcal_100g();
            if (calorias == null || calorias == 0.0)
                calorias = n.getEnergy_100g();
            if (calorias == null || calorias == 0.0)
                calorias = n.getEnergy_kcal_serving();

            Double proteinas = n.getProteins_100g();
            if (proteinas == null || proteinas == 0.0)
                proteinas = n.getProteins_serving();

            Double carbohidratos = n.getCarbohydrates_100g();
            if (carbohidratos == null || carbohidratos == 0.0)
                carbohidratos = n.getCarbohydrates_serving();

            Double grasas = n.getFat_100g();
            if (grasas == null || grasas == 0.0)
                grasas = n.getFat_serving();

            return new FoodDTO(
                    p.getProduct_name(),
                    calorias != null ? calorias : 0.0,
                    proteinas != null ? proteinas : 0.0,
                    carbohidratos != null ? carbohidratos : 0.0,
                    grasas != null ? grasas : 0.0);
        }
        return new FoodDTO(null, 0.0, 0.0, 0.0, 0.0);
    }

    public List<FoodDTO> searchProductsByName(String name) {
        String url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms="
                + name + "&json=1&page_size=10";

        // 1. Ver respuesta cruda
        String rawJson = restTemplate.getForObject(url, String.class);
        System.out.println("Respuesta cruda de OFF: " + rawJson);

        // 2. Mapear al objeto
        OpenFoodSearchResponse response = restTemplate.getForObject(url, OpenFoodSearchResponse.class);

        // 3. Ver cantidad de productos
        if (response != null && response.getProducts() != null) {
            System.out.println("Cantidad de productos: " + response.getProducts().size());
        } else {
            System.out.println("No se mapearon productos");
        }

        List<FoodDTO> results = new ArrayList<>();
        if (response != null && response.getProducts() != null) {
            for (OpenFoodResponse.Product p : response.getProducts()) {
                System.out.println("Producto encontrado: " + p.getProduct_name());

                OpenFoodResponse.Nutriments n = p.getNutriments();

                Double calorias = n.getEnergy_kcal_100g();
                if (calorias == null || calorias == 0.0)
                    calorias = n.getEnergy_100g();
                if (calorias == null || calorias == 0.0)
                    calorias = n.getEnergy_kcal_serving();

                FoodDTO dto = new FoodDTO(
                        p.getProduct_name(),
                        calorias != null ? calorias : 0.0,
                        n.getProteins_100g() != null ? n.getProteins_100g() : 0.0,
                        n.getCarbohydrates_100g() != null ? n.getCarbohydrates_100g() : 0.0,
                        n.getFat_100g() != null ? n.getFat_100g() : 0.0);

                System.out.println("DTO generado: " + dto);
                results.add(dto);
            }
        }

        return results;
    }
}