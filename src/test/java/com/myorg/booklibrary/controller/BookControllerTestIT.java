package com.myorg.booklibrary.controller;

import java.util.List;

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

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.repository.BookRepository;
import com.myorg.booklibrary.service.BookService;
import com.myorg.booklibrary.service.RecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    RecordService recordService;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @DisplayName("Test get book by Id - no jwt")
    @Test
    public void getBookTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/1");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test valid get book by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetBookTest_witJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/1");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("title_1")) // dot notation
                .andExpect(jsonPath("$['description']").value("description_1")); // bracket-notation
    }

    @DisplayName("Test invalid get book by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetBookTest_witJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/8");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Test create book - no jwt")
    @Test
    public void validBookCreationTest_noJwt_returnsForbidden() throws Exception {
        Book newBook = new Book("title_6", "description_6");
        RequestBuilder request = MockMvcRequestBuilders.post("/book", newBook);
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test create valid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void validBookCreationTest_witJwt_returnsCreated() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Book("title_6", "description_6")));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("title_6"))
                .andExpect(jsonPath("$.description").value("description_6"));
    }

    @DisplayName("Test create invalid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidBookCreationTest_witJwt_returnsBadReq() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Book("title_1", "   ")));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Description cannot be blank"));
    }

    @DisplayName("Test update book - no jwt")
    @Test
    public void invalidBookUpdateTest_witJwt_returnsForbidden() throws Exception {
        Book updateBook = new Book("title_1", "description_1_new");
        updateBook.setId(Long.valueOf(1));
        RequestBuilder request = MockMvcRequestBuilders.post("/book", updateBook);
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test update valid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void validBookUpdateTest_witJwt_returnsCreated() throws Exception {
        Book updateBook = new Book("title_1", "description_1_new");
        updateBook.setId(Long.valueOf(1));
        RequestBuilder request = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBook));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("title_1"))
                .andExpect(jsonPath("$.description").value("description_1_new"));
    }

    @DisplayName("Test update invalid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidBookUpdateTest_witJwt_returnsBadReq() throws Exception {
        Book updateBook = new Book("      ", "description_1");
        updateBook.setId(Long.valueOf(1));
        RequestBuilder request = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBook));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Title cannot be blank"));
    }

    @DisplayName("Test delete book - no jwt")
    @Test
    public void validBookDeletionTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/book/5");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete valid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void validBookDeletionTest_witJwt_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/book/5");
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @DisplayName("Test delete invalid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidBookDeletionTest_witJwt_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/book/8");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }

    @DisplayName("Test get all books - no jwt")
    @Test
    public void getAllBooksTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/all");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all books - with jwt")
    @WithMockUser("spring")
    @Test
    public void getAllBooksTest_witJwt_returnsOk() throws Exception {
        List<Book> books = (List<Book>) bookRepository.findAll();

        RequestBuilder request = MockMvcRequestBuilders.get("/book/all");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("title_1"))
                .andExpect(jsonPath("$[0].description").value("description_1"))
                .andExpect(jsonPath("$.size()").value(books.size()));
    }

    @DisplayName("Test assign a reader to a book - no jwt")
    @Test
    public void validAssignReaderToBookTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/book/1/reader/1");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test valid assign a reader to a book - with jwt")
    @WithMockUser("spring")
    @Test
    public void validAssignReaderToBookTest_witJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/book/3/reader/5");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value("title_3"))
                .andExpect(jsonPath("$.description").value("description_3"));
    }

    @DisplayName("Test assign invalid reader to a book - with jwt")
    @WithMockUser("spring")
    @Test
    public void assignInvalidReaderToBookTest_witJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/book/1/reader/8");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The reader with id '8' does not exist in our records"));
    }

    @DisplayName("Test assign a reader to invalid book - with jwt")
    @WithMockUser("spring")
    @Test
    public void assignIReaderTonvalidBookTest_witJwt_returnsisNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put("/book/8/reader/1");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The book with id '8' does not exist in our records"));
    }

    @DisplayName("Test get a book based on record id - no jwt")
    @Test
    public void validGetBookByRecordIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/1/record");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get a valid book based on record id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetBookByRecordIdTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/1/record");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title_1"))
                .andExpect(jsonPath("$.description").value("description_1"));
    }

    @DisplayName("Test get a book based on invalid record id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetBookByRecordIdTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/8/record");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The record does not exist in our records"));
    }

    @DisplayName("Get a list of readers based on book id - no jwt")
    @Test
    public void validGetReadersByBookIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/all/1/reader");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Get a list of readers based on book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetReadersByBookIdTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/all/1/reader");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("reader_1"))
                .andExpect(jsonPath("$[0].birthDate").value("2000-10-01"))
                .andExpect(jsonPath("$[1].name").value("reader_2"))
                .andExpect(jsonPath("$[1].birthDate").value("2000-10-02"))
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Get a list of readers based on invalid book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetReadersByBookIdTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/book/all/8/reader");
        mockMvc.perform(request).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The book with id '8' does not exist in our records"));
    }

}
