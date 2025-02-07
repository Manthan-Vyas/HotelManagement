//package com.akm.hotelmanagement.controller;
//
//import com.akm.hotelmanagement.dto.user.CreateUserRequestDto;
//import com.akm.hotelmanagement.dto.user.UpdateUserRequestDto;
//import com.akm.hotelmanagement.entity.User;
//import com.akm.hotelmanagement.repository.AmenityRepository;
//import com.akm.hotelmanagement.repository.HotelRepository;
//import com.akm.hotelmanagement.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional // Important for tests modifying the database
//@WithMockUser(username = "admin", roles = {"ADMIN"})
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private HotelRepository hotelRepository;
//
//    @Autowired
//    private AmenityRepository amenityRepository;
//
//    @Test
//    void testGetAdminHome() throws Exception {
//        mockMvc.perform(get("/admin"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        CreateUserRequestDto dto = new CreateUserRequestDto(
//                "Test",
//                "test@example.com",
//                "testuser",
//                "P@ssw0rd",
//                "0123456789"
//        );
//
//        mockMvc.perform(post("/admin/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.username").value("testuser"));
//
//        // Verify in the database
//        User user = userRepository.findByUsername("testuser").orElse(null);
//        // assertNotNull(user); // Use Assertions.assertNotNull if needed
//    }
//
//    @Test
//    void testCreateUser_DuplicateUsername() throws Exception {
//        // Create a user in the repository to simulate a duplicate username
//        User existingUser = new User();
//        existingUser.setUsername("testuser");
//        existingUser.setEmail("test@example.com");
//        existingUser.setPassword("P@ssw0rd");
//        existingUser.setName("Test");
//        existingUser.setPhone("1231312233");
//        userRepository.save(existingUser);
//
//        // Create a DTO with the same username
//        CreateUserRequestDto dto = new CreateUserRequestDto(
//                "Test",
//                "test@example.com",
//                "testuser",
//                "P@ssw0rd",
//                "0123456789"
//        );
//
//        mockMvc.perform(post("/admin/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isConflict()); // Expecting 409 Conflict
//    }
//
//    @Test
//    void testGetUserDetails() throws Exception {
//        mockMvc.perform(get("/admin/users/testuser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.username").value("testuser"));
//    }
//
//    @Test
//    void testGetHotelDetails() throws Exception {
//        mockMvc.perform(get("/admin/hotels/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(1));
//    }
//
//    @Test
//    void testGetRoomDetails() throws Exception {
//        mockMvc.perform(get("/admin/rooms/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(1));
//    }
//
//    @Test
//    void testGetAmenityDetails() throws Exception {
//        mockMvc.perform(get("/admin/amenities/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(1));
//    }
//
//    @Test
//    void testGetReservationDetails() throws Exception {
//        mockMvc.perform(get("/admin/reservations/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id").value(1));
//    }
//
//    @Test
//    void testUpdateUserDetails() throws Exception {
//        UpdateUserRequestDto dto = new UpdateUserRequestDto(
//                "UpdatedName",
//                "updated@example.com",
//                "testuser",
//                "UpdatedPassword",
//                "0123456789"
//        );
//
//        mockMvc.perform(put("/admin/users/testuser")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.username").value("testuser"))
//                .andExpect(jsonPath("$.data.name").value("UpdatedName"));
//    }
//
//    @Test
//    void testUpdateHotelDetails() throws Exception {
//        // Similar to testUpdateUserDetails, create a DTO and perform a PUT request
//    }
//
//    @Test
//    void testUpdateAmenityDetails() throws Exception {
//        // Similar to testUpdateUserDetails, create a DTO and perform a PUT request
//    }
//
//    @Test
//    void testChangeUserEnabled() throws Exception {
//        mockMvc.perform(patch("/admin/users/testuser/enabled")
//                        .param("enabled", "true"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.enabled").value(true));
//    }
//
//    @Test
//    void testDeleteUser() throws Exception {
//        mockMvc.perform(delete("/admin/users/testuser"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteHotel() throws Exception {
//        mockMvc.perform(delete("/admin/hotels/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteAmenity() throws Exception {
//        mockMvc.perform(delete("/admin/amenities/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetAmenityHotels() throws Exception {
//        mockMvc.perform(get("/admin/amenities/1/hotels"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content").isArray());
//    }
//
//}