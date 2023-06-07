package com.myorg.booklibrary.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.service.impl.ReaderServiceImpl;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.entity.Reader;

@ExtendWith(MockitoExtension.class)
public class ReaderServiceImplTest {
    @Mock
    ReaderRepository readerRepository;
    @InjectMocks
    ReaderServiceImpl readerServiceImpl;

    @DisplayName("Test Find all")
    @Test
    void findAll() {
        List<Reader> readers = Arrays.asList(
                new Reader("reader_1", LocalDate.parse("2000-10-01")),
                new Reader("title_2", LocalDate.parse("2000-10-02")));

        when(readerRepository.findAll()).thenReturn(readers);
        List<Reader> foundReaders = readerServiceImpl.getReaders();

        verify(readerRepository, times(1)).findAll();
        assertThat(foundReaders).hasSize(2);
        assertEquals("reader_1", foundReaders.get(0).getName());
        assertEquals(LocalDate.parse("2000-10-02"), foundReaders.get(1).getBirthDate());
    }

    @DisplayName("Test Find By Id")
    @Test
    void findById() {
        Reader reader = new Reader("reader_1", LocalDate.parse("2000-10-01"));

        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        Reader foundReader = readerServiceImpl.getReader(1L);
        verify(readerRepository, times(1)).findById(1L);
        assertThat(foundReader).isNotNull();
        assertEquals(reader, foundReader);
    }

    @DisplayName("Test Find By invalid Id")
    @Test
    void findByInvalidId() {

        when(readerRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            readerServiceImpl.getReader(999L);
        });

        verify(readerRepository, times(1)).findById(999L);
        assertThat(readerRepository.findById(999L)).isEmpty();
    }

    @DisplayName("Test Save")
    @Test
    void save() {
        Reader reader = new Reader();

        when(readerRepository.save(any(Reader.class))).thenReturn(reader);

        Reader savedReader = readerServiceImpl.saveReader(new Reader());

        verify(readerRepository, times(1)).save(any(Reader.class));

        assertThat(savedReader).isNotNull();
    }

    @DisplayName("Test Update")
    @Test
    void update() {
        Reader reader = new Reader("reader_1", LocalDate.parse("2000-10-01"));

        when(readerRepository.save(any(Reader.class))).thenReturn(reader);

        reader.setName("reader_2");
        Reader savedReader = readerServiceImpl.saveReader(reader);

        verify(readerRepository, times(1)).save(reader);

        assertThat(savedReader).isNotNull();
        assertEquals("reader_2", savedReader.getName());
    }

    @DisplayName("Test Delete By Id")
    @Test
    void deleteById() {
        readerServiceImpl.deleteReader(1L);
        verify(readerRepository).deleteById(1L);
    }

    @DisplayName("Test Get books of reader")
    @Test
    void getBooksOfReader() {
        Reader reader = new Reader();
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));

        readerServiceImpl.getBooksOfReader(1L);
        verify(readerRepository, times(1)).findById(1L);
    }
}
