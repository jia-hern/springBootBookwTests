package com.myorg.booklibrary.service;

import java.util.List;

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;

public interface ReaderService {

    Reader getReader(Long readerId);

    Reader saveReader(Reader reader);

    void deleteReader(Long readerId);

    List<Reader> getReaders();

    List<Book> getBooksOfReader(Long readerId);
}