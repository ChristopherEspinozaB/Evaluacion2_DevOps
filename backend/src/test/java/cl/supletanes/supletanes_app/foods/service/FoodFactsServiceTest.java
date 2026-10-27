package cl.supletanes.supletanes_app.foods.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;
import cl.supletanes.supletanes_app.foods.dto.OpenFoodResponse;
import cl.supletanes.supletanes_app.foods.dto.OpenFoodSearchResponse;

@ExtendWith(MockitoExtension.class)
class FoodFactsServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private FoodFactsService service;

    @BeforeEach
    void setUp() {
        service = new FoodFactsService();
        ReflectionTestUtils.setField(service, "restTemplate", restTemplate);
    }

    private OpenFoodResponse.Product producto(String nombre, OpenFoodResponse.Nutriments n) {
        OpenFoodResponse.Product p = new OpenFoodResponse.Product();
        p.setProduct_name(nombre);
        p.setNutriments(n);
        return p;
    }

    @Test
    void getProductByBarcode_valoresPor100g() {
        OpenFoodResponse.Nutriments n = new OpenFoodResponse.Nutriments();
        n.setEnergy_kcal_100g(370.0);
        n.setProteins_100g(13.0);
        n.setCarbohydrates_100g(60.0);
        n.setFat_100g(7.0);
        OpenFoodResponse response = new OpenFoodResponse();
        response.setProduct(producto("Avena", n));
        when(restTemplate.getForObject(anyString(), eq(OpenFoodResponse.class))).thenReturn(response);

        FoodDTO dto = service.getProductByBarcode("123");

        assertEquals("Avena", dto.getNombre());
        assertEquals(370.0, dto.getCalorias());
        assertEquals(13.0, dto.getProteinas());
        assertEquals(60.0, dto.getCarbohidratos());
        assertEquals(7.0, dto.getGrasas());
    }

    @Test
    void getProductByBarcode_usaValoresPorPorcionCuandoFaltan100g() {
        OpenFoodResponse.Nutriments n = new OpenFoodResponse.Nutriments();
        n.setEnergy_kcal_100g(0.0);
        n.setEnergy_kcal_serving(120.0);
        n.setProteins_serving(20.0);
        n.setCarbohydrates_serving(3.0);
        n.setFat_serving(2.0);
        OpenFoodResponse response = new OpenFoodResponse();
        response.setProduct(producto("Whey", n));
        when(restTemplate.getForObject(anyString(), eq(OpenFoodResponse.class))).thenReturn(response);

        FoodDTO dto = service.getProductByBarcode("456");

        assertEquals(120.0, dto.getCalorias());
        assertEquals(20.0, dto.getProteinas());
        assertEquals(3.0, dto.getCarbohidratos());
        assertEquals(2.0, dto.getGrasas());
    }

    @Test
    void getProductByBarcode_usaEnergiaGeneralComoRespaldo() {
        OpenFoodResponse.Nutriments n = new OpenFoodResponse.Nutriments();
        n.setEnergy_100g(1500.0);
        OpenFoodResponse response = new OpenFoodResponse();
        response.setProduct(producto("Barra", n));
        when(restTemplate.getForObject(anyString(), eq(OpenFoodResponse.class))).thenReturn(response);

        FoodDTO dto = service.getProductByBarcode("789");

        assertEquals(1500.0, dto.getCalorias());
        assertEquals(0.0, dto.getProteinas());
    }

    @Test
    void getProductByBarcode_sinProducto() {
        when(restTemplate.getForObject(anyString(), eq(OpenFoodResponse.class))).thenReturn(new OpenFoodResponse());

        FoodDTO dto = service.getProductByBarcode("000");

        assertNull(dto.getNombre());
        assertEquals(0.0, dto.getCalorias());
    }

    @Test
    void searchProductsByName_conResultados() {
        OpenFoodResponse.Nutriments n1 = new OpenFoodResponse.Nutriments();
        n1.setEnergy_kcal_100g(370.0);
        n1.setProteins_100g(13.0);
        n1.setCarbohydrates_100g(60.0);
        n1.setFat_100g(7.0);
        OpenFoodResponse.Nutriments n2 = new OpenFoodResponse.Nutriments();

        OpenFoodSearchResponse response = new OpenFoodSearchResponse();
        response.setProducts(List.of(producto("Avena", n1), producto("Vacío", n2)));

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");
        when(restTemplate.getForObject(anyString(), eq(OpenFoodSearchResponse.class))).thenReturn(response);

        List<FoodDTO> result = service.searchProductsByName("avena");

        assertEquals(2, result.size());
        assertEquals("Avena", result.get(0).getNombre());
        assertEquals(370.0, result.get(0).getCalorias());
        assertEquals(0.0, result.get(1).getCalorias());
    }

    @Test
    void searchProductsByName_sinResultados() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");
        when(restTemplate.getForObject(anyString(), eq(OpenFoodSearchResponse.class))).thenReturn(null);

        assertTrue(service.searchProductsByName("nada").isEmpty());
    }
}