package kbs.project.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kbs.project.apidtos.IngredientApiDto;
import kbs.project.apidtos.IngredientByIdApiDto;
import kbs.project.apidtos.IngredientNutritionApiDto;
import kbs.project.apidtos.RecipeByIdApiDto;
import kbs.project.apidtos.RecipeByIngredientsApiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RulesInterpreter {

  private ApiClientService apiClientService;

  @Autowired
  public RulesInterpreter(ApiClientService apiClientService) {
    this.apiClientService = apiClientService;
  }

  // ВСИЧКИ СЛЕДВАЩИ ФУНКЦИИ ИГРАЯТ РОЛЯ НА "ИНТЕРПРЕТАТОР НА ПРАВИЛАТА":

  // Правило за използване само на зададените продукти:
  // Премахваме рецептите, които съдържат продукти освен зададените от нас (има липсващи):
  public List<RecipeByIngredientsApiDto> removeRecipesWithMissingIngredients(List<RecipeByIngredientsApiDto> allRecipes) {
    return allRecipes.stream()
        .filter(recipeByIngredientsApiDto -> recipeByIngredientsApiDto.getMissedIngredientCount() == 0)
        .collect(Collectors.toList());
  }

  // Правило за количество на продукта <= зададеното от поребителя:
  // Премахваме рецептите, които съдържат продукти с по-голямо количество от зададеното от потребителя:
  public List<RecipeByIngredientsApiDto> removeRecipesWithProductsQuantityBiggerThanExpected(List<RecipeByIngredientsApiDto> allRecipes,
      Map<String, BigDecimal> productsWithAmounts) {
    List<RecipeByIngredientsApiDto> filteredRecipes = new ArrayList<>();

    for (RecipeByIngredientsApiDto recipe : allRecipes) {
      boolean checkAllIngredients = true;
      for (IngredientApiDto ingredient : recipe.getUsedIngredients()) {
        for (String product : productsWithAmounts.keySet()) {
          if (ingredient.getName().equals(product)) {
//            if (ingredient.getUnit().equals("g")) {
              if (ingredient.getAmount().compareTo(productsWithAmounts.get(product)) > 0) {
                checkAllIngredients = false;
                break;
              }
//            } else {
              //convert units and check again
//            }
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

  // Правило за енергийна стойност на продукта <= зададеното от поребителя:
  // Премахваме рецептите, които съдържат продукти с по-голяма енергийна стойност от зададената от потребителя:
  public List<RecipeByIngredientsApiDto> removeRecipesWithMoreCaloriesOfIngredients(List<RecipeByIngredientsApiDto> allRecipes,
      Map<String, BigDecimal> productsWithCalories) {
    List<RecipeByIngredientsApiDto> filteredRecipes = new ArrayList<>();
    for (RecipeByIngredientsApiDto recipe : allRecipes) {
      boolean checkAllIngredients = true;
      RecipeByIdApiDto recipeById = apiClientService.findRecipeById(recipe.getId());
      for (IngredientNutritionApiDto ingredient : recipeById.getNutrition().getIngredients()) {
        for (String productName : productsWithCalories.keySet()) {
          if (productName.equals(ingredient.getName())) {
//            if (ingredient.getUnit().equals("g")) {
              if (ingredient.getAmount().compareTo(productsWithCalories.get(productName)) > 0) {
                checkAllIngredients = false;
                break;
              }
//            } else {
              //convert units and check again
//            }
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

  // Правило за цена на продукта <= зададеното от поребителя:
  // Премахваме рецептите, които съдържат продукти с по-голяма цена от зададената от потребителя:
  public List<RecipeByIngredientsApiDto> removeRecipesWithBiggerPricesOfIngredients(List<RecipeByIngredientsApiDto> allRecipes,
      Map<String, BigDecimal> productsWithPrices) {
    List<RecipeByIngredientsApiDto> filteredRecipes = new ArrayList<>();
    for (RecipeByIngredientsApiDto recipe : allRecipes) {
      boolean checkAllIngredients = true;
      for (IngredientApiDto ingredient : recipe.getUsedIngredients()) {
        IngredientByIdApiDto ingredientById = apiClientService.getIngredientById(ingredient.getId());
        for (String productName : productsWithPrices.keySet()) {
          if (productName.equals(ingredient.getName())) {
            if (ingredientById.getEstimatedCost().getValue().compareTo(productsWithPrices.get(productName)) > 0) {
              checkAllIngredients = false;
              break;
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
}
