package cl.supletanes.supletanes_app.foods.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;
import cl.supletanes.supletanes_app.foods.service.FoodFactsService;

@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FoodFactsService service;

    @Test
    void getFoodPorBarcode() throws Exception {
        when(service.getProductByBarcode("123")).thenReturn(new FoodDTO("Avena", 370.0, 13.0, 60.0, 7.0));
        mockMvc.perform(get("/api/food/123"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Avena"))
               .andExpect(jsonPath("$.calorias").value(370.0));
    }

    @Test
    void searchFood() throws Exception {
        when(service.searchProductsByName("avena")).thenReturn(List.of(new FoodDTO("Avena", 370.0, 13.0, 60.0, 7.0)));
        mockMvc.perform(get("/api/food/search").param("name", "avena"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].nombre").value("Avena"));
    }
}