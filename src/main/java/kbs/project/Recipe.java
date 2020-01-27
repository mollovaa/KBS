package kbs.project;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  //продукти:
  private List<Product> products;

}
