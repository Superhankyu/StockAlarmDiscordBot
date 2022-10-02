package ggloa.loabot;

import ggloa.loabot.database.Member;
import ggloa.loabot.database.Stock;
import ggloa.loabot.repository.MemberRepository;
import ggloa.loabot.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    public void 등록(){
        Member member = new Member();
        member.setId(123L);
        member.setName("kim");

        memberRepository.save(member);
        Assertions.assertEquals(member, memberRepository.findById(123L));
    }
}
