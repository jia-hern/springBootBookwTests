package com.myorg.booklibrary.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myorg.booklibrary.exception.ErrorResponse;
import com.myorg.booklibrary.entity.Book;
import com.myorg.booklibrary.entity.Reader;
import com.myorg.booklibrary.service.ReaderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reader")
@Tag(name = "Reader Controller", description = "Create, Edit, Retrieve and Delete readers")
public class ReaderController {

    ReaderService readerService;

    @Operation(summary = "Get a reader by id", description = "Return a reader based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of reader", content = @Content(schema = @Schema(implementation = Reader.class))),
            @ApiResponse(responseCode = "404", description = "Reader doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reader> getReader(@PathVariable Long id) {
        return new ResponseEntity<>(readerService.getReader(id), HttpStatus.OK);
    }

    @Operation(summary = "Create/Edit Reader", description = "Create/Edit a reader from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation of reader", content = @Content(schema = @Schema(implementation = Reader.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reader> saveReader(@Valid @RequestBody Reader reader) {
        return new ResponseEntity<>(readerService.saveReader(reader), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a reader", description = "Delete a reader based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deletion of reader"),
            @ApiResponse(responseCode = "400", description = "Bad request: Reader doesn't exist")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all readers", description = "Provides a list of all readers")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all readers", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reader.class))))
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reader>> getReaders() {
        return new ResponseEntity<>(readerService.getReaders(), HttpStatus.OK);
    }

    @Operation(summary = "Get all books of a reader", description = "Provides a list of all books by reader id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of allbooks by reader id", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reader.class))))
    @GetMapping(value = "/all/{bookId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getBooksOfReader(@PathVariable Long bookId) {
        return new ResponseEntity<>(readerService.getBooksOfReader(bookId), HttpStatus.OK);
    }
}
