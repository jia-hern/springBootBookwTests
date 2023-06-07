package com.myorg.booklibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.myorg.booklibrary.entity.Reader;

public interface ReaderRepository extends CrudRepository<Reader, Long> {

}