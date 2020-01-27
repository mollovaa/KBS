package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientApiDto {

  @JsonProperty(value = "id")
  private Long id;

  @JsonProperty(value = "amount")
  private BigDecimal amount;

  @JsonProperty(value = "name")
  private String name;

  @JsonProperty(value = "unit")
  private String unit;
}
