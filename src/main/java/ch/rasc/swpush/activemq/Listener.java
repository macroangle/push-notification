package ch.rasc.swpush.activemq;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.rasc.swpush.fcm.FcmClient;

@Component
public class Listener {
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Autowired
    FcmClient fcmClient;
    
    @JmsListener(destination = "push.notification.queue")
    public void receiveMessage(final Message jsonMessage) throws JMSException, JsonParseException, JsonMappingException, IOException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            System.out.println("Actual text " + messageData);
            
            if('{' == messageData.charAt(0)) {
                ObjectMapper mapper = new ObjectMapper();
                Map mapMessage = mapper.readValue(messageData, Map.class);
             
                String fromUser = (String) mapMessage.get("fromUser");
                String title = (String) mapMessage.get("title");
                String body = (String) mapMessage.get("body");
                
                try {
                    fcmClient.send("chuck", fromUser, title, body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("First char is " + messageData.indexOf(0));
            }
        } else if(jsonMessage instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) jsonMessage;
            Enumeration en = mapMessage.getMapNames();
            while (en.hasMoreElements()) {
                String property = (String) en.nextElement();
                String mapObject = mapMessage.getString(property);
                System.out.println(property + ":" + mapObject);
            }
            String fromUser = mapMessage.getString("fromUser");
            String title = mapMessage.getString("title");
            String body = mapMessage.getString("body");
            
            try {
                fcmClient.send("chuck", fromUser, title, body);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
    }
    
    @JmsListener(destination = "push.notification.queue")
    @SendTo("push.notification.queue")
    public void sendMessage(String message) {
        jmsTemplate.convertAndSend("push.notification.queue", message);
    }
}