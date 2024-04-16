import java.util.Iterator;
import java.util.List;

public class SearchMessagesByUser implements Iterator<Message> {
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
