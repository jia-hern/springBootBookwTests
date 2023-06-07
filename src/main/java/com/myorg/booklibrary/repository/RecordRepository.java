package com.myorg.booklibrary.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.myorg.booklibrary.entity.Record;

public interface RecordRepository extends CrudRepository<Record, Long> {

    Optional<Record> findByReaderIdAndBookId(Long readerId, Long bookId);

    List<Record> findByReaderId(Long readerId);

    List<Record> findByBookId(Long bookId);

    @Transactional
    void deleteByReaderIdAndBookId(Long readerId, Long bookId);
}
