package com.example;


import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    private final Map<Long, UserSession> sessions = new HashMap<>();

    @Override
    public String getBotUsername(){
        return "";
    }

    @Override
    public String getBotToken(){
        return "";
    }

    @Override
    public void onUpdateReceived(Update update){
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long id = update.getMessage().getChatId();
            String responce = null;
            Recipe recipe = new Recipe();
            UserSession session = sessions.get(id);

            try {
                if((!(session==null))&&!session.isFinished){
                    try {
                        sendMessage(id, recipe.getRecipeText("https://www.themealdb.com/api/json/v1/1/search.php?s=" + messageText));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(id, "I couldn't find a recipe with that name. Try another one");
                    }
                    session.endSession();
                    return;
                }

                switch (messageText.toLowerCase()) {
                    case "/start":
                        sendMessage(id, "Hi! I'm recipe bot. Let's choose what we are going to cook today!");
                        break;
                    case "/randomrecipe":
                        responce = recipe.getRecipeText("https://www.themealdb.com/api/json/v1/1/random.php");
                        break;
                    case "/recipebytitle":
                        session = new UserSession();
                        sessions.put(id, session);
                        sendMessage(id, "What recipe do you want to get?");
                        break;
                    default:
                        responce = "I don't understand this command. Try something another";
                        break;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendMessage(id,responce);
        }
    }

    private void sendMessage(long id, String text) {
        SendMessage sendMessage = new SendMessage();
        
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        try{
            execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendRecipe(long id,Recipe recipe, String url) throws Exception{
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(id);
        sendPhoto.setCaption(recipe.getRecipeText(url));
        
    }
}
