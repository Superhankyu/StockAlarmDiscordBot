package ggloa.loabot.connect;

import ggloa.loabot.message.TListener;
import net.dv8tion.jda.api.JDA;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Timer;

@Component
public class Conn extends ListenerAdapter {
    private TListener tl;


    public Conn(TListener tl){

        this.tl = tl;
        MakeJDA();

    }


    public void MakeJDA(){
        JDA jda = JDABuilder.createDefault("token")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        jda.addEventListener(tl);

    }

}

