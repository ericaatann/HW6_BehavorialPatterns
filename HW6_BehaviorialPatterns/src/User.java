import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class User {
    private String username;
    private ChatServer mediator;
    private List<Message> messageHistory = new ArrayList<>();
    private Stack<MessageMemento> mementoStack = new Stack<>();

    public User(String username, ChatServer mediator) {
        this.username = username;
        this.mediator = mediator;
    }

    // Send message to other users through the mediator
    public void sendMessage(List<String> recipients, String content) {
        Message message = new Message(username, recipients, content);
        mediator.sendMessage(message);
        messageHistory.add(message);
    }

    // Receive message from mediator
    public void receiveMessage(Message message) {
        messageHistory.add(message);
    }

    // Undo the last message sent using Memento
    public void undoLastMessage() {
        if (!messageHistory.isEmpty()) {
            Message lastMessage = messageHistory.remove(messageHistory.size() - 1);
            MessageMemento memento = new MessageMemento(lastMessage.getContent(), lastMessage.getTimestamp());
            mementoStack.push(memento);
        }
    }

    // Block messages from specific users
    public void blockUser(String username) {
        mediator.blockUser(username);
    }

    // Getters for user properties
    public String getUsername() {
        return username;
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }

    public Stack<MessageMemento> getMementoStack() {
        return mementoStack;
    }
}
