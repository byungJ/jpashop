package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

//V4(화면조회전용?) : api 스펙이 맞게 최적화된 쿼리,dto 따로 패키지로 빼는것도 좋다.
//V3은 재사용성이 좋고, V4는 유지보수성이 좋다.
//V3번에 너무 많은 select 절이 나갈때 V4를 사용.
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        //entity,value object(embeddable address) 만 반환이 가능
        //dto는 안된다 할려면 new를 사용해줘야 한다.
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address) from Order o join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }
}
