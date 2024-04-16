import java.util.Date;
import java.util.List;

public class Message {
    private String sender;
    private List<String> recipients;
    private Date timestamp;
    private String content;

    public Message(String sender, List<String> recipients, String content) {
        this.sender = sender;
        this.recipients = recipients;
        this.timestamp = new Date();
        this.content = content;
    }

    // Getters for message properties
    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}