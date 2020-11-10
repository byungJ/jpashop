package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded //@Embeddable를 사용했다고 표시를 해준다(명시적), 둘중에 한개만 있어도 가능하다.
    private Address address;

    //"member" -- Order에 있는 member 필드
    @OneToMany(mappedBy = "member") //한사람이 여러개를 주문 할수있(서로 반대.)
    //컬렉션은 객체로 딱 생성하고 바꾸지말자(하이버네이트가 제공하는
    //내장 컬렉션으로 변경되는데 중간에 바꾸면 원하는 방향으로 안 돌아 간다)

    @JsonIgnore //사용시 주문 정보는 빠지고 순수하게 회원정보만 뿌릴수 있다.(하지만 entity에 화면 뿌리는 로직은 추천하지 않는다)
    private List<Order> orders = new ArrayList<>();
}
