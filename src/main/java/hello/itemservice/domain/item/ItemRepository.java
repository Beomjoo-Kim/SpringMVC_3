package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    //실제 서비스(멀티 스레드) 환경에선 hashmap 사용 금지.
    //ConcurrentHashMap 사용.
    //sequence 또한 long 사용 금지.
    private static Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        //arrayList 로 감싸서 반환하게 되면, 해당 arrayList 가 수정되더라도 내부의 store 은 변하지 않는다.
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        //이 경우엔 따로 DTO 를 만들어 update 되는 항목만 가지는 객체를 생성하여 작업하는 것이 맞다.
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
