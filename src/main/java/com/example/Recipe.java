package com.example;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recipe {
    String title;
    String category;
    String instruction;
    String ingredients;
    String photoURL;

    public String getRecipeText(String url) throws Exception{
        ApiController apiController = new ApiController();
        JSONObject object = apiController.getJSON(url);
        Recipe recipe = new Recipe();
        recipe.setTitle(object.getString("strMeal"));
        recipe.setCategory(object.getString("strCategory"));

        String ingredients ="";
        int a = 1;
        while(!(object.getString("strIngredient" + a).equalsIgnoreCase(""))){
            ingredients = ingredients + (object.getString("strIngredient" + a) + ": " +  object.getString("strMeasure" + a) + "\n");
            a++;
        }
        recipe.setIngredients(ingredients);
        
        recipe.setInstruction(object.getString("strInstructions"));

        recipe.setPhotoURL(object.getString("strMealThumb"));
        return recipe.getTitle() + "\n \n" + "Категория: " + recipe.getCategory() + "\n \n" + recipe.getIngredients() + "\n \n" + recipe.getInstruction();
    }

    public ArrayList<Recipe> getMultiplyRecipe(String url1) throws Exception{
        ArrayList<Recipe> recipeList = new ArrayList<>();
        try {
            // Создаем соединение
            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Проверяем статус ответа
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Читаем ответ
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();

                // Парсим JSON
                JSONObject jsonObject = new JSONObject(responseContent.toString());

                // Получаем массив "meals"
                JSONArray mealsArray = jsonObject.optJSONArray("meals");

                if (mealsArray != null) {
                    for (int i = 0; i < mealsArray.length(); i++) {
                        JSONObject object = mealsArray.getJSONObject(i);
                        Recipe recipe = new Recipe();
                        recipe.setTitle(object.getString("strMeal"));
                        recipe.setCategory(object.getString("strCategory"));

                        String ingredients ="";
                        int a = 1;
                        while(!(object.getString("strIngredient" + a).equalsIgnoreCase(""))){
                            ingredients = ingredients + (object.getString("strIngredient" + a) + ": " +  object.getString("strMeasure" + a) + "\n");
                            a++;
                            if(a>20){
                                break;
                            }
                        }
                        recipe.setIngredients(ingredients);
        
                        recipe.setInstruction(object.getString("strInstructions"));

                        recipe.setPhotoURL(object.getString("strMealThumb"));
                        System.out.println(recipe.getTitle() + "\n \n" + "Категория: " + recipe.getCategory() + "\n \n" + recipe.getIngredients() + "\n \n" + recipe.getInstruction());
                        recipeList.add(recipe);
                    }
                } else {
                    return null;
                }
            } else {
                System.out.println("Ошибка при выполнении запроса. Код ответа: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return recipeList;
    }

    
}
