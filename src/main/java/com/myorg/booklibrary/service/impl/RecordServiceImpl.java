package com.myorg.booklibrary.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.repository.BookRepository;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.repository.RecordRepository;

import com.myorg.booklibrary.service.RecordService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {
    private RecordRepository recordRepository;
    private ReaderRepository readerRepository;
    private BookRepository bookRepository;

    @Override
    public Record getRecord(Long recordId) {
        Optional<Record> record = recordRepository.findById(recordId);
        return unwrapRecord(record, recordId);
    }

    @Override
    public Record getRecord(Long readerId, Long bookId) {
        Optional<Record> record = recordRepository.findByReaderIdAndBookId(readerId, bookId);
        return unwrapRecord(record);
    }

    @Override
    public Record saveRecord(Record record, Long readerId, Long bookId) {
        Reader reader = ReaderServiceImpl.unwrapReader(readerRepository.findById(readerId), readerId);
        Book book = BookServiceImpl.unwrapBook(bookRepository.findById(bookId), bookId);
        record.setActivity(record.getActivity().toLowerCase());
        record.setReader(reader);
        record.setBook(book);
        record.setTimestamp(LocalDateTime.now());
        return recordRepository.save(record);
    }

    @Override
    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }

    @Override
    public void deleteRecord(Long readerId, Long bookId) {
        Optional<Record> record = recordRepository.findByReaderIdAndBookId(readerId, bookId);
        if (record.isPresent())
            recordRepository.deleteByReaderIdAndBookId(readerId, bookId);
        else
            throw new EmptyResultDataAccessException(0);
    }

    @Override
    public List<Record> getRecordsOfReader(Long readerId) {
        return recordRepository.findByReaderId(readerId);
    }

    @Override
    public List<Record> getRecordsOfBook(Long bookId) {
        return recordRepository.findByBookId(bookId);
    }

    @Override
    public List<Record> getAllRecords() {
        return (List<Record>) recordRepository.findAll();
    }

    static Record unwrapRecord(Optional<Record> entity) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(Record.class);
    }

    static Record unwrapRecord(Optional<Record> entity, Long id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, Record.class);
    }
}
