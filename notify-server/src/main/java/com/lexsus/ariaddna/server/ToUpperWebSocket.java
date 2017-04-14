package com.lexsus.ariaddna.server;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;

@WebSocket
public class ToUpperWebSocket{

    private UserService service;
    private SharedQueue<Session> queue;
    public ToUpperWebSocket(UserService service) {
        this.service = service;
    }
    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {

        System.out.println("Message received:" + message);

        if (session.isOpen()) {

        String response = message.toUpperCase();

        session.getRemote().sendString(response);

        }

    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        System.out.println(session.getRemoteAddress().getHostString() + " connected!");
        service.addClient(session);

    }


    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println(session.getRemoteAddress().getHostString() + " closed!");
        service.removeClient(session);
    }

}
