package ch.rasc.swpush.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    
    @Autowired
    JmsTemplate jmsTemplate;
    
    @JmsListener(destination = "push.notification.queue")
    public void receiveMessage(final Message jsonMessage) throws JMSException {
        String messageData = null;
        System.out.println("Received message " + jsonMessage);
        if(jsonMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)jsonMessage;
            messageData = textMessage.getText();
            System.out.println("Actual text " + messageData);
        }
    }
    
    @JmsListener(destination = "push.notification.queue")
    @SendTo("push.notification.queue")
    public void sendMessage(String message) {
        jmsTemplate.convertAndSend("push.notification.queue", message);
    }
}