package com.example.messenger.signalr;

import android.util.Log;

import com.example.messenger.Constants;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

public class SignalRListener {

    private static  SignalRListener instance;

    HubConnection hubConnection;

    public SignalRListener() {
        hubConnection = HubConnectionBuilder.create(Constants.BASE_URL_SIGNALR).build();
        hubConnection.start();
        hubConnection.on("newMessage", (message) -> {
            Log.d("newMessage", "message" + message);
        }, SignalrMessage.class);
    }

    public static SignalRListener getInstance(){
        if(instance == null) {
            instance = new SignalRListener();
        }
        return instance;
    }

    public boolean startConnection(){
        if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
            hubConnection.start();
            return true;
        }
        else return false;
    }

    public boolean stopConnection(){
        if(hubConnection.getConnectionState() == HubConnectionState.CONNECTED){
            hubConnection.stop();
            return true;
        }
        else return false;
    }

    public String getConnectionState(){
        return hubConnection.getConnectionState().toString();
    }
    public void sendMessage(SignalrMessage message, Integer roomId ){
        if(startConnection()){
            hubConnection.send("creatMessages", message, roomId);
        }
    }
}
