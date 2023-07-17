package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        User user = checkUserId(userId);
        Item item = ItemMapper.mapToItem(itemDto, user);
        checkItemIfExists(item);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto partialUpdateItem(Map<String, Object> updates, long itemId, long userId) {
        checkUserId(userId);
        Item item = itemRepository.findById(itemId);
        checkItemIfExists(item);
        if (item.getUser().getId() != userId) {
            throw new DataNotFoundException("У пользователя по id " + userId + " нет такой вещи по id " + item.getId());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(patchItem(updates, item)));
    }

    @Override
    public ItemDto findById(long itemId, long userId) {
        checkUserId(userId);
        Item item = itemRepository.findById(itemId);
        checkItemIfExists(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItems(Long userId) {
        checkUserId(userId);
        return itemRepository.findAllItemsByUser(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {
        checkUserId(userId);
        return itemRepository.searchItemsByNameOrDescriptionContainingIgnoreCase(text, text)
                .stream()
                .filter(Item::isAvailable)
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    private User checkUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Пользователя с таким id нет в базе");
        } else {
            return user.get();
        }
    }

    private void checkItemIfExists(Item item) {
        if (item == null) {
            throw new DataNotFoundException("Вещи с таким id нет в базе");
        }
    }

    private Item patchItem(Map<String, Object> updates, Item itemOld) {
        for (String s : updates.keySet()) {
            switch (s) {
                case "name":
                    itemOld.setName((String) updates.get(s));
                    break;
                case "description":
                    itemOld.setDescription((String) updates.get(s));
                    break;
                case "available":
                    itemOld.setAvailable((Boolean) updates.get(s));
                    break;
            }
        }
        return itemOld;
    }
}
