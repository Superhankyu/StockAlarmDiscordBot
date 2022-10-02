package ggloa.loabot.message;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class SendRequest {
    WebClient client = WebClient.builder().baseUrl("http://localhost:8081").defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081"))
            .build();;

    public SendRequest(){

    }
    public String getPrice(String code){

        Mono<String> list = client.get().uri("stockprice/codenum/" + code).retrieve().bodyToMono(String.class);
        String msg =  list.block();
        return msg;
    }

}
