package kbs.project.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kbs.project.apidtos.RecipeByIdApiDto;
import kbs.project.apidtos.RecipeByIngredientsApiDto;
import kbs.project.entities.Product;
import kbs.project.entities.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

  private ApiClientService apiClientService;
  private RulesInterpreter rulesInterpreter;

  @Autowired
  public RecipeService(ApiClientService apiClientService, RulesInterpreter rulesInterpreter) {
    this.apiClientService = apiClientService;
    this.rulesInterpreter = rulesInterpreter;
  }

  public List<Recipe> findRecipesByProducts(List<Product> products) {

    List<RecipeByIngredientsApiDto> recipes = apiClientService.getRecipesByProducts(products.stream()
        .map(Product::getName)
        .collect(Collectors.toList()));

    System.out.println("Got " + recipes.size() + " recipes with these products!");

    recipes = rulesInterpreter.removeRecipesWithMissingIngredients(recipes);

    System.out.println("Left " + recipes.size() + " recipes after removing the ones with missing ingredients.");

    //product - available amount
    Map<String, BigDecimal> productsWithAmounts = new HashMap<>();
    products.stream()
        .map(product -> productsWithAmounts.put(product.getName(), product.getAvailableAmount()));

    recipes = rulesInterpreter.removeRecipesWithProductsQuantityBiggerThanExpected(recipes, productsWithAmounts);

    System.out.println("Left " + recipes.size() + " after removing the ones with bigger product quantity then specified.");

    //product - calories
    Map<String, BigDecimal> productsWithCalories = new HashMap<>();
    products.stream()
        .map(product -> productsWithCalories.put(product.getName(), product.getKiloCalories()));

    recipes = rulesInterpreter.removeRecipesWithMoreCaloriesOfIngredients(recipes, productsWithCalories);

    System.out.println("Left " + recipes.size() + " after removing the ones with bigger calories than specified.");

    //product - calories
    Map<String, BigDecimal> productsWithPrices = new HashMap<>();
    products.stream()
        .map(product -> productsWithPrices.put(product.getName(), product.getPrice()));

    recipes = rulesInterpreter.removeRecipesWithBiggerPricesOfIngredients(recipes, productsWithPrices);

    System.out.println("Left " + recipes.size() + " after removing the ones with bigger price than specified.");
    System.out.println("Final number of recipes: " + recipes.size());

    return recipes.stream()
        .map(this::convertRecipeApiDtoToRecipe)
        .collect(Collectors.toList());
  }

  private Recipe convertRecipeApiDtoToRecipe(RecipeByIngredientsApiDto recipeByIngredientsApiDto) {
    RecipeByIdApiDto recipeByIdApiDto = apiClientService.findRecipeById(recipeByIngredientsApiDto.getId());
    return new Recipe(recipeByIngredientsApiDto.getTitle(), recipeByIdApiDto.getServings(),
        recipeByIdApiDto.getNutrition().getNutrients()
            .stream()
            .filter(nutrientApiDto -> nutrientApiDto.getTitle().equals("Calories"))
            .findFirst()
            .get()
            .getAmount(), recipeByIdApiDto.getPricePerServing());
  }

}