package kbs.project.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kbs.project.apidtos.RecipeByIdApiDto;
import kbs.project.apidtos.RecipeByIngredientsApiDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource(value = "application.properties")
public class ApiClientService {

  // тук ще се прави call към Аpi i резултатът ще се мапва в Recipe;

  @Value("${food.api.key}")
  private String apiKey;

  private String findByIngredientsURL = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=%s"
      + "&number=100&apiKey=%s&ranking=1";

  private String findRecipeById = "https://api.spoonacular.com/recipes/%s/information"
      + "?includeNutrition=true&apiKey=%s";

  private final RestTemplate restTemplate;

  @Autowired
  public ApiClientService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<RecipeByIngredientsApiDto> getRecipesByProducts(List<String> products) {
    String productsSeparatedByComma = products.stream().map(s -> s + ",")
        .collect(Collectors.joining());
    String url = String.format(findByIngredientsURL, productsSeparatedByComma, apiKey);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("User-Agent", "PostmanRuntime/7.20.1");

    ResponseEntity<RecipeByIngredientsApiDto[]> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), RecipeByIngredientsApiDto[].class);

    return new ArrayList<>(Arrays.asList(result.getBody()));
  }

  public RecipeByIdApiDto findRecipeById(Long id) {
    String url = String.format(findRecipeById, id, apiKey);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("User-Agent", "PostmanRuntime/7.20.1");

    ResponseEntity<RecipeByIdApiDto> result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), RecipeByIdApiDto.class);

    return result.getBody();
  }

  //todo get ingredient by id
}