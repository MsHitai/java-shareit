package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PatchMapping("/{itemId}")
    public Item partialUpdateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody Map<String, Object> updates,
                                  @PathVariable(required = false) Long itemId) {
        log.debug("Получен запрос PATCH на обновление вещи по id {}", itemId);
        userService.findById(userId);
        Item item = itemService.findById(itemId);
        if (item.getOwnerId() != userId) {
            throw new DataNotFoundException("У пользователя по id " + userId + " нет такой вещи по id " + item.getId());
        }
        return itemService.partialUpdateItem(updates, item);
    }

    @GetMapping("/{itemId}")
    public Item findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                         @PathVariable(required = false) Long itemId) {
        log.debug("Получен запрос GET на получение вещи по id {}", itemId);
        userService.findById(userId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<Item> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос GET на получение вещей пользователя по id {}", userId);
        userService.findById(userId);
        return itemService.findAllItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam String text) {
        log.debug("Получен запрос GET на поиск вещей от пользователя по id {}", userId);
        userService.findById(userId);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.searchItems(text.toLowerCase(), userId);
    }
}
