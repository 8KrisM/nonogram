package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Date;

public class RatingServiceTest {
    private RatingServiceJDBC ratingServiceJDBC;

    public RatingServiceTest() {
        ratingServiceJDBC = new RatingServiceJDBC();
    }

    @Test
    public void resetTest() {
        ratingServiceJDBC.reset();
        int rating = ratingServiceJDBC.getAverageRating("Nonogram");
        Assertions.assertEquals(0, rating);
    }

    @Test
    public void AddAndGetRating() {
        ratingServiceJDBC.reset();
        ratingServiceJDBC.setRating(new Rating("Nonogram", "Tester", 5, new Date(System.currentTimeMillis())));
        int rating = ratingServiceJDBC.getRating("Nonogram", "Tester");
        Assertions.assertEquals(5, rating);
    }
}
