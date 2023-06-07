package com.myorg.booklibrary.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.myorg.booklibrary.repository.BookRepository;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.repository.RecordRepository;
import com.myorg.booklibrary.service.impl.BookServiceImpl;
import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.entity.Reader;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    ReaderRepository readerRepository;
    @Mock
    RecordRepository recordRepository;
    @InjectMocks
    BookServiceImpl bookServiceImpl;

    @DisplayName("Test Find all")
    @Test
    void findAll() {
        List<Book> books = Arrays.asList(
                new Book("title_1", "description_1"),
                new Book("title_2", "description_2"));

        when(bookRepository.findAll()).thenReturn(books);
        List<Book> foundBooks = bookServiceImpl.getBooks();

        verify(bookRepository, times(1)).findAll();
        assertThat(foundBooks).hasSize(2);
        assertEquals("title_1", foundBooks.get(0).getTitle());
        assertEquals("description_2", foundBooks.get(1).getDescription());
    }

    @DisplayName("Test Find By Id")
    @Test
    void findById() {
        Book book = new Book("title_1", "description_1");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookServiceImpl.getBook(1L);
        verify(bookRepository, times(1)).findById(1L);
        assertThat(foundBook).isNotNull();
        assertEquals(book, foundBook);
    }

    @DisplayName("Test Find By invalid Id")
    @Test
    void findByInvalidId() {

        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            bookServiceImpl.getBook(999L);
        });

        verify(bookRepository, times(1)).findById(999L);
        assertThat(bookRepository.findById(999L)).isEmpty();
    }

    @DisplayName("Test Save")
    @Test
    void save() {
        Book book = new Book();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookServiceImpl.saveBook(new Book());

        verify(bookRepository, times(1)).save(any(Book.class));

        assertThat(savedBook).isNotNull();
    }

    @DisplayName("Test Update")
    @Test
    void update() {
        Book book = new Book("title_1", "description_1");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        book.setTitle("title_2");
        Book savedBook = bookServiceImpl.saveBook(book);

        verify(bookRepository, times(1)).save(book);

        assertThat(savedBook).isNotNull();
        assertEquals("title_2", savedBook.getTitle());
    }

    @DisplayName("Test Delete By Id")
    @Test
    void deleteById() {
        bookServiceImpl.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @DisplayName("Test Add reader to book")
    @Test
    void addReaderToBook() {
        Book book = new Book();
        Reader reader = new Reader();
        List<Reader> list = new ArrayList<Reader>();
        book.setReaders(list);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1L)).thenReturn(Optional.of(reader));
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookServiceImpl.addReaderToBook(1L, 1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(readerRepository, times(1)).findById(1L);
        assertThat(savedBook).isNotNull();
    }

    @DisplayName("Test Get book of record")
    @Test
    void getBookOfRecord() {
        Record record = new Record();
        when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

        bookServiceImpl.getBookOfRecord(1L);
        verify(recordRepository, times(1)).findById(1L);
    }

    @DisplayName("Test Get readers of book")
    @Test
    void getReadersOfBook() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookServiceImpl.getReadersOfBook(1L);
        verify(bookRepository, times(1)).findById(1L);
    }

}
