package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
/**
 * member entity가 있는데 새롭게 만들어서 사용하는 이유는 개체를 사용하게 되면 화면에 맞게 추가로 이것저것 건드려야된다.
 * 실무에서 form이랑 entity랑 차이가 있을수 밖에 없으므로 화면에서 건드는 부분은 따로 빼서 사용 해주는 것이 좋다.
 * jpa에서는 entity는 순수하게 db를 처리하는 기능만 있는게 좋다(화면처리 로직은 따로 form 또는 dto(Data Transaction Object) 만들기)
 * api를 만들때는 무조건 entity를 반환하면 안된다.
 */
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;


}
