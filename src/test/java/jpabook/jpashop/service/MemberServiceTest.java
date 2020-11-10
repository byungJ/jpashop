package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest //springboot를 띄운상태에서 테스트를 할려면 작성해줘야 한다.
//jpa는 commit이 되어야지 insert문을 날린다.
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given이 주어졌을 when 이렇게(이걸 실행) 하면 then (이게 나와야돼) 이렇게 된다.
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    void 중복_회원_예외() throws Exception
    {
        //given
        String name = "1hoon";

        Member memberA = new Member();
        memberA.setName(name);

        Member memberB = new Member();
        memberB.setName(name);

        //when
        memberService.join(memberA);

        //then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(memberB));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }

}