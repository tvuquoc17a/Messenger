package com.example.messenger.signalr;

import android.util.Log;

import com.example.messenger.Constants;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import io.reactivex.rxjava3.core.Single;

public class SignalRListener {

    private static  SignalRListener instance;

    HubConnection hubConnection;
    private static String  accessToken;

    public SignalRListener(String accessToken) {
        this.accessToken = accessToken;
        hubConnection = HubConnectionBuilder.create(Constants.BASE_URL_SIGNALR)
                .withAccessTokenProvider(Single.defer(() -> Single.just(accessToken))) // Cài đặt nhà cung cấp mã thông báo truy cập
                .build();
        hubConnection.start();
        hubConnection.on("newMessage", (message) -> {
            Log.d("newMessage", "message" + message);
        }, SignalrMessage.class);
    }

    public static SignalRListener getInstance(String accessToken){
        if(instance == null) {
            instance = new SignalRListener(accessToken);
        }
        return instance;
    }

    public boolean startConnection(){
        try{
            if(hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
                hubConnection.start().blockingAwait();
                return true;
            }
            else return false;
        }
        catch (Exception e){
            if (e instanceof com.microsoft.signalr.HttpRequestException && e.getMessage().contains("401")) {
                Log.e("SignalR", "Authentication error when starting SignalR connection", e);
            } else {
                Log.e("SignalR", "Error starting SignalR connection", e);
            }
            return false;
        }
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
