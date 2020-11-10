package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//부모테이블에 전략을 적어줘야한다.
//싱글테이블 (한 테이블에 다 때려 박는다.)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    //Item items필드에 의해 매핑이 되었다.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==(재고조절)
    //도메인주도 설계(엔티티 자체가 해결할수 있는 문제는 엔티티 안에 넣는게 객체지향적이고 좋다)
    //즉 데이터를 가지고 있는쪽에서 비즈니스 메서드가 있는게 가장 좋다(응집력이 좋다)

    /**
     * stock증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;

    }

}
