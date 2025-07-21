package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    private final Map<Long, UserSession> sessions = new HashMap<>();

    @Override
    public String getBotUsername(){
        return "@recipesForMumbot";
    }

    @Override
    public String getBotToken(){
        return "7605976168:AAFs63sOkXcvMeHDrJWaNS3We7xp1pmQWyk";
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
                        ArrayList<Recipe> recipeList = new ArrayList<>();
                        recipeList = recipe.getMultiplyRecipe("https://www.themealdb.com/api/json/v1/1/search.php?s=" + messageText);
                        for (Recipe recipe2 : recipeList) {
                            sendMessage(id, recipe2.getTitle() + "\n \n" + "Category: " + recipe2.getCategory() + "\n \n" + recipe2.getIngredients() + "\n \n" + recipe2.getInstruction());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(id, "I couldn't find a recipe with that name. Try another one");
                    }
                    session.endSession();
                    return;
                }

                switch (messageText) {
                    case "/start":
                        sendMessageWithKeyboard(id, "Hi! I'm recipe bot. Let's choose what we are going to cook today!");
                        break;
                    case "Get random recipe":
                        responce = recipe.getRecipeText("https://www.themealdb.com/api/json/v1/1/random.php");
                        break;
                    case "Find recipe by title":
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

    private void sendMessageWithKeyboard(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        // Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        // Создаем строки клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Get random recipe"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Find recipe by title"));

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        
        // Можно отключить автоматическую очистку клавиатуры после нажатия
        keyboardMarkup.setOneTimeKeyboard(true);
        
        // Можно оставить клавиатуру всегда видимой или скрывать после выбора
        keyboardMarkup.setResizeKeyboard(false);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
