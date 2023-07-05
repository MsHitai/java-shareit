package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item saveItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.mapToItem(itemDto, userId);
        return itemRepository.save(item);
    }

    @Override
    public Item partialUpdateItem(Map<String, Object> updates, Item item) {
        return itemRepository.partialUpdateItem(updates, item);
    }

    @Override
    public Item findById(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> findAllItems(Long userId) {
        return itemRepository.findAllItems(userId);
    }

    @Override
    public List<Item> searchItems(String text, Long userId) {
        return itemRepository.searchItems(text, userId);
    }
}
