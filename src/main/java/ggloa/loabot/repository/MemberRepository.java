package ggloa.loabot.repository;

import ggloa.loabot.database.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class MemberRepository {

    private EntityManager em;

    public MemberRepository(EntityManager em){
        this.em = em;
    }

    public void save(Member member){
        em.persist(member);
    }

    public Member findById(Long id){
        return em.find(Member.class, id);
    }


}
