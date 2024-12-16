package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(long bookingId);

    List<Booking> findBookingsByItem(Item item);

    @Query("select b from Booking as b where b.booker.id = ?1 and b.item.id =?2 and CURRENT_TIMESTAMP > b.end")
    List<Booking> findPastBookingsByBookerIdAndItmId(long bookerId, long itemId);


    List<Booking> findAllBookingsByBookerId(long bookerId);

    @Query("select b from Booking as b where b.booker.id = ?1 and CURRENT_TIMESTAMP between b.start and b.end")
    List<Booking> findCurrentBookingsByBookerId(long bookerId);

    @Query("select b from Booking as b where b.booker.id = ?1 and CURRENT_TIMESTAMP < b.start")
    List<Booking> findFutureBookingsByBookerId(long bookerId);

    @Query("select b from Booking as b where b.booker.id = ?1 and CURRENT_TIMESTAMP > b.end")
    List<Booking> findPastBookingsByBookerId(long bookerId);

    List<Booking> findAllBookingsByBookerIdAndStatus(long bookerId, Status status);


    @Query("select b from Booking as b where b.item.owner.id = ?1")
    List<Booking> findAllBookingsByOwnerId(long ownerId);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and CURRENT_TIMESTAMP between b.start and b.end")
    List<Booking> findCurrentBookingsByOwnerId(long owner);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and CURRENT_TIMESTAMP < b.start")
    List<Booking> findFutureBookingsByOwnerId(long owner);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and CURRENT_TIMESTAMP > b.end")
    List<Booking> findPastBookingsByOwnerId(long owner);

    @Query("select b from Booking as b where b.item.owner.id = ?1 and b.status = ?2")
    List<Booking> findAllBookingsByOwnerIdAndStatus(long owner, Status status);

}
