package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        try {
            restTemplate.postForEntity(url, rating, Rating.class);
        } catch (Exception e) {
            throw new RatingException("Error adding rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            return restTemplate.getForEntity(url + "/" + game, Integer.class).getBody();
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) {
        try {
            return restTemplate.getForEntity(url + "/" + game + "/" + player, Integer.class).getBody();
        } catch (Exception e) {
            throw new RatingException("Error getting rating", e);
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}