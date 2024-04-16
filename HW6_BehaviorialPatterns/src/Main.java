import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();

        User user1 = new User("Alice", server);
        User user2 = new User("Bob", server);
        User user3 = new User("Charlie", server);

        server.registerUser(user1);
        server.registerUser(user2);
        server.registerUser(user3);

        user1.sendMessage(Collections.singletonList("Bob"), "Hello Bob!");
        user2.sendMessage(Collections.singletonList("Alice"), "Hi Alice!");
        user3.sendMessage(Arrays.asList("Alice", "Bob"), "Group chat message!");

        user1.undoLastMessage();

        user2.blockUser("Alice");

        System.out.println("User 1 Message History:");
        for (Message message : user1.getMessageHistory()) {
            System.out.println(message.getSender() + ": " + message.getContent());
        }

        System.out.println("User 2 Message History:");
        for (Message message : user2.getMessageHistory()) {
            System.out.println(message.getSender() + ": " + message.getContent());
        }

        System.out.println("User 3 Message History:");
        for (Message message : user3.getMessageHistory()) {
            System.out.println(message.getSender() + ": " + message.getContent());
        }

        System.out.println("User 1 Messages Sent or Received:");
        ChatHistory user1History = new ChatHistory();
        for (Message message : user1.getMessageHistory()) {
            user1History.addMessage(message);
        }
        Iterator<Message> user1Iterator = user1History.iterator(user1);
        while (user1Iterator.hasNext()) {
            Message message = user1Iterator.next();
            System.out.println(message.getSender() + ": " + message.getContent());
        }
    }
}
