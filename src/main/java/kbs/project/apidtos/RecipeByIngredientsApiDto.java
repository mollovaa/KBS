package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeByIngredientsApiDto {

  @JsonProperty(value = "id")
  private Long id;

  @JsonProperty(value = "title")
  private String title;

  @JsonProperty(value = "missedIngredientCount")
  private Integer missedIngredientCount;

  @JsonProperty(value = "usedIngredients")
  private List<IngredientApiDto> usedIngredients;

}