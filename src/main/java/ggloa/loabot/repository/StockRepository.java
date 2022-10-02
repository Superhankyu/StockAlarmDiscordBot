package ggloa.loabot.repository;

import ggloa.loabot.database.Member;
import ggloa.loabot.database.Stock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class StockRepository {

    private EntityManager em;

    public StockRepository(EntityManager em){
        this.em = em;
    }

    public Long SaveStock(Member member, Long avg_price, String code, Long stock_count){
        Stock stock = new Stock();
        stock.setMember(member);
        stock.setAverage_price(avg_price);
        stock.setCode(code);
        stock.setStock_count(stock_count);

        em.persist(stock);
        return stock.getId();
    }

    public Stock GetStockById(Long id){
        Stock stock = em.find(Stock.class, id);
        return stock;
    }

    public List<Stock> GetStocksByMember(Member member){
        return member.getStockList();
    }


}
