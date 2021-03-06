package kbs.project.entities;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

  //наименование на ястието
  private String name;
  //максимален брой порции, които могат да бъдат приготвени с налчините продукти
  private Integer maxNumberOfPortions;
  //енергийна стойност
  private BigDecimal kiloCalories;
  //цена на една порция
  private BigDecimal pricePerPortion;

}