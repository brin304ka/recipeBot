package com.example;

public class UserSession {
    
    boolean isFinished;

    public UserSession(){
        this.isFinished = false;
    }

    public void endSession(){
        isFinished = true;    
    }

    public boolean getSessionStatus(){
        return isFinished;
    }
}
