package kbs.project.entities;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {

  //име
  private String name;
  //налично количество
  private BigDecimal availableAmount;  //in gram always
  //енергийна стойност
  private BigDecimal kiloCalories;  //kcal
  //цена
  private BigDecimal price;

}