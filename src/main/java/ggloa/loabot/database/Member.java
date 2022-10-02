package ggloa.loabot.database;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member")
    private List<Stock> stockList;

}
