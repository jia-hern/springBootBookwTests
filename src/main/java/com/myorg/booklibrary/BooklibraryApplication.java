package com.myorg.booklibrary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.myorg.booklibrary.repository.UserRepository;
import com.myorg.booklibrary.service.BookService;
import com.myorg.booklibrary.service.ReaderService;
import com.myorg.booklibrary.service.RecordService;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class BooklibraryApplication implements CommandLineRunner {
	ReaderService readerService;
	BookService bookService;
	UserRepository userRepository;
	RecordService recordService;

	public static void main(String[] args) {
		SpringApplication.run(BooklibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Seed seed = new Seed(readerService, bookService, userRepository, recordService);
		seed.seedData();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
