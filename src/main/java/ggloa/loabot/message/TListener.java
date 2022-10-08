package ggloa.loabot.message;


import ggloa.loabot.crawling.NaverCrawling;
import ggloa.loabot.crawling.StockInfo;
import ggloa.loabot.database.Member;
import ggloa.loabot.database.Stock;
import ggloa.loabot.repository.MemberRepository;
import ggloa.loabot.repository.StockRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

@Component
public class TListener extends ListenerAdapter {
    private StockInfo stockInfo;
    private Map<String, String> stocks;
    private NaverCrawling naverCrawling;
    private StockRepository stockRepository;
    private MemberRepository memberRepository;
    private SendRequest sendRequest;

    public TListener(StockInfo stockInfo, NaverCrawling naverCrawling, StockRepository stockRepository, MemberRepository memberRepository, SendRequest sendRequest){
        this.stockInfo = stockInfo;
        this.stocks = stockInfo.GetStockInfo();
        this.naverCrawling = naverCrawling;
        this.stockRepository = stockRepository;
        this.memberRepository = memberRepository;
        this.sendRequest = sendRequest;
    }

    @Override
    @Transactional
    public void onMessageReceived(MessageReceivedEvent event) {

        User user = event.getAuthor();
        Message msg = event.getMessage();
        EmbedBuilder embed = new EmbedBuilder();

        if(user.isBot()) return;
        MessageChannel channel = event.getChannel();
        if(msg.getContentRaw().startsWith("!주가")){
            SendPrice(msg, channel, user);
        }
        else if(msg.getContentRaw().startsWith("!등록")){
            SaveStockInfo(msg, channel, user);
        }
        else if(msg.getContentRaw().startsWith("!조회")){
            GetMyStocks(msg, channel, user, embed);
        }
        else if(msg.getContentRaw().startsWith("!help")){
            SendHelpMessage(msg, channel, embed);
        }

    }

