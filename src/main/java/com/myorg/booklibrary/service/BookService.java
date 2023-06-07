package com.myorg.booklibrary.service;

import java.util.List;

import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.entity.Book;

public interface BookService {
    Book getBook(Long bookId);

    Book saveBook(Book book);

    void deleteBook(Long bookId);

    Book addReaderToBook(Long readerId, Long bookId);

    List<Book> getBooks();

    List<Reader> getReadersOfBook(Long bookId);

    Book getBookOfRecord(Long recordId);
}
