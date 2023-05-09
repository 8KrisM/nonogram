package sk.tuke.gamestudio.service;
import sk.tuke.gamestudio.entity.Customer;

public interface UserService {
    void addUser(Customer customer) throws UserException;

    boolean validCredentials(Customer customer) throws UserException;


    void reset() throws CommentException;
}
