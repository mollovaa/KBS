package kbs.project.apidtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrientApiDto {

  @JsonProperty(value = "title")
  private String title;

  @JsonProperty(value = "amount")
  private BigDecimal amount;

  @JsonProperty(value = "unit")
  private String unit; //cal
}
