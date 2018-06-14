package ch.rasc.swpush;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.rasc.swpush.activemq.Listener;
import ch.rasc.swpush.fcm.FcmClient;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class RegistryController {
    @Autowired
    private Listener listener;
    private final FcmClient fcmClient;

    public RegistryController(FcmClient fcmClient) {
        this.fcmClient = fcmClient;
    }

    @PostMapping("/register")
    public void register(@RequestBody Mono<String> token) {
        token.subscribe(t -> this.fcmClient.subscribe("chuck", t));
    }
    
    @PostMapping("/sendNotification")
    public void sendNotification(@RequestBody Map<String, String> message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(message);
        System.out.println(message.toString());
        listener.sendMessage(jsonString);
    }

}
