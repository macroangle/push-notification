package ch.rasc.swpush.activemq;

public class NotificationMessage {
    private String fromUser;
    private String title;
    private String body;
    
    public String getFromUser() {
        return fromUser;
    }
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "NotificationMessage [fromUser=" + fromUser + ", title=" + title + ", body=" + body + "]";
    }
    
}
