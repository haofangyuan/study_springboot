import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by cage on 2017/11/17.
 */
@SpringBootApplication
@EnableWebSocket
public class App {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler(), "/pmsvr");
    }

    @Bean
    public WebSocketHandler echoWebSocketHandler() {
        return new PbWebSocketHandler();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
