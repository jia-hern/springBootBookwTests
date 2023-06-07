package com.myorg.booklibrary;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.repository.UserRepository;
import com.myorg.booklibrary.service.BookService;
import com.myorg.booklibrary.service.ReaderService;
import com.myorg.booklibrary.service.RecordService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Seed {

    ReaderService readerService;
    BookService bookService;
    UserRepository userRepository;
    RecordService recordService;

    public void seedData() {
        Reader[] readers = new Reader[] {
                new Reader("reader_1", LocalDate.parse("2000-10-01")),
                new Reader("reader_2", LocalDate.parse("2000-10-02")),
                new Reader("reader_3", LocalDate.parse("2000-10-03")),
                new Reader("reader_4", LocalDate.parse("2000-10-04")),
                new Reader("reader_5", LocalDate.parse("2000-10-05")),
        };
        for (Reader reader : readers) {
            readerService.saveReader(reader);
        }
        Book[] books = new Book[] {
                new Book("title_1", "description_1"),
                new Book("title_2", "description_2"),
                new Book("title_3", "description_3"),
                new Book("title_4", "description_4"),
                new Book("title_5", "description_5")
        };
        for (Book book : books) {
            bookService.saveBook(book);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User admin = new User("admin", passwordEncoder.encode("password"));
        admin.setRole("admin");
        User[] users = new User[] {
                admin,
                new User("user", passwordEncoder.encode("password")),
                new User("user2", passwordEncoder.encode("password")),
                new User("user3", passwordEncoder.encode("password")),
                new User("user4", passwordEncoder.encode("password")),
        };
        for (User user : users) {
            userRepository.save(user);
        }

        bookService.addReaderToBook(1L, 1L);
        bookService.addReaderToBook(1L, 2L);
        bookService.addReaderToBook(2L, 1L);

        recordService.saveRecord(new Record("borrow"), 1L, 1L);
        recordService.saveRecord(new Record("borrow"), 1L, 2L);
        recordService.saveRecord(new Record("borrow"), 2L, 1L);
        recordService.saveRecord(new Record("return"), 2L, 2L);
        recordService.saveRecord(new Record("return"), 3L, 2L);
        recordService.saveRecord(new Record("return"), 4L, 3L);
        recordService.saveRecord(new Record("return"), 3L, 2L);
    }
}
