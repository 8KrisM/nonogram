package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Date;
import java.util.List;

public class CommentServiceTest {
    private CommentServiceJDBC commentServiceJDBC;

    public CommentServiceTest() {
        commentServiceJDBC = new CommentServiceJDBC();
    }

    @Test
    public void resetTest() {
        commentServiceJDBC.reset();
        List<Comment> comments = commentServiceJDBC.getComments("Nonogram");
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    public void AddAndGetComment() {
        commentServiceJDBC.reset();
        commentServiceJDBC.addComment(new Comment("Nonogram", "Tester", "This is a test", new Date(System.currentTimeMillis())));
        List<Comment> comments = commentServiceJDBC.getComments("Nonogram");
        Assertions.assertEquals("This is a test", comments.get(0).getComment());
        Assertions.assertEquals("Tester", comments.get(0).getPlayer());
    }
}
