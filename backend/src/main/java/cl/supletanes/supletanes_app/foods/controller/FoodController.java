package cl.supletanes.supletanes_app.foods.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.supletanes.supletanes_app.foods.dto.FoodDTO;
import cl.supletanes.supletanes_app.foods.service.FoodFactsService;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodFactsService foodFactsService;

    public FoodController(FoodFactsService foodFactsService) {
        this.foodFactsService = foodFactsService;
    }

    @GetMapping("/{barcode}")
    public FoodDTO getFood(@PathVariable String barcode) {
        return foodFactsService.getProductByBarcode(barcode);
    }

    @GetMapping("/search")
    public List<FoodDTO> searchFood(@RequestParam String name) {
        return foodFactsService.searchProductsByName(name);
    }
}