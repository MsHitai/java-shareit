package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findById(long id);

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime start);

    List<Booking> findAllByBookerAndEndBeforeOrderByEndDesc(User booker, LocalDateTime end);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start,
                                                                           LocalDateTime end);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, Status status);

    List<Booking> findByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(long bookerId, long itemId, LocalDateTime end);

    List<Booking> findAllByItemInOrderByStartDesc(List<Item> item);

    List<Booking> findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(List<Item> items, LocalDateTime now,
                                                                           LocalDateTime now1);

    List<Booking> findAllByItemInAndEndBeforeOrderByEndDesc(List<Item> items, LocalDateTime now);

    List<Booking> findAllByItemInAndStartAfterOrderByStartDesc(List<Item> items, LocalDateTime now);

    List<Booking> findAllByItemInAndStatusEqualsOrderByStartDesc(List<Item> items, Status status);

    List<Booking> findAllByItemAndStatusOrderByEndAsc(Item item, Status status);
}
