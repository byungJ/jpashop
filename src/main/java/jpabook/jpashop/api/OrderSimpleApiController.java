package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne)
 * Order 를 조회
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();//Lazy 강제 초기화(order.getMember() proxy객체 .getName()까지 하는 순간 디비에서 값을 가져온다.)
            order.getDelivery().getAddress();//Lazy 강제 초기화
        }

        return all;

    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        //ORDER 2개 [처음에 order가 두개 나오고 그다음에 loop를 돌면서 2번(order1에 맴버/주소, order2에 맴버/주소) 호출] --쿼리5번
        // N + 1 -> 1 + 회원N(2) + 배송N(2)
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        //orders를 그대로 반환하면 안되고 SimpleOrderDto 타입으로 변경을 해야된다.
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;

        // 위에 3줄 한줄로 합쳤다.
        //https://futurecreator.github.io/2018/08/26/java-8-streams/ 람다블로그
        //return orderRepository.findAllByString(new OrderSearch()).stream().map(SimpleOrderDto::new).collect(Collectors.toList());
    }

    /**
     * fetch join사용
     * join: 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않는다. Ex) select T.*
     * fetch join: 연관된 엔티티도 함께 조회(즉시 로딩) SQL를 한번에 조회하는 개념 Ex) select T.*,M.*
     */
    //entity를 조회후 중간에 dto로 변환
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
    }

    // jpa에서 바로 dto를 사용(V3 보다 조금 더 최적화 하지만 딱딱함)
    // 내가 원하는 값만 select
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}