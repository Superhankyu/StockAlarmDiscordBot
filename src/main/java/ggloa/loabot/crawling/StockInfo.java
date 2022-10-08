package ggloa.loabot.crawling;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component

public class StockInfo {
    private Map<String, String> stocks = new HashMap<String, String>();

    public StockInfo(){
        this.stocks = GetStockInfo();
    }

    public String GetCode(String StockName){
        return stocks.get(StockName);
    }

    public String GetStockName(String CodeNum){
        CodeNum = "\"" +CodeNum+ "\"";
        for (String key: stocks.keySet()){
            if (stocks.get(key).equals(CodeNum)){
                return key;
            }
        }
        return null;
    }

    public Map GetStockInfo(){

        HashMap<String, String> info = new HashMap<String, String >();
        InputStream in =  getClass().getResourceAsStream("/stockprice.csv");
        InputStreamReader reader = new InputStreamReader(in, Charset.forName("EUC-KR"));

        // File csv = new File("src/main/resources/stockprice.csv");
        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(reader);
           //  br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                info.put(lineArr[3], lineArr[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                    return info;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }


}
