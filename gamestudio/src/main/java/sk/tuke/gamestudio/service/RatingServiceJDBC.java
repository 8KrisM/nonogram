package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECTAVERAGE = "SELECT game, player, rating, ratedOn FROM rating WHERE game = ?";
    public static final String SELECTRATING = "SELECT game, player, rating, ratedOn FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";

    @Override
    public void setRating(Rating rating) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECTAVERAGE);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                int averageRating = 0;
                int entries=0;
                while (rs.next()) {
                    averageRating=rs.getInt(3);
                    entries++;
                }
                if(entries!=0)return averageRating/entries;
                else return 0;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECTRATING);
        ) {
            statement.setString(1, game);
            statement.setString(2,player);
            try (ResultSet rs = statement.executeQuery()) {
                int rating=0;
                while (rs.next()) {
                    rating=rs.getInt(3);
                }
                return rating;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting average rating", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting rating", e);
        }
    }
}
