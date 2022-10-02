package ggloa.loabot;

import ggloa.loabot.database.Member;
import ggloa.loabot.repository.MemberRepository;
import ggloa.loabot.repository.StockRepository;
import ggloa.loabot.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional(readOnly = true)
public class StockRepositoryTest {

    @Autowired
    StockRepository stockRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    public void 주식등록(){
        Member member = new Member();
        member.setName("kim");
        member.setId(123L);
        memberRepository.save(member);

        Long id = stockRepository.SaveStock(member, 1234L, "123", 222L);
        Assertions.assertEquals(memberRepository.findById(member.getId()), stockRepository.GetStockById(id).getMember());

    }

}
