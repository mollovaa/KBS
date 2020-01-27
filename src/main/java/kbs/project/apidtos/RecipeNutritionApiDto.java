package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeNutritionApiDto {

  @JsonProperty(value = "nutrients")
  private List<NutrientApiDto> nutrients;

  @JsonProperty(value = "ingredients")
  private List<IngredientNutritionApiDto> ingredients;

}
