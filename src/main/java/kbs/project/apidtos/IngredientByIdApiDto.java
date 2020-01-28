package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientByIdApiDto {

  @JsonProperty(value = "id")
  private Long id;

  @JsonProperty(value = "name")
  private String name;

  @JsonProperty(value = "estimatedCost")
  private IngredientEstimatedCostApiDto estimatedCost;

}