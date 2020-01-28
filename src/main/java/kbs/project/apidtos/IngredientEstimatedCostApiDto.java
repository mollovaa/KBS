package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientEstimatedCostApiDto {

  @JsonProperty(value = "value")
  private BigDecimal value;

  @JsonProperty(value = "unit")
  private String unit;
}
