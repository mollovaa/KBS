package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientNutritionApiDto {

  @JsonProperty(value = "name")
  private String name;

  @JsonProperty(value = "amount")
  private BigDecimal amount;

  @JsonProperty(value = "unit")
  private String unit;

  @JsonProperty(value = "nutrients")
  private List<NutrientApiDto> nutrients;
}
