package com.myorg.booklibrary.controller;

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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.repository.RecordRepository;
import com.myorg.booklibrary.service.RecordService;

@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    RecordService recordService;

    List<Record> records;

    Record record;

    @BeforeEach()
    void setUp() {
        // for delete 1, to delete the last record
        records = (List<Record>) recordRepository.findAll();
        record = records.get(records.size() - 1);
    }

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @DisplayName("Test get record by Id - no jwt")
    @Test
    public void getRecordTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/1");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test valid get record by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetRecordTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/1");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.activity").value("borrow"))
                .andExpect(jsonPath("$.reader.id").value("1"))
                .andExpect(jsonPath("$.reader.name").value("reader_1"))
                .andExpect(jsonPath("$.book.id").value("1"))
                .andExpect(jsonPath("$.book.title").value("title_1"));
    }

    @DisplayName("Test invalid get record by Id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetBookTest_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/20");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The record with id '20' does not exist in our records"));

    }

    @DisplayName("Test get record by reader id and book id - no jwt")
    @Test
    public void getRecordTestByReaderAndBookId_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/1/reader/2/book");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test valid get record by reader id and book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetRecordTestByReaderAndBookId_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/1/reader/2/book");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.activity").value("borrow"))
                .andExpect(jsonPath("$.reader.id").value("1"))
                .andExpect(jsonPath("$.reader.name").value("reader_1"))
                .andExpect(jsonPath("$.book.id").value("2"))
                .andExpect(jsonPath("$.book.title").value("title_2"));
    }

    @DisplayName("Test get record by invalid reader id and book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void getBookTestByInvalidReaderAndBookId_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/8/reader/2/book");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The record does not exist in our records"));
    }

    @DisplayName("Test get record by reader id and invalid book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void getBookTestByReaderAndInvalidBookId_withJwt_returnsNotFound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/2/reader/8/book");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("The record does not exist in our records"));
    }

    @DisplayName("Test create record - no jwt")
    @Test
    public void validRecordCreationTest_noJwt_returnsForbidden() throws Exception {
        Record newRecord = new Record("borrow");
        RequestBuilder request = MockMvcRequestBuilders.post("/record/4/reader/3/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRecord));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test create valid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void validRecordCreationTest_withJwt_returnsCreated() throws Exception {
        Record newRecord = new Record("borrow");
        RequestBuilder request = MockMvcRequestBuilders.post("/record/4/reader/3/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRecord));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("8"))
                .andExpect(jsonPath("$.activity").value("borrow"))
                .andExpect(jsonPath("$.reader.id").value("4"))
                .andExpect(jsonPath("$.reader.name").value("reader_4"))
                .andExpect(jsonPath("$.book.id").value("3"))
                .andExpect(jsonPath("$.book.title").value("title_3"));
    }

    @DisplayName("Test create invalid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidRecordCreationTest_withJwt_returnsBadReq() throws Exception {
        Record newRecord = new Record("not_borrow");
        RequestBuilder request = MockMvcRequestBuilders.post("/record/4/reader/3/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRecord));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Activity should be borrow or return"));
    }

    @DisplayName("Test update record - no jwt")
    @Test
    public void recordUpdateTest_noJwt_returnsForbidden() throws Exception {
        Record updateRecord = new Record("return");
        updateRecord.setId(Long.valueOf(4));
        RequestBuilder request = MockMvcRequestBuilders.post("/record/2/reader/2/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRecord));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test update valid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void validRecordUpdateTest_withJwt_returnsCreated() throws Exception {
        Record updateRecord = new Record("return");
        updateRecord.setId(Long.valueOf(4));
        RequestBuilder request = MockMvcRequestBuilders.post("/record/2/reader/2/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRecord));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.activity").value("return"))
                .andExpect(jsonPath("$.reader.id").value("2"))
                .andExpect(jsonPath("$.reader.name").value("reader_2"))
                .andExpect(jsonPath("$.book.id").value("2"))
                .andExpect(jsonPath("$.book.title").value("title_2"));
    }

    @DisplayName("Test update invalid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidRecordUpdateTest_withJwt_returnsBadReq() throws Exception {
        Record updateRecord = new Record("not_borrow");
        updateRecord.setId(Long.valueOf(4));
        RequestBuilder request = MockMvcRequestBuilders.post("/record/3/reader/3/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRecord));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Activity should be borrow or return"));
    }

    @DisplayName("Test delete record - no jwt")
    @Test
    public void validRecordDeletionTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/" + record.getId());
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete valid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void validRecordDeletionTest_withJwt_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/" + record.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @DisplayName("Test delete invalid record - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidRecordDeletionTest_withJwt_returnsBadReq() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/20");
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }

    @DisplayName("Test delete record by reader id and book id - no jwt")
    @Test
    public void validRecordDeletionByReaderAndBookIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/4/reader/3/book");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete valid record by reader id and book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validRecordDeletionByReaderAndBookIdTest_withJwt_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/4/reader/3/book");
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @DisplayName("Test delete record by invalid reader id and book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void recordDeletionByInvalidReaderAndBookIdTest_withJwt_returnsBadReq() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/8/reader/3/book");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }

    @DisplayName("Test delete record by reader id and invalid book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void recordDeletionByReaderAndInvalidBookIdTest_withJwt_returnsBadReq() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/record/8/reader/3/book");

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }

    @DisplayName("Test get all records - no jwt")
    @Test
    public void getAllRecordsTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all records - with jwt")
    @WithMockUser("spring")
    @Test
    public void getAllRecordsTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].activity").value("borrow"))
                .andExpect(jsonPath("$[0].reader.id").value("1"))
                .andExpect(jsonPath("$[0].reader.name").value("reader_1"))
                .andExpect(jsonPath("$[0].book.id").value("1"))
                .andExpect(jsonPath("$[0].book.title").value("title_1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].activity").value("borrow"))
                .andExpect(jsonPath("$[1].reader.id").value("1"))
                .andExpect(jsonPath("$[1].reader.name").value("reader_1"))
                .andExpect(jsonPath("$[1].book.id").value("2"))
                .andExpect(jsonPath("$[1].book.title").value("title_2"))
                .andExpect(jsonPath("$.size()").value(records.size()));
    }

    @DisplayName("Get a list of records based on reader id - no jwt")
    @Test
    public void validGetRecordsByReaderIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/1/reader");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Get a list of records based on reader id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetRecordsByReaderIdTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/1/reader");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].activity").value("borrow"))
                .andExpect(jsonPath("$[0].reader.id").value("1"))
                .andExpect(jsonPath("$[0].reader.name").value("reader_1"))
                .andExpect(jsonPath("$[0].book.id").value("1"))
                .andExpect(jsonPath("$[0].book.title").value("title_1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].activity").value("borrow"))
                .andExpect(jsonPath("$[1].reader.id").value("1"))
                .andExpect(jsonPath("$[1].reader.name").value("reader_1"))
                .andExpect(jsonPath("$[1].book.id").value("2"))
                .andExpect(jsonPath("$[1].book.title").value("title_2"))
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Get list of records based on invalid reader id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetRecordsByReaderIdTest_withJwt_returnsOk_andEmpty() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/8/reader");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
    }

    @DisplayName("Get a list of records based on book id - no jwt")
    @Test
    public void validGetRecordsByBookIdTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/1/book");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Get a list of records based on book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void validGetRecordsByBookIdTest_withJwt_returnsOk() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/1/book");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].activity").value("borrow"))
                .andExpect(jsonPath("$[0].reader.id").value("1"))
                .andExpect(jsonPath("$[0].reader.name").value("reader_1"))
                .andExpect(jsonPath("$[0].book.id").value("1"))
                .andExpect(jsonPath("$[0].book.title").value("title_1"))
                .andExpect(jsonPath("$[1].id").value("3"))
                .andExpect(jsonPath("$[1].activity").value("borrow"))
                .andExpect(jsonPath("$[1].reader.id").value("2"))
                .andExpect(jsonPath("$[1].reader.name").value("reader_2"))
                .andExpect(jsonPath("$[1].book.id").value("1"))
                .andExpect(jsonPath("$[1].book.title").value("title_1"))
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Get list of records based on invalid book id - with jwt")
    @WithMockUser("spring")
    @Test
    public void invalidGetRecordsByBookIdTest_withJwt_returnsOk_andEmpty() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/record/all/8/book");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
    }
}
