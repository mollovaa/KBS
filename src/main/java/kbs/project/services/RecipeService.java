package kbs.project.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kbs.project.apidtos.IngredientApiDto;
import kbs.project.apidtos.IngredientNutritionApiDto;
import kbs.project.apidtos.RecipeByIdApiDto;
import kbs.project.apidtos.RecipeByIngredientsApiDto;
import kbs.project.entities.Product;
import kbs.project.entities.Recipe;
import kbs.project.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

  private ApiClientService apiClientService;

  @Autowired
  public RecipeService(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  public List<Recipe> findRecipesByProducts(List<Product> products) {

    List<RecipeByIngredientsApiDto> recipes = apiClientService.getRecipesByProducts(products.stream()
        .map(Product::getName)
        .collect(Collectors.toList()));

    System.out.println("Got " + recipes.size() + " recipes with these products!");

    recipes = removeRecipesWithMissingIngredients(recipes);

    System.out.println("Left " + recipes.size() + " recipes after removing the ones with missing ingredients.");

    //product - available amount
    Map<String, BigDecimal> productsWithAmounts = new HashMap<>();
    products.stream()
        .map(product -> productsWithAmounts.put(product.getName(), product.getAvailableAmount()));

    recipes = removeRecipesWithProductsQuantityBiggerThanExpected(recipes, productsWithAmounts);

    System.out.println("Left " + recipes.size() + " after removing the ones with bigger product quantity then specified.");

    //product - calories
    Map<String, BigDecimal> productsWithCalories = new HashMap<>();
    products.stream()
        .map(product -> productsWithCalories.put(product.getName(), product.getKiloCalories()));

    recipes = removeRecipesWithNotEqualCaloriesOfIngredients(recipes, productsWithCalories);

    System.out.println("Left " + recipes.size() + " after removing the ones with bigger calories than specified.");

    //todo check the price of the products -> next call to api

    return recipes.stream()
        .map(this::convertRecipeApiDtoToRecipe)
        .collect(Collectors.toList());
  }

  //циклично намира най-подходяшите рецепти спрямо дадените от потребителя правила:
  //цена и енергийна стойност


  // Премахваме рецептите, които съдържат продукти освен зададените от нас:
  private List<RecipeByIngredientsApiDto> removeRecipesWithMissingIngredients(List<RecipeByIngredientsApiDto> allRecipes) {
    return allRecipes.stream()
        .filter(recipeByIngredientsApiDto -> recipeByIngredientsApiDto.getMissedIngredientCount() == 0)
        .collect(Collectors.toList());
  }

  private List<RecipeByIngredientsApiDto> removeRecipesWithProductsQuantityBiggerThanExpected(List<RecipeByIngredientsApiDto> allRecipes,
      Map<String, BigDecimal> productsWithAmounts) {

    List<RecipeByIngredientsApiDto> filteredRecipes = new ArrayList<>();

    for (RecipeByIngredientsApiDto recipe : allRecipes) {
      boolean checkAllIngredients = true;
      for (IngredientApiDto ingredient : recipe.getUsedIngredients()) {
        for (String product : productsWithAmounts.keySet()) {
          if (ingredient.getName().equals(product)) {
            if (ingredient.getUnit().equals("g")) {
              if (!ingredient.getAmount().equals(productsWithAmounts.get(product))) {
                checkAllIngredients = false;
                break;
              }
            } else {
              //convert units and check again
            }
          }
        }
        if (!checkAllIngredients) {
          break;
        }
      }
      if (checkAllIngredients) {
        filteredRecipes.add(recipe);
      }
    }
    return filteredRecipes;
  }

  private List<RecipeByIngredientsApiDto> removeRecipesWithNotEqualCaloriesOfIngredients(List<RecipeByIngredientsApiDto> allRecipes,
      Map<String, BigDecimal> productsWithCalories) {
    List<RecipeByIngredientsApiDto> filteredRecipes = new ArrayList<>();
    for (RecipeByIngredientsApiDto recipe : allRecipes) {
      boolean checkAllIngredients = true;
      RecipeByIdApiDto recipeById = apiClientService.findRecipeById(recipe.getId());
      for (IngredientNutritionApiDto ingredient : recipeById.getNutrition().getIngredients()) {
        for (String productName : productsWithCalories.keySet()) {
          if (productName.equals(ingredient.getName())) {
            if (ingredient.getUnit().equals("g")) {
              if (!ingredient.getAmount().equals(productsWithCalories.get(productName))) {
                checkAllIngredients = false;
                break;
              }
            } else {
              //convert units and check again
            }
          }
        }
        if (!checkAllIngredients) {
          break;
        }
      }
      if (checkAllIngredients) {
        filteredRecipes.add(recipe);
      }
    }
    return filteredRecipes;
  }

  private Recipe convertRecipeApiDtoToRecipe(RecipeByIngredientsApiDto recipeByIngredientsApiDto) {
    RecipeByIdApiDto recipeByIdApiDto = apiClientService.findRecipeById(recipeByIngredientsApiDto.getId());
    return new Recipe(recipeByIngredientsApiDto.getTitle(), recipeByIdApiDto.getServings(),
        recipeByIdApiDto.getNutrition().getNutrients()
            .stream()
            .filter(nutrientApiDto -> nutrientApiDto.getTitle().equals("Calories"))
            .findFirst()
            .get()
            .getAmount(), recipeByIdApiDto.getPricePerServing(), new ArrayList<>());
  }

  private Product convertIngredientApiDtoToProduct(IngredientApiDto ingredient) {
    return new Product(ingredient.getName(), null, null, null);
  }
}
