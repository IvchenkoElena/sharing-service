package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    ItemRequestServiceImpl requestService;

    @Test
    void createItemRequest_positiveCase() {
        long requestorId = 7L;
        User requestor = new User(requestorId, "requestor name", "requestor email");
        UserDto requestorDto = new UserDto(requestorId, "requestor name", "requestor email");
        long itemRequestId = 0L;
        String description = "description";
        NewItemRequestRequest request = new NewItemRequestRequest(description);
        ItemRequest itemRequestToSave = new ItemRequest(itemRequestId, description, requestor, LocalDate.now());
        ItemRequestDto itemRequestDto = new ItemRequestDto(itemRequestId, description, requestorDto, LocalDate.now());
        when(userRepository.findById(requestorId)).thenReturn(Optional.of(requestor));
        when(requestRepository.save(itemRequestToSave)).thenReturn(itemRequestToSave);

        ItemRequestDto actualDto = requestService.createItemRequest(requestorId, request);
        //тест не работал из-за секунд во времени, не смогла придумать, как по-другому исправить, кроме как заменить на LocalDate

        assertEquals(itemRequestDto, actualDto);
        verify(requestRepository).save(itemRequestToSave);
    }

    @Test
    void createItemRequest_whenRequestorNotFound() {
        long requestorId = 0L;
        NewItemRequestRequest request = new NewItemRequestRequest("description");
        User requestor = new User(requestorId, "requestor name", "requestor email");
        when(userRepository.findById(requestorId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> requestService.createItemRequest(requestorId, request));

        assertEquals("Пользователь с ID " + requestorId + " не найден", thrown.getMessage());
    }
}