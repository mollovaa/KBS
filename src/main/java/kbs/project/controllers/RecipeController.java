package kbs.project.controllers;

import java.util.ArrayList;
import java.util.List;
import kbs.project.entities.Product;
import kbs.project.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

  private RecipeService recipeService;

  @Autowired
  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  // endpoint, който
  // 1. приема дадени продукти от потребителя
  // 2. хитва АПИ, за да вземе всички рецепти (ApiClient)
  // 3. циклично филтрита по дадени правила: цена, енергийна стойност (Algorithm)
  // 4. връща отговарящите резултати

  @PostMapping(path = "/findRecipesByProducts")
  public ResponseEntity findRecipesByProducts(@RequestBody Product products) {
    List<Product> products1 = new ArrayList<>();
    products1.add(products);
    return ResponseEntity.ok(recipeService.findRecipesByProducts(products1));
  }


  //todo add swagger

}
