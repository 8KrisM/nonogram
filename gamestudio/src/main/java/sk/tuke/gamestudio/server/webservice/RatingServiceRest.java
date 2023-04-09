package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;


@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}")
    public int getAverageRating(@PathVariable String game) {
        try{
            return ratingService.getAverageRating(game);
        }
        catch(Exception e){
            throw new RatingException("Error getting rating",e);
        }
    }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) {
        try{
            return ratingService.getRating(game, player);
        }
        catch(Exception e){
            throw new RatingException("Error getting rating",e);
        }
    }

    @PostMapping
    public void addScore(@RequestBody Rating rating) {
        try{
            ratingService.setRating(rating);
        }
        catch(Exception e){
            throw new RatingException("Error adding rating",e);
        }
    }
}
