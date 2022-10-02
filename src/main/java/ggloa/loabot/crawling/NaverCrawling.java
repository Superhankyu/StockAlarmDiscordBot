package ggloa.loabot.crawling;

import org.apache.commons.collections4.Get;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NaverCrawling {

    public NaverCrawling(){

    }
    public String GetPrice(String CodeNum){
        String URL = GetURL(CodeNum);
        Connection conn = Jsoup.connect(URL);

        try{
            Document document = conn.get();
            Elements em2 = document.select(" #middle > dl > dd:nth-child(5)");
            return em2.get(0).text().split(" ")[1];

        }catch (IOException e){
            e.printStackTrace();
        }
        return "0";
    }

     private String GetURL(String CodeNum){
        return "https://finance.naver.com/item/main.nhn?code=" + CodeNum; //
    }
}
