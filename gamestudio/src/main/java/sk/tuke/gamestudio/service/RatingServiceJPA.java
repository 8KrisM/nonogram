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
        try {
            entityManager.persist(rating);
        } catch (Exception e) {
            throw new RatingException("Error setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            return entityManager.createQuery("select AVG(rating) from Rating r where r.game = :game", Double.class)
                    .setParameter("game", game)
                    .getSingleResult().intValue();
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }

    }

    @Override
    public int getRating(String game, String player) {
        try {
            return entityManager.createQuery("select r.rating from Rating r where r.game = :game AND r.player = :player", Integer.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }
    }

    @Override
    public void reset() {
        try {
            entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
        } catch (Exception e) {
            throw new RatingException("Error resetting rating", e);
        }
    }
}

