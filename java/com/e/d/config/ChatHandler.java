package com.e.d.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.e.d.model.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.Session;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, WebSocketSession> clients = new HashMap<>();
    private static final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("클라이언트 접속! " + session.getId() + "가 접속함");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageDto msg = mapper.readValue(message.getPayload(), MessageDto.class);
        switch (msg.getType()) {
            case "enter":
                addClient(session, msg);  // 클라이언트 입장 처리
                break;
            case "chat":
                broadcastSend(msg);  // 채팅 메시지 전송
                break;
        }
    }

    private void addClient(WebSocketSession session, MessageDto message) {
        clients.put(message.getSender(), session);
        message.setData(message.getSender() + "님이 입장하셨습니다.");
        broadcastSend(message);
    }

    private void broadcastSend(MessageDto message) {
        for (WebSocketSession client : clients.values()) {
            try {
                TextMessage sendMsg = new TextMessage(mapper.writeValueAsString(message));
                client.sendMessage(sendMsg);  // 클라이언트에게 메시지 전송
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.values().remove(session);
        super.afterConnectionClosed(session, status);
    }
    
    
    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        // 채팅방을 나갈 때 메시지 전송
        new MessageDto("chat", "System", "", username + " 님이 나갔습니다.");
    }

    private void handleEnter(WebSocketSession session, MessageDto msg) throws Exception {
        // 사용자가 채팅방에 입장할 때
        String enterMessage = msg.getSender() + "님이 입장했습니다.";
        broadcastMessage(enterMessage, session);
    }

    private void handleChat(WebSocketSession session, MessageDto msg) throws Exception {
        // 일반 채팅 메시지 처리
        String chatMessage = msg.getSender() + ": " + msg.getData();
        broadcastMessage(chatMessage, session);
    }

    private void handleExit(WebSocketSession session, MessageDto msg) throws Exception {
        // 사용자가 채팅방에서 나갔을 때
        String exitMessage = msg.getSender() + "님이 나갔습니다.";
        broadcastMessage(exitMessage, session);
        sessions.remove(session);  // 채팅방에서 나간 사용자 제거
    }
    
    private void broadcastMessage(String message, WebSocketSession senderSession) throws Exception {
        // 채팅방에 접속한 모든 사용자에게 메시지 전송
        for (WebSocketSession session : sessions) {
            if (session.isOpen() && !session.getId().equals(senderSession.getId())) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
    
}