package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId,String name, int price, int stockQuantity){
        /**
         * controller에서 new Book해서 직접만든 객체는 영속성 컨테스트에 등록된 개체가 아니다
         * 준영속이다.(한번 들어갔다 나옴)그래서 영속성 등록시켜줄려면에(변경감지 사용하기 위해서)
         * 밑에 코드 처럼 한번 find로 불러서 영속성에 등록 해줘야 한다
         * merge를 사용해도 되는데 merge는 값이 없으면 기존값을 넣어주는게 아니라
         * null로 그냥 등록해버려서 실무에서 위험하다.
         */

        //findItem.change(price,name,stockQuantity); -- > set말고 이렇게 의미 있는 메서드 만들어서 사용하는게 좋다.
        //이렇게 해야 변경지점이 엔티티로 간다(엔티티에 있다.)
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}