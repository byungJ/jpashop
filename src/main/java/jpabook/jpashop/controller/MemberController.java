package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        //화면에서 new MemberFrom()에 접근할수 있게 된다.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    //MemberForm이 파라미터로 넘어온다..@Valid((@NotEmpty,,등등 다양한 어노테이션 검증),@NotEmpty 필수로 사용하고싶다)
    //@Valid 다음에 BindingResult result가 있으면 오류가 * result에 담긴다.(그다음 밑에 코드 실행된다)
    public String create(@Valid MemberForm form, BindingResult result) {

        // * result에 오류가 담겨있고 밑에 코드가 실행되니까

        //오류가 들어왔을때 재가입으로 보낸다.
        //Spring이 BindingResult result에 담긴 오류를 화면까지 끌고 가준다(form값도 당연히 가져감)
        // 그리고나면 @NotEmpty에 있는 문구가 출력된다.
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(),form.getStreet(),form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        model.addAttribute("members", memberService.findMembers());
//        List<Member> members = memberService.findMembers(); 둘줄이 위에 한줄로;
//        model.addAttribute("members",members);
        return "members/memberList";
    }

}
