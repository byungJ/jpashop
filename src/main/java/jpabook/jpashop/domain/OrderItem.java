package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
//생성자에 protected (OrderItem을 new로 직접 생성해서 사용하는 것을 막아준다)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)//하나의 주문이 여러개의 OrderItem을 가질수 있다.
    @JoinColumn(name = "order_id") //Many에 FK가 들어간다.
    private Order order;

    private int orderPrice; //주문당시에 가격.
    private int count; //주문 당시 수량.

    //==생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrcie, int count) {

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrcie);
        orderItem.setCount(count);
        item.removeStock(count); //orderitem을 만들면서 넘어온것만큼 아이템의 재고를 처리한다.

        return orderItem;
    }

    //== 비즈니스 로직
    public void cancel() {
        getItem().addStock(count); //아이템의 재고수량을 원복
    }

    //==조회 로직
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
