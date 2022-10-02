package ggloa.loabot;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonoTests {

    Mono<String> blockingHelloWorld() {
        return Mono.just("Hello world!");
    }

    @Test
    public void Test(){
        String result = blockingHelloWorld().block();
        assertEquals("Hello world!", result);

    }

}
