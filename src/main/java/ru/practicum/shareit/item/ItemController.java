package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public Item saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                         @Valid @RequestBody ItemDto itemDto) {
        log.debug("Поступил запрос POST на создание вещи {} от пользователя по id {}",
                itemDto.toString(), userId);
        User user = userService.findById(userId);
        return itemService.saveItem(itemDto, user.getId());
    }
}
