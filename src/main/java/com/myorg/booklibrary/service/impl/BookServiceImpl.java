package com.myorg.booklibrary.service.impl;

import java.util.List;
import java.util.Optional;

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.exception.EntityNotFoundException;

import com.myorg.booklibrary.repository.BookRepository;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.repository.RecordRepository;
import com.myorg.booklibrary.service.BookService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private ReaderRepository readerRepository;
    private RecordRepository recordRepository;

    @Override
    public Book getBook(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return unwrapBook(book, bookId);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Book addReaderToBook(Long readerId, Long bookId) {
        Book book = getBook(bookId);
        Optional<Reader> reader = readerRepository.findById(readerId);
        Reader unwrappedReader = ReaderServiceImpl.unwrapReader(reader, readerId);
        book.getReaders().add(unwrappedReader);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public Book getBookOfRecord(Long recordId) {
        Optional<Record> record = recordRepository.findById(recordId);
        Record unwrappedRecord = RecordServiceImpl.unwrapRecord(record);
        return unwrappedRecord.getBook();
    }

    @Override
    public List<Reader> getReadersOfBook(Long bookId) {
        Book book = getBook(bookId);
        return book.getReaders();
    }

    static Book unwrapBook(Optional<Book> entity, Long id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, Book.class);
    }

    static Book unwrapBook(Optional<Book> entity) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(Book.class);
    }

}
