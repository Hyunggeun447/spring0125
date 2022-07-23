package com.store.chichi.controller.item;

import com.store.chichi.domain.item.Item;
import com.store.chichi.domain.item.Shirt;
import com.store.chichi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createItemForm(Model model) {
        model.addAttribute("form", new ItemCreateForm());
        return "items/createForm";
    }

    /**
     * ModelAttribute 이름 주의할 것
     * 나중에 세터 없에는 방법을 강구할 것
     */
    @PostMapping("/items/new")
    public String createItem(@Validated @ModelAttribute("form") ItemCreateForm form, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "items/createForm";
        }

        Item item = new Shirt();
        item.changeItemName(form.getName());
        item.setPrice(form.getPrice());
        item.setStockQuantity(form.getStockQuantity());
        item.setItemColor(form.getColor());
        item.setItemSize(form.getSize());
        item.setGenerateTime(LocalDateTime.now());

        itemService.saveItem(item);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String listItem(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);

        return "items/itemList";
    }
}
