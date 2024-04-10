import java.util.*;

// Message class representing a message sent by a user
class Message {
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

    // Memento class representing a snapshot of a message
    private static class MessageMemento {
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

    // User class representing a user of the chat application
    static class User {
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

    // ChatServer class acting as a mediator between users
    static class ChatServer {
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

    // ChatHistory class that stores the chat history for a user
    static class ChatHistory implements IterableByUser {
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

    // Interface for iterable by user
    public interface IterableByUser {
        Iterator<Message> iterator(User userToSearchWith);
    }

    // Iterator class for searching messages by user
    static class SearchMessagesByUser implements Iterator<Message> {
        private User userToSearchWith;
        private Iterator<Message> iterator;

        public SearchMessagesByUser(User userToSearchWith, Iterator<Message> iterator) {
            this.userToSearchWith = userToSearchWith;
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            while (iterator.hasNext()) {
                Message nextMessage = iterator.next();
                if (nextMessage.getSender().equals(userToSearchWith.getUsername()) ||
                        nextMessage.getRecipients().contains(userToSearchWith.getUsername())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Message next() {
            while (iterator.hasNext()) {
                Message nextMessage = iterator.next();
                if (nextMessage.getSender().equals(userToSearchWith.getUsername()) ||
                        nextMessage.getRecipients().contains(userToSearchWith.getUsername())) {
                    return nextMessage;
                }
            }
            return null;
        }
    }

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

