import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatHistory implements IterableByUser {
    private List<Message> messages = new ArrayList<>();

    // Add a new message to the history
    public void addMessage(Message message) {
        messages.add(message);
    }

    // Get the last message sent
    public Message getLastMessage() {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }

    // Implement IterableByUser interface to allow iteration over messages by user
    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new SearchMessagesByUser(userToSearchWith, messages.iterator());
    }
}