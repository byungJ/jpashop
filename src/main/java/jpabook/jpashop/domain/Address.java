package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

// jpa내장타입
@Embeddable
@Getter
//값 타입 변경될 일이 거의없다 생성할때만 값을 세팅하고 끝
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //생성자로 세팅하면 꼭 기본생성자도 만들어줘야한다.
    //jpa가(proxy 사용해서) 생성할때 기본생성자가 있어야지 만들어줄수있다.
    //protected 사용해주기(호출 못하게 할려고)
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
