package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.nonogram.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.service.*;


@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
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
    public GameField field() throws Exception {
        int size = new ConsoleUI().selectSize();
        return new GameField(size, size, GameField.Type.BLACKANDWHITE);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public UserService userService(){return new UserServiceJPA();}

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
