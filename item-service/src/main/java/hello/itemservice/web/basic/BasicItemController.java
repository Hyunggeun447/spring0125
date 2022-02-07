package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 붙은 객체 생성자를 만들어줌.
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

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                           @RequestParam Integer price,
                           @RequestParam Integer quantity,
                           Model model
                           ) {
//        Item item = new Item(itemName, price, quantity);
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {  //Model model

        itemRepository.save(item);
//        model.addAttribute("item", item);  //ModelAttribute 때문에 생략 가능
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {  //ModelAttribute 뒤의 class명을 소문자로 바꾼게 디폴트값이라 생략 가능
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {  //ModelAttribute 생략 가능. 그러나 비추. 명확성이 떨어진다.
        itemRepository.save(item);
        return "basic/item";
    }

    //redirect를 하지않으면 새로고침때마다 save명령이 계속 들어감.
    //redirect로 클라이언트에게 명령어를 강제시켜서 해결.
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

//    @PostMapping("/{itemId}/edit")
//    public String editItem(@ModelAttribute Item item) {
//        Long id = item.getId();
//        Item param = new Item(item.getItemName(),item.getPrice(),item.getQuantity());
//
//        itemRepository.update(id,item);
//        return "basic/item";
//    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 1000, 11));
        itemRepository.save(new Item("itemB", 2000, 21));
    }




//    @Autowired // 생성자 하나만 있으면 생략 가능
//    public BasicItemController(ItemRepository itemRepository) { //@RequiredArgsConstructor 때문에 생략 가
//        this.itemRepository = itemRepository;
//    }
}
