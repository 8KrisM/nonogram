package sk.tuke.gamestudio.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int ident;
    private String game;

    private String player;

    private int rating;

    private Date ratedOn;

    public Rating() {
    }

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getRating() {
        return rating;
    }

    public void setPoints(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date playedOn) {
        this.ratedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", RatedOn=" + ratedOn +
                '}';
    }

}
