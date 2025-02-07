package com.akm.hotelmanagement.controller;

import com.akm.hotelmanagement.assembler.AmenityModelAssembler;
import com.akm.hotelmanagement.assembler.HotelModelAssembler;
import com.akm.hotelmanagement.assembler.RoomModelAssembler;
import com.akm.hotelmanagement.controller.base.BaseController;
import com.akm.hotelmanagement.dto.amenity.AmenityResponseDto;
import com.akm.hotelmanagement.mapper.AmenityMapper;
import com.akm.hotelmanagement.service.AmenityService;
import com.akm.hotelmanagement.service.HotelService;
import com.akm.hotelmanagement.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.akm.hotelmanagement.util.Constants.*;
import static com.akm.hotelmanagement.util.TestData.AMENITIES;
import static com.akm.hotelmanagement.util.Utils.getPageable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BaseController.class)
public class BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AmenityMapper amenityMapper;

    @Mock
    private AmenityService amenityService;
    @Mock
    private AmenityModelAssembler amenityModelAssembler;
    @Mock
    private HotelService hotelService;
    @Mock
    private HotelModelAssembler hotelModelAssembler;
    @Mock
    private RoomService roomService;
    @Mock
    private RoomModelAssembler roomModelAssembler;

    @Test
    void testGetAmenities() throws Exception {

        Page<AmenityResponseDto> amenitiesPage = new PageImpl<>(AMENITIES.stream().map(amenityMapper::toResponseDto).toList());
        when(amenityService.getAllAmenities(
                getPageable(DEFAULT_PAGE_NUMBER_INT, DEFAULT_PAGE_SIZE_INT, DEFAULT_AMENITY_SORT_BY, DEFAULT_SORT_DIR), null, null
        )).thenReturn(amenitiesPage);

        MvcResult mvcResult = mockMvc.perform(get("/amenities"))
                .andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        // read the content with ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        assertThat(objectMapper.readTree(content).get("data").size()).isEqualTo(AMENITIES.size());
    }

}
