package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        User user = checkUserId(userId);
        Item item = checkItem(bookingDto.getItemId());
        Booking booking = BookingMapper.mapToBooking(bookingDto, user, item, Status.WAITING);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, boolean approved) {
        checkUserId(userId);
        Booking booking = checkBookingId(bookingId);
        long itemId = booking.getItem().getId();
        List<Item> items = itemRepository.findAllItemsByUser(userId);
        if (!checkIfUserIsOwner(items, itemId)) {
            throw new DataNotFoundException("Пользователь с id " + userId + " не владелей вещи по id " + itemId);
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Бронь по id " + bookingId + " уже одобрена");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findById(Long userId, Long bookingId) {
        checkUserId(userId);
        Booking booking = checkBookingId(bookingId);
        long itemId = booking.getItem().getId();
        List<Item> items = itemRepository.findAllItemsByUser(userId);
        if (!checkIfUserIsOwner(items, itemId) && userId != booking.getBooker().getId()) {
            throw new DataNotFoundException("Пользователь с id " + userId + " не владелей или не бронировал " +
                    "вещь по id " + itemId);
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> findAllBookingsByUser(Long userId, String state) {
        User user = checkUserId(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerOrderByStartDesc(user)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                                LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByBookerAndEndBeforeOrderByEndDesc(user, LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, Status.WAITING)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, Status.REJECTED)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> findAllBookingsByOwner(Long userId, String state) {
        checkUserId(userId);
        List<Item> items = itemRepository.findAllItemsByUser(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemInOrderByStartDesc(items)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(items,
                                LocalDateTime.now(), LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByItemInAndEndBeforeOrderByEndDesc(items, LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByItemInAndStartAfterOrderByStartDesc(items, LocalDateTime.now())
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByItemInAndStatusEqualsOrderByStartDesc(items, Status.WAITING)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByItemInAndStatusEqualsOrderByStartDesc(items, Status.REJECTED)
                        .stream().map(BookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }


    private User checkUserId(long userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new DataNotFoundException("Пользователя с id " + userId + " нет в базе данных");
        }
        return user;
    }

    private Boolean checkIfUserIsOwner(List<Item> items, long itemId) {
        boolean isFound = false;
        for (Item item : items) {
            if (item.getId() == itemId) {
                isFound = true;
                break; // так как нет смысла продолжать поиск, если найдена вещь
            }
        }
        return isFound;
    }

    private Item checkItem(long itemId) {
        Item item = itemRepository.findById(itemId);

        if (item == null) {
            throw new DataNotFoundException("Вещи с id " + itemId + " нет в базе данных");
        }
        if (!item.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вещь с id " + itemId + " не доступна для брони");
        }
        return item;
    }

    private Booking checkBookingId(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);

        if (booking == null) {
            throw new DataNotFoundException("Брони с id " + bookingId + " нет в базе данных");
        }
        return booking;
    }
}
