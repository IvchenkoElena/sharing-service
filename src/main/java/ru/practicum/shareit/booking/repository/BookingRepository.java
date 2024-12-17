package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByItem(Item item);

    List<Booking> findBookingsByItemAndStatus(Item item, Status status);

    //List<Booking> findBookingsByItemAndStartIsBeforeOrEndIsAfter(Item item, LocalDateTime requestEnd, LocalDateTime requestStart);//пыталась сдлелать запрос с условием, но не заработало

    List<Booking> findBookingsByBookerIdAndItemIdAndEndIsBefore(long bookerId, long itemId, LocalDateTime now);

    List<Booking> findAllBookingsByBookerId(long bookerId);

    List<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfter(long bookerId, LocalDateTime now, LocalDateTime nowAgain);//не придумала, как по-другому написать

    List<Booking> findBookingsByBookerIdAndStartIsAfter(long bookerId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdAndEndIsBefore(long bookerId, LocalDateTime now);

    List<Booking> findAllBookingsByBookerIdAndStatus(long bookerId, Status status);

    List<Booking> findAllBookingsByItemOwnerId(long ownerId);

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long owner, LocalDateTime now, LocalDateTime nowAgain);//хочется использовать between, но не в этом видимо случае?

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfter(long owner, LocalDateTime now);

    List<Booking> findBookingsByItemOwnerIdAndEndIsBefore(long owner, LocalDateTime now);

    List<Booking> findAllBookingsByItemOwnerIdAndStatus(long owner, Status status);

}
