//package com.akm.hotelmanagement.controller;
//
//import com.akm.hotelmanagement.assembler.ReservationModelAssembler;
//import com.akm.hotelmanagement.assembler.UserModelAssembler;
//import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
//import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
//import com.akm.hotelmanagement.dto.user.UserResponseDto;
//import com.akm.hotelmanagement.service.AmenityService;
//import com.akm.hotelmanagement.service.ReservationService;
//import com.akm.hotelmanagement.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(controllers = UserController.class,
//        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {com.akm.hotelmanagement.HotelManagementApplication.class}))
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//    @Mock
//    private ReservationService reservationService;
//    @Mock
//    private AmenityService amenityService;
//    @Mock
//    private UserModelAssembler userModelAssembler;
//    @Mock
//    private ReservationModelAssembler reservationModelAssembler;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testGetUserDetails() throws Exception {
//        String username = "testUser";
//        UserResponseDto userResponseDto = new UserResponseDto(
//                UUID.randomUUID(),
//                "John Doe",
//                "john.doe@example.com",
//                "johndoe",
//                "1234567890",
//                Collections.singleton(1L)
//        );
//
//        when(userService.getUserByUsername(username)).thenReturn(userResponseDto);
//
//        mockMvc.perform(get("/users/{username}", username))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    @Test
//    public void testUpdateUserDetails() throws Exception {
//        String username = "testUser";
//        UpdateUserRequestDto dto = new UpdateUserRequestDto(
//                "John Doe",
//                "john.doe@example.com",
//                "johndoe",
//                "Password@123",
//                "1234567890"
//        );
//        UserResponseDto userResponseDto = new UserResponseDto(
//                UUID.randomUUID(),
//                "John Doe",
//                "john.doe@example.com",
//                "johndoe",
//                "1234567890",
//                Collections.singleton(1L)
//        );
//
//        when(userService.updateUser(anyString(), any(UpdateUserRequestDto.class), eq(true))).thenReturn(userResponseDto);
//
//        mockMvc.perform(put("/users/{username}", username)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"username\":\"johndoe\",\"password\":\"Password@123\",\"phone\":\"1234567890\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    @Test
//    public void testCreateUser() throws Exception {
//        CreateUserRequestDto dto = new CreateUserRequestDto(
//                "John Doe",
//                "john.doe@example.com",
//                "johndoe",
//                "Password@123",
//                "1234567890"
//        );
//        UserResponseDto userResponseDto = new UserResponseDto(
//                UUID.randomUUID(),
//                "John Doe",
//                "john.doe@example.com",
//                "johndoe",
//                "1234567890",
//                Collections.singleton(1L)
//        );
//
//        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(userResponseDto);
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"username\":\"johndoe\",\"password\":\"Password@123\",\"phone\":\"1234567890\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    // Add more tests for other methods in UserController
//}