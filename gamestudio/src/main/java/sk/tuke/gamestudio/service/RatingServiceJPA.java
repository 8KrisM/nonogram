package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        try {
            TypedQuery<Rating> query = entityManager.createQuery("SELECT r FROM Rating r WHERE r.player = :player", Rating.class);
            query.setParameter("player", rating.getPlayer());
            List<Rating> ratings = query.getResultList();

            if (!ratings.isEmpty()) {
                Rating existingRating = ratings.get(0);
                existingRating.setPoints(rating.getRating());
                existingRating.setRatedOn(rating.getRatedOn());
                entityManager.merge(existingRating);
            } else {
                entityManager.persist(rating);
            }
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

