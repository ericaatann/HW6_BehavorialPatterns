import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    private List<User> users = new ArrayList<>();
    private Map<String, User> userMap = new HashMap<>();
    private Map<String, List<Message>> blockedMessages = new HashMap<>();

    // Register user with the server
    public void registerUser(User user) {
        users.add(user);
        userMap.put(user.getUsername(), user);
    }

    // Unregister user from the server
    public void unregisterUser(User user) {
        users.remove(user);
        userMap.remove(user.getUsername());
    }

    // Send message from one user to one or more other users
    public void sendMessage(Message message) {
        List<String> recipients = message.getRecipients();
        for (String recipient : recipients) {
            User user = userMap.get(recipient);
            if (user != null && !blockedMessages.containsKey(recipient)) {
                user.receiveMessage(message);
            }
        }
    }

    // Block messages from specific user
    public void blockUser(String username) {
        if (!blockedMessages.containsKey(username)) {
            blockedMessages.put(username, new ArrayList<>());
        }
    }

    // Getters for server properties
    public List<User> getUsers() {
        return users;
    }

    public Map<String, List<Message>> getBlockedMessages() {
        return blockedMessages;
    }
}

