package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    @PostMapping("/add")
    //@ModelAttribute 의 경우 사용한 뒤 자동으로 model 에 addAttribute 를 진행한다.
    //이 때 사용되는 이름은 @ModelAttribute 를 사용할 때 소괄호안에 지정할 수 있다.
    //지정하지 않을 경우 해당 객체의 class 명의 첫글자를 소문자로 바꾼 것을 사용한다.
    //추가적으로, @ModelAttribute 는 생략이 가능하다. 이 때 model 에 들어가는 이름은 지정하지 않았을 때와 동일하다.
    public String addItem(Item item, Model model) {
        itemRepository.save(item);
        //PRG -> post/redirect/get
        //이렇게 처리를 하게 되면 중간에 redirect 가 들어가기 때문에 refresh 를 진행해도 같은 요청이 반복되지 않는다.
        return "redirect:/basic/items/" + item.getId();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, @ModelAttribute Item item) {
        Item originalItem = itemRepository.findById(itemId);
        itemRepository.update(itemId, item);
        //return 에 redirect 를 set 하고 {itemId} 를 입력하게되면 @PathVariable 에서 받아온 값이 자동으로 들어간다.
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * test init
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
