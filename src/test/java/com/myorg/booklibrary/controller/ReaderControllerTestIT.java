package com.myorg.booklibrary.controller;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
public class ReaderControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    BookService bookService;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @DisplayName("Test get reader by Id - no jwt")
    @Test
    public void getReaderTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/1");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test valid get reader by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetReaderTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/1");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("reader_1"))
                .andExpect(jsonPath("$.birthDate").value("2000-10-01"));
    }

    @DisplayName("Test invalid get reader by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetReaderTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/8");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Test create reader - no jwt")
    @Test
    public void validReaderCreationTest_noJwt_returnsForbidden() throws Exception {
        Reader newReader = new Reader("reader_6", LocalDate.parse("2000-10-06"));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReader));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test create valid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void validReaderCreationTest_withJwt_returnsCreated() throws Exception {
        Reader newReader = new Reader("reader_6", LocalDate.parse("2000-10-06"));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReader));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("reader_6"))
                .andExpect(jsonPath("$.birthDate").value("2000-10-06"));
    }

    @DisplayName("Test create invalid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidReaderCreationTest_withJwt_returnsBadRequest() throws Exception {
        Reader newReader = new Reader("    ", LocalDate.parse("2000-10-06"));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newReader));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Name cannot be blank"));
    }

    @DisplayName("Test update reader - no jwt")
    @Test
    public void invalidReaderUpdateTest_withJwt_returnsForbidden() throws Exception {
        Reader updateReader = new Reader("reader_5_new", LocalDate.parse("2022-12-12"));
        updateReader.setId(Long.valueOf(5));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReader));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test update valid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void validReaderUpdateTest_withJwt_returnsCreated() throws Exception {
        Reader updateReader = new Reader("reader_5_new", LocalDate.parse("2022-12-12"));
        updateReader.setId(Long.valueOf(5));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReader));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.name").value("reader_5_new"))
                .andExpect(jsonPath("$.birthDate").value("2022-12-12"));
    }

    @DisplayName("Test update invalid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidNameUpdateTest_withJwt_returnsBadReq() throws Exception {
        Reader updateReader = new Reader("     ", LocalDate.parse("2022-12-12"));
        updateReader.setId(Long.valueOf(5));
        RequestBuilder request = MockMvcRequestBuilders.post("/reader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReader));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Name cannot be blank"));
    }

    @DisplayName("Test delete reader - no jwt")
    @Test
    public void validReaderDeletionTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/reader/3");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete valid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void validReaderDeletionTest_withJwt_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/reader/3");
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @DisplayName("Test delete invalid reader - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidReaderDeletionTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/reader/8");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }

    @DisplayName("Test get all readers - no jwt")
    @Test
    public void getAllReadersTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/all");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all readers - with jwt")
    @WithMockUser("spring")
    @Test
    public void getAllReadersTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/all");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("reader_1"))
                .andExpect(jsonPath("$[0].birthDate").value("2000-10-01"))
                .andExpect(jsonPath("$.size()").value(5));
    }

    // stop here
    @DisplayName("Test get all books based on reader id - no jwt")
    @Test
    public void validGetAllBooksByReaderIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/all/1/book");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all books based on reader id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetAllBooksByReaderIdTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/all/1/book");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title_2"))
                .andExpect(jsonPath("$[0].description").value("description_2"));
    }

    @DisplayName("Test get all books based on invalid record id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetAllBooksByReaderIdTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/reader/all/8/book");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The reader with id '8' does not exist in our records"));
    }
}
