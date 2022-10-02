package ggloa.loabot.database;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue
    private Long id;

    private String code; // stock code_number
    private Long average_price;
    private Long stock_count;

    @ManyToOne
    @JoinColumn
    private Member member;

}
