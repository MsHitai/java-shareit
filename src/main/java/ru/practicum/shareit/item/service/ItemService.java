package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Item saveItem(ItemDto itemDto, Long userId);

    Item partialUpdateItem(Map<String, Object> updates, Item item);

    Item findById(long itemId);

    List<Item> findAllItems(Long userId);

    List<Item> searchItems(String text, Long userId);
}
