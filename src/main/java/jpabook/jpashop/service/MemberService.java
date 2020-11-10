package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// @AllArgsConstructor lombok에서 필드의 모든것을 가지고 생성자를 만들어줌.
@RequiredArgsConstructor //final이 있는 필드만 생성자를 만들어줌.(lombok)
public class MemberService {

    private final MemberRepository memberRepository;

//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } --- @RequiredArgsConstructor

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member){

        vaildateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    private void vaildateDuplicateMember(Member member) {
        //그래도 혹시 모르니까 데이터베이스 쪽에서 member의 name을 유니크한 제약조건 걸기.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        } }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단 건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        //찾은다음에 set 변경감지에 의해 실행.
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
