package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) {
        try{
            entityManager.persist(score);
        }
        catch(Exception e){
            throw new ScoreException("Error adding score",e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try{
            return entityManager.createQuery("select s from Score s where s.game = :game order by s.points desc", Score.class)
                    .setParameter("game", game)
                    .setMaxResults(10)
                    .getResultList();
        }
        catch(Exception e){
            throw new ScoreException("Error getting scores",e);
        }

    }


    @Override
    public void reset() {
        try{
            entityManager.createNativeQuery("DELETE FROM score").executeUpdate();
        }
        catch(Exception e){
            throw new ScoreException("Error resetting score",e);
        }
    }
}
