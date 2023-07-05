package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {
    Item save(Item item);

    Item findById(long itemId);

    Item partialUpdateItem(Map<String, Object> updates, Item itemOld);

    List<Item> findAllItems(Long userId);

    List<Item> searchItems(String text, Long userId);
}
