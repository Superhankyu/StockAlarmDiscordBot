package ggloa.loabot.database;


import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class AutoAlarm {

    @Id
    @GeneratedValue
    private Long id;

}

