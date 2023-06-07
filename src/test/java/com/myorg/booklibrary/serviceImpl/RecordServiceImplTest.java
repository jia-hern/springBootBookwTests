package com.myorg.booklibrary.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.myorg.booklibrary.repository.BookRepository;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.repository.RecordRepository;
import com.myorg.booklibrary.service.impl.RecordServiceImpl;
import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.entity.Reader;

@ExtendWith(MockitoExtension.class)
public class RecordServiceImplTest {
    @Mock
    RecordRepository recordRepository;
    @Mock
    ReaderRepository readerRepository;
    @Mock
    BookRepository bookRepository;
    @InjectMocks
    RecordServiceImpl recordServiceImpl;

    @DisplayName("Test Find all")
    @Test
    void findAll() {
        List<Record> records = Arrays.asList(
                new Record("borrow"),
                new Record("return"));

        when(recordRepository.findAll()).thenReturn(records);
        List<Record> foundRecords = recordServiceImpl.getAllRecords();

        verify(recordRepository, times(1)).findAll();
        assertThat(foundRecords).hasSize(2);
        assertEquals("borrow", foundRecords.get(0).getActivity());
        assertEquals("return", foundRecords.get(1).getActivity());
    }

    @DisplayName("Test Find By Id")
    @Test
    void findById() {
        Record record = new Record("borrow");

        when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

        Record foundRecord = recordServiceImpl.getRecord(1L);
        verify(recordRepository, times(1)).findById(1L);
        assertThat(foundRecord).isNotNull();
        assertEquals(record, foundRecord);
    }

    @DisplayName("Test Find By invalid Id")
    @Test
    void findByInvalidId() {

        when(recordRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            recordServiceImpl.getRecord(999L);
        });

        verify(recordRepository, times(1)).findById(999L);
        assertThat(recordRepository.findById(999L)).isEmpty();
    }

    @DisplayName("Test Find By reader and book Id")
    @Test
    void findByReaderAndBookId() {
        Record record = new Record("borrow");

        when(recordRepository.findByReaderIdAndBookId(1L, 1L)).thenReturn(Optional.of(record));

        Record foundRecord = recordServiceImpl.getRecord(1L, 1L);
        verify(recordRepository, times(1)).findByReaderIdAndBookId(1L, 1L);
        assertThat(foundRecord).isNotNull();
        assertEquals(record, foundRecord);
    }

    @DisplayName("Test Find By Invalid reader and book Id")
    @Test
    void findByInvalidReaderAndBookId() {

        when(recordRepository.findByReaderIdAndBookId(999L,999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            recordServiceImpl.getRecord(999L, 999L);
        });

        verify(recordRepository, times(1)).findByReaderIdAndBookId(999L,999L);
        assertThat(recordRepository.findByReaderIdAndBookId(999L,999L)).isEmpty();
    }

    @DisplayName("Test Save")
    @Test
    void save() {
        Record record = new Record("return");
        Book book = new Book();
        Reader reader = new Reader();

        when(recordRepository.save(any(Record.class))).thenReturn(record);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        Record savedRecord = recordServiceImpl.saveRecord(record, 1L, 1L);

        verify(recordRepository, times(1)).save(record);
        verify(bookRepository, times(1)).findById(1L);
        verify(readerRepository, times(1)).findById(1L);
        assertThat(savedRecord).isNotNull();
    }

    @DisplayName("Test Update")
    @Test
    void update() {
        Record record = new Record("return");
        Book book = new Book();
        Reader reader = new Reader();

        when(recordRepository.save(any(Record.class))).thenReturn(record);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        record.setActivity("borrow");
        Record savedRecord = recordServiceImpl.saveRecord(record, 1L, 1L);

        verify(recordRepository, times(1)).save(record);
        verify(bookRepository, times(1)).findById(1L);
        verify(readerRepository, times(1)).findById(1L);
        assertThat(savedRecord).isNotNull();
        assertEquals("borrow", savedRecord.getActivity());
    }

    @DisplayName("Test Delete By Id")
    @Test
    void deleteById() {
        recordServiceImpl.deleteRecord(1L);
        verify(recordRepository).deleteById(1L);
    }

    @DisplayName("Test Delete By reader and book Id")
    @Test
    void deleteByReaderAndBookId() {
        Record record = new Record("return");
        when(recordRepository.findByReaderIdAndBookId(1L, 1L)).thenReturn(Optional.of(record));
        recordServiceImpl.deleteRecord(1L, 1L);
        verify(recordRepository).deleteByReaderIdAndBookId(1L, 1L);
    }

    @DisplayName("Test Delete By invalid reader and book Id")
    @Test
    void deleteByInvalidReaderAndBookId() {
        when(recordRepository.findByReaderIdAndBookId(999L, 999L)).thenReturn(Optional.empty());
        
        assertThrows(EmptyResultDataAccessException.class, () -> {
            recordServiceImpl.deleteRecord(999L, 999L);
        });

        verify(recordRepository, times(1)).findByReaderIdAndBookId(999L, 999L);
        assertThat(recordRepository.findByReaderIdAndBookId(999L, 999L)).isEmpty();
    }

    @DisplayName("Test Get records of book")
    @Test
    void getRecordsOfBook() {
        Record record = new Record();
        when(recordRepository.findByBookId(1L)).thenReturn(Arrays.asList(record));

        recordServiceImpl.getRecordsOfBook(1L);
        verify(recordRepository, times(1)).findByBookId(1L);
    }

    @DisplayName("Test Get records of reader")
    @Test
    void getRecordsOfReader() {
        Record record = new Record();
        when(recordRepository.findByReaderId(1L)).thenReturn(Arrays.asList(record));

        recordServiceImpl.getRecordsOfReader(1L);
        verify(recordRepository, times(1)).findByReaderId(1L);
    }
}
