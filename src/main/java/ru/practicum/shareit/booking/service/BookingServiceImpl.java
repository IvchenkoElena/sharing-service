package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(long bookerId, NewBookingRequest request) {
        User booker = userRepository.findUserById(bookerId);
        if (booker == null) {
            String message = "Пользователь с ID " + bookerId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        Item item = itemRepository.findItemById(request.getItemId());
        if (item == null) {
            String message = "Вещь с ID " + request.getItemId() + " не найдена";
            log.error(message);
            throw new NotFoundException(message);
        }
        if (!item.getAvailable()) {
            String message = "Вещь недоступна для бронирования";
            log.error(message);
            throw new ValidationException(message);
        }

//        if (booker.equals(item.getOwner())) {
//            String message = "Нельзя бронировать свою вещь";
//            log.error(message);
//            throw new NotFoundException(message);
//        }

        //Проверка пересечения

        List<Booking> bookingsList = bookingRepository.findBookingsByItem(item);
        if (bookingsList.stream()
                .anyMatch(b -> isTimeCross(b, request))) {
            String message = "В это время вещь занята";
            log.error(message);
            throw new ValidationException(message);
        }

        Booking booking = BookingMapper.mapToBooking(booker, item, request);
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    private boolean isTimeCross(Booking booking, NewBookingRequest request) {
        boolean notCross = request.getStart().isAfter(booking.getEnd()) || request.getEnd().isBefore(booking.getStart());
        return !notCross;
    }

    @Override
    public BookingDto approveBooking(long ownerId, long bookingId, Boolean approved) {
        //проверка, что бронирование существует
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            String message = "Бронирование c ID " + bookingId + " не найдено";
            log.error(message);
            throw new NotFoundException(message);
        }
        //проверка, что пользователь является хозяином вещи
        if (ownerId != booking.getItem().getOwner().getId()) {
            String message = "У вещи с ID " + booking.getItem().getId() + " другой владелец";
            log.error(message);
            throw new ValidationException(message);
        }
        //меняем статус
//        if (approved) {
//            booking.setStatus(Status.APPROVED);
//        } else {
//            booking.setStatus(Status.REJECTED);
//        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        //перезаписываем в репозиторий
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        //проверка, что бронирование существует
        if (booking == null) {
            String message = "Бронирование c ID " + bookingId + " не найдено";
            log.error(message);
            throw new NotFoundException(message);
        }
        //Проверяем, что пользователь либо автор бронирования, либо владелец
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            String message = "Недопустимый пользователь";
            log.error(message);
            throw new NotFoundException(message);
        }
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(long bookerId, String state) {
        User booker = userRepository.findUserById(bookerId);
        if (booker == null) {
            String message = "Пользователь с ID " + bookerId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        State currentState = State.valueOf(state);
        List<Booking> bookingsList;

        switch (currentState) {
            case ALL -> bookingsList = bookingRepository.findAllBookingsByBookerId(bookerId);
            case CURRENT -> bookingsList = bookingRepository.findCurrentBookingsByBookerId(bookerId);
            case PAST -> bookingsList = bookingRepository.findPastBookingsByBookerId(bookerId);
            case FUTURE -> bookingsList = bookingRepository.findFutureBookingsByBookerId(bookerId);
            case WAITING -> bookingsList = bookingRepository.findAllBookingsByBookerIdAndStatus(bookerId, Status.WAITING);
            case REJECTED -> bookingsList = bookingRepository.findAllBookingsByBookerIdAndStatus(bookerId, Status.REJECTED);
            default -> throw new NotFoundException("Статус указан неверно");
        }

        return bookingsList.stream()
                .map(BookingMapper::mapToBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(long ownerId, String state) {
        User owner = userRepository.findUserById(ownerId);
        if (owner == null) {
            String message = "Пользователь с ID " + ownerId + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        }
        State currentState = State.valueOf(state);
        List<Booking> bookingsList;

        switch (currentState) {
            case ALL -> bookingsList = bookingRepository.findAllBookingsByOwnerId(ownerId);
            case CURRENT -> bookingsList = bookingRepository.findCurrentBookingsByOwnerId(ownerId);
            case PAST -> bookingsList = bookingRepository.findPastBookingsByOwnerId(ownerId);
            case FUTURE -> bookingsList = bookingRepository.findFutureBookingsByOwnerId(ownerId);
            case WAITING -> bookingsList = bookingRepository.findAllBookingsByOwnerIdAndStatus(ownerId, Status.WAITING);
            case REJECTED -> bookingsList = bookingRepository.findAllBookingsByOwnerIdAndStatus(ownerId, Status.REJECTED);
            default -> throw new NotFoundException("Статус указан неверно");
        }

        return bookingsList.stream()
                .map(BookingMapper::mapToBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .toList();
    }
}
