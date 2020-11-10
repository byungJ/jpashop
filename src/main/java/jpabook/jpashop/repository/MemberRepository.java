package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //스프링이 jpa가 생성한 EntityManager을 자동으로 만들어서 인젝션(주입)해준다.
    // @PersistenceContext

    // 1)@PersistenceContext --> @Autowired 스트링부트jpa를 사용하면 이걸로 바꿀수 있다
    // 2)그러면 또 lombok의 @RequiredArgsConstructor가 사용가능
    private final EntityManager em;

    //기본생성자가 있는 상태;(스프링에서)

    public void save(Member member) {
        //jpa 저장하는 로직이 된다.
        em.persist(member);
    }

    public Member findOne(Long id) {
        //첫번째타입,두번째pk
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        //JPQL쿼리(개체를 대상),반환타입
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        // : 파라미터를 바인딩.
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                // "name"이 :name 파라미터에 바인딩.
                .setParameter("name",name).getResultList();
    }
}
