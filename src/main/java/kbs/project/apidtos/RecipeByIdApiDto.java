package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
public class RecipeByIdApiDto {

  @JsonProperty(value = "pricePerServing")
  private BigDecimal pricePerServing;

  @JsonProperty(value = "servings")
  private Integer servings;

  @JsonProperty(value = "nutrition")
  private RecipeNutritionApiDto nutrition;

}
