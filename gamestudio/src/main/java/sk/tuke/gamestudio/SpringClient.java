package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.nonogram.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@Configuration
public class SpringClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class, args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {

        return args -> ui.play();
    }

    @Bean
    public ConsoleUI consoleUI(GameField gameField) {
        return new ConsoleUI(gameField);
    }

    @Bean
    public GameField field() {
        int size= new ConsoleUI().selectSize();
        return new GameField(size, size, GameField.Type.BLACKANDWHITE);
    }

    @Bean
    public ScoreServiceJPA scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public RatingServiceJPA ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public CommentServiceJPA commentService() {
        return new CommentServiceJPA();
    }

}
