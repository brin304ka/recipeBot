package com.example;


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

}
