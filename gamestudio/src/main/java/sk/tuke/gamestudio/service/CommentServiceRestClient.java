package sk.tuke.gamestudio.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommentServiceRestClient implements CommentService {
    private final String url = "http://localhost:8080/api/comment";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        try{
            restTemplate.postForEntity(url, comment, Comment.class);
        }
        catch (Exception e){
            throw new CommentException("Error adding comment",e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try{
            return Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game, Comment[].class).getBody()));
        }
        catch (Exception e){
            throw new CommentException("Error getting comments",e);
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