    public void SendHelpMessage(Message msg, MessageChannel channel, EmbedBuilder embed){
        embed.setTitle("사용법");
        String embedstr = "1. \"!주가 삼성전자\" 처럼 사용하면 해당 기업의 현재 주가를 알 수 있습니다\n\n " +
                " 2. \"!등록 삼성전자 가격(평단가) 주식수 로 사용하기 \n\n" +
                "ex) !등록 현대차 130000 200 -> 등록하고 나면 !조회 로 현재 수익을 확인할 수 있습니다\n\n" +
                "3. !조회 -> 등록한 주식들에 대해 현재 수익률을 확인할 수 있습니다.\n\n";

        embed.addField("", embedstr, false);

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Transactional
    public void SendPrice(Message msg, MessageChannel channel, User user){
        String[] parse = msg.getContentRaw().split(" ");

        if (parse.length == 2){
            /// todo

            String code = stockInfo.GetCode("\"" + parse[1] + "\""); // 005930
            if (code != null){
                String price = naverCrawling.GetPrice(code.substring(1,code.length()-1));
                channel.sendMessage("Pong!") /* => RestAction<Message> */
                        .queue(response /* => Message */ -> {
                            response.editMessageFormat(parse[1] + " 주가 :" + " %s원" , price).queue();
                        });
            }
            else{
                channel.sendMessage("나는그런거몰랑").queue();
            }
        }
    }

    // 이미 등록된 정보가 있으면 기존의 것을 삭제하고 갱신하는 코드도 짜야함.
    // !등록 삼성전자 59000 (주식수)
    @Transactional
    public void SaveStockInfo(Message msg, MessageChannel channel, User user){
        String[] parse = msg.getContentRaw().split(" ");
        if (parse.length == 4){

            String code = stockInfo.GetCode("\"" + parse[1] + "\"");

            if (code != null){
                try{

                    Long avgPrice = Long.parseLong(parse[2]);
                    Long stock_count = Long.parseLong(parse[3]);
                    String[] p_user = user.toString().split("[0-9a-zA-Z]+[:][0-9a-zA-Z]+[(]");
                    Long id = Long.parseLong(p_user[1].substring(0,p_user[1].length()-1));
                    // todo -> 이미 있는 주식을 다시 등록할 때 수정이 되게한다.
                    if (memberRepository.findById(id) == null){ // 첫등록이라면 멤버에 등록해주기. -> 후에 조회할 때 멤버 아이디를 받아서 조회하기 위함.
                        Member member = new Member();
                        member.setId(id);
                        memberRepository.save(member);
                        stockRepository.SaveStock(member, avgPrice, code.substring(1,code.length()-1), stock_count);
                    }
                    else{
                        Member member = memberRepository.findById(id);
                        List<Stock> stocklist = member.getStockList();
                        for (Stock stock: stocklist){
                            if (stock.getCode().equals(code.substring(1,code.length()-1))){
                                stockRepository.SaveStockChange(stock, avgPrice, stock_count);
                                return;
                            }
                        }
                        stockRepository.SaveStock(member, avgPrice, code.substring(1,code.length()-1), stock_count);
                    }


                }
                catch (IllegalFormatException e){
                    channel.sendMessage("ex) \"!주가 삼성전자 59000 주식수\" 형식을 맞춰주세요");
                    e.printStackTrace();
                }

            }
            else{
                channel.sendMessage("그런종목없엉").queue();
            }
        }

    }

    @Transactional
    public void GetMyStocks(Message msg, MessageChannel channel, User user, EmbedBuilder embed){
        DecimalFormat decFormat = new DecimalFormat("###,###");
        String[] p_user = user.toString().split("[0-9a-zA-Z]+[:][0-9a-zA-Z]+[(]");
        List<Stock> stockList = stockRepository.GetStocksByMember(memberRepository.findById(Long.parseLong(p_user[1].substring(0, p_user[1].length()-1))));
        if (stockList.size() == 0) return;

        Long g_total_buy = 0L;
        Long CurrentAddress = 0L;

        embed.setTitle("주가 알림", null);

        String embedmsg = "";
        for (Stock stock: stockList){
            String Cprice = CurPrice(stock.getCode());
            Long CurrentStatus = Long.parseLong(Cprice.replace(",","")) * stock.getStock_count();
            Long totalbuy = stock.getStock_count() * stock.getAverage_price();
            double percent = (double)(CurrentStatus-totalbuy)*100/(double)totalbuy;
            embedmsg += "\n" + stockInfo.GetStockName(stock.getCode()) + "  주식 수: " + stock.getStock_count() + ", 평균단가: " + decFormat.format(stock.getAverage_price())  + ", 총 매수 금액: " + decFormat.format(totalbuy)
                    + ",  현재 잔고: " + decFormat.format(CurrentStatus) + ", 수익금: " + decFormat.format(CurrentStatus-totalbuy) + ", 수익률: " +  String.format("%.2f", percent) + "%\n";

            // channel.sendMessage(stockInfo.GetStockName(stock.getCode()) + "  주식 수: " + stock.getStock_count() + ", 평균단가: " + decFormat.format(stock.getAverage_price())  + ", 총 매수 금액: " + decFormat.format(totalbuy)
            //+ ",  현재 잔고: " + decFormat.format(CurrentStatus) + ", 수익금: " + decFormat.format(CurrentStatus-totalbuy) + ", 수익률: " +  String.format("%.2f", percent) + "%").setEmbeds(embed.build()).queue();

            g_total_buy += totalbuy;
            CurrentAddress += CurrentStatus;
        }
        embedmsg += "\n 총 매수 금액: " + decFormat.format(g_total_buy) + ", 현재 잔고: " + decFormat.format(CurrentAddress) +", 총 수익률 : " + String.format("%.2f", (double)(CurrentAddress-g_total_buy)*100 / (double)g_total_buy) + "%";
        embed.addField("", embedmsg, false);

        if (CurrentAddress >= g_total_buy) embed.setColor(Color.RED); // 수익중이라면 빨간색으로 표시.
        else embed.setColor(Color.blue);

        channel.sendMessageEmbeds(embed.build()).queue();
        // channel.sendMessage("총 매수 금액: " + decFormat.format(g_total_buy) + ", 현재 잔고: " + decFormat.format(CurrentAddress) +", 총 수익률 : " + String.format("%.2f", (double)(CurrentAddress-g_total_buy)*100 / (double)g_total_buy) + "%").queue();
    }

    // todo 캐시로 만들어서 여러사람이 접근할 때 크롤링 덜하도록하기. 1분단위로 접근시
    public String CurPrice(String codeNum){
        return sendRequest.getPrice(codeNum);
        // return naverCrawling.GetPrice(codeNum);
    }

}