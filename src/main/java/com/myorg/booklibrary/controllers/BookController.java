package com.myorg.booklibrary.controllers;

import org.springframework.http.MediaType;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.service.BookService;
import com.myorg.booklibrary.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/book")
@Tag(name = "Book Controller", description = "Create, Edit, Retrieve and Delete books.")
public class BookController {
    BookService bookService;

    @Operation(summary = "Get book by id", description = "Return a book based on an id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of book", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @Operation(summary = "Create/Edit Book", description = "Create/Edit a book from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful saved the book", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.saveBook(book), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a Book", description = "Delete a book based on an id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful deletion of a book"),
            @ApiResponse(responseCode = "400", description = "Bad request: Book does not exist")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Retrieves books", description = "Provides a list of all books")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of courses", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class))))
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @Operation(summary = "Assign a reader to a book", description = "Assign a reader to a book with reader id and book id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful assigned a reader to a book"),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{bookId}/reader/{readerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addReaderToBook(@PathVariable Long bookId, @PathVariable Long readerId) {
        return new ResponseEntity<>(bookService.addReaderToBook(readerId, bookId), HttpStatus.OK);
    }

    @Operation(summary = "Get a book based on record id", description = "Return a book based on record id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of book based on record id", content = @Content(schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "404", description = "Book with record id doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{recordId}/record", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookOfRecord(@PathVariable Long recordId) {
        return new ResponseEntity<>(bookService.getBookOfRecord(recordId), HttpStatus.OK);
    }

    @Operation(summary = "Get a list of readers based on book id", description = "Get a list of readers based on book id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of readers based on book id", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reader.class))))
    @GetMapping(value = "all/{bookId}/reader", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reader>> getReadersOfBook(@PathVariable Long bookId) {
        return new ResponseEntity<>(bookService.getReadersOfBook(bookId), HttpStatus.OK);
    }

}
