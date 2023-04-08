package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) {
        return entityManager.createQuery("select AVG(rating) from Rating r where r.game = :game", int.class)
                .setParameter("game", game)
                .getSingleResult();
    }

    @Override
    public int getRating(String game, String player) {
        return entityManager.createQuery("select r.rating from Rating r where r.game = :game AND r.player = :player", int.class)
                .setParameter("game", game)
                .setParameter("player",player)
                .getSingleResult();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}

