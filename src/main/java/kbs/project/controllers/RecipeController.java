package kbs.project.controllers;

import java.util.List;
import kbs.project.entities.Product;
import kbs.project.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

  @PostMapping(path = "/findRecipesByProducts", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity findRecipesByProducts(@RequestBody List<Product> products) {
    return ResponseEntity.ok(recipeService.findRecipesByProducts(products));
  }

}