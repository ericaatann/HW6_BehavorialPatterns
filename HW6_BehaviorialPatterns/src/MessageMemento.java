import java.util.Date;

public class MessageMemento {
    private String content;
    private Date timestamp;

    public MessageMemento(String content, Date timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters for memento properties
    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
