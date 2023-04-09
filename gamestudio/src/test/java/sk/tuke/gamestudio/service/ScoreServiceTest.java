package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;
import java.util.List;

public class ScoreServiceTest {
    private ScoreServiceJDBC scoreServiceJDBC;

    public ScoreServiceTest() {
        scoreServiceJDBC = new ScoreServiceJDBC();
    }

    @Test
    public void resetTest() {
        scoreServiceJDBC.reset();
        List<Score> scores = scoreServiceJDBC.getTopScores("Nonogram");
        Assertions.assertEquals(0, scores.size());
    }

    @Test
    public void AddAndGetRating() {
        scoreServiceJDBC.reset();
        scoreServiceJDBC.addScore(new Score("Nonogram", "Tester", 5, new Date(System.currentTimeMillis())));
        List<Score> scores = scoreServiceJDBC.getTopScores("Nonogram");
        Assertions.assertEquals(5, scores.get(0).getPoints());
    }
}
