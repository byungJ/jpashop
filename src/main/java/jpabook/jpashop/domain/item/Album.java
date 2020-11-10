package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
//기본으로 해도 들어간다.
@DiscriminatorValue("A")
public class Album extends Item{

    private String artist;
    private String etc;

}
