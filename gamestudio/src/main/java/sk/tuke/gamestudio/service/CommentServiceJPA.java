package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) {
        try {
            entityManager.persist(comment);
        } catch (Exception e) {
            throw new CommentException("Error adding comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try {
            return entityManager.createQuery("select c from Comment c where c.game = :game", Comment.class)
                    .setParameter("game", game)
                    .getResultList();
        } catch (Exception e) {
            throw new CommentException("Error getting comment", e);
        }
    }


    @Override
    public void reset() {
        try {
            entityManager.createNativeQuery("DELETE FROM comment").executeUpdate();
        } catch (Exception e) {
            throw new CommentException("Error resetting comment", e);
        }
    }
}