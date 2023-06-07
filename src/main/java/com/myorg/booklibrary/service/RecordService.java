package com.myorg.booklibrary.service;

import java.util.List;

import com.myorg.booklibrary.entity.Record;

public interface RecordService {
    Record getRecord(Long recordId);

    Record getRecord(Long readerId, Long bookId);

    Record saveRecord(Record record, Long readerId, Long bookId);

    void deleteRecord(Long recordId);

    void deleteRecord(Long readerId, Long bookId);

    List<Record> getRecordsOfReader(Long readerId);

    List<Record> getRecordsOfBook(Long bookId);

    List<Record> getAllRecords();
}
