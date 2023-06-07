package com.myorg.booklibrary.repository;

import org.springframework.data.repository.CrudRepository;

import com.myorg.booklibrary.entity.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
