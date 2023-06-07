package com.myorg.booklibrary.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.repository.ReaderRepository;
import com.myorg.booklibrary.service.ReaderService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private ReaderRepository readerRepository;

    @Override
    public Reader getReader(Long readerId) {
        Optional<Reader> reader = readerRepository.findById(readerId);
        return unwrapReader(reader, readerId);
    }

    @Override
    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    public void deleteReader(Long readerId) {
        readerRepository.deleteById(readerId);
    }

    @Override
    public List<Reader> getReaders() {
        return (List<Reader>) readerRepository.findAll();
    }

    @Override
    public List<Book> getBooksOfReader(Long readerId) {
        Reader reader = getReader(readerId);
        return reader.getBooks();
    }

    static Reader unwrapReader(Optional<Reader> entity) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(Reader.class);
    }

    static Reader unwrapReader(Optional<Reader> entity, Long id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, Reader.class);
    }

}