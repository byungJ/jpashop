package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
//테이블 이름을 줘야한다 안 그러면 클래스 명을 그대로 따라간다.
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //여러주문은 한 사람한테 매핑될수있다
    @JoinColumn(name = "member_id")
    private Member member; // = new ProxyMember(하이버네이트에서 가짜로 넣어둠,LAZY)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)//통상적으로 권장하는 cascade 범위는, 완전히 개인 소유하는 엔티티
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) //A엔티티를 PERSIST할 때 B 엔티티도 연쇄
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //자바8부터는 @Date를 사용하지 않아도 된다.
    private LocalDateTime orderDate; //주문시간.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL] enum으로 만든다.

    //연관관계 편의메서드 (컨트롤 하는쪽이 들고 있는게 좋다) -- 양방향에서 쓰면 좋다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드(복잡한 생성 부분을 따론 빼서 만든다.)
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직

    /**
     * 주문 취소
     */

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        //orderItem에서 가져오는 이유 거기에 주문가격과 수량이 있다.
        // stream()으로 코드를 깔끔하게 정리.
//        for (OrderItem orderItem :orderItems) {
//            //orderItem에서 가져오는 이유 거기에 주문가격과 수량이 있다.
//            totalPrice += orderItem.getTotalPrice();
//        }
        return totalPrice;
    }
}
