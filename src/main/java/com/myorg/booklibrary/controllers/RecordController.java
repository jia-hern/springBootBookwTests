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
import com.myorg.booklibrary.entity.Record;
import com.myorg.booklibrary.service.RecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/record")
@Tag(name = "Record Controller", description = "Create, Edit, Retrieve and Delete records")
public class RecordController {
    RecordService recordService;

    @Operation(summary = "Get a record by id", description = "Return a record based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of record", content = @Content(schema = @Schema(implementation = Record.class))),
            @ApiResponse(responseCode = "404", description = "Record doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Record> getRecord(@PathVariable Long id) {
        return new ResponseEntity<>(recordService.getRecord(id), HttpStatus.OK);
    }

    @Operation(summary = "Get a record by reader id and book id", description = "Return a record based on reader id and book id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of record", content = @Content(schema = @Schema(implementation = Record.class))),
            @ApiResponse(responseCode = "404", description = "Record doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{readerId}/reader/{bookId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Record> getRecord(@PathVariable Long readerId, @PathVariable Long bookId) {
        return new ResponseEntity<>(recordService.getRecord(readerId, bookId), HttpStatus.OK);
    }

    @Operation(summary = "Create/Edit a record of a reader and a book", description = "Create/Edit a record from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful saved a record", content = @Content(schema = @Schema(implementation = Record.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/{readerId}/reader/{bookId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Record> saveRecord(@Valid @RequestBody Record record, @PathVariable Long readerId,
            @PathVariable Long bookId) {
        return new ResponseEntity<>(recordService.saveRecord(record, readerId, bookId), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a record by id", description = "Delete a record based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deleted record for a reader and a book"),
            @ApiResponse(responseCode = "404", description = "Did not delete record for a reader and a book")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a record by reader id and book id", description = "Delete a record based on reader id and book id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deleted record for a reader and a book"),
            @ApiResponse(responseCode = "404", description = "Did not delete record for a reader and a book")
    })
    @DeleteMapping(value = "/{readerId}/reader/{bookId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long readerId, @PathVariable Long bookId) {
        recordService.deleteRecord(readerId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all records", description = "Return a list of all records")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all records", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Record.class))))
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Record>> getAllRecords() {
        return new ResponseEntity<>(recordService.getAllRecords(), HttpStatus.OK);
    }

    @Operation(summary = "Get all records of a reader", description = "Return a list of all records based on reader id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all records", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Record.class))))
    @GetMapping(value = "/all/{readerId}/reader", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Record>> getAllRecordsOfReader(@PathVariable Long readerId) {
        return new ResponseEntity<>(recordService.getRecordsOfReader(readerId), HttpStatus.OK);
    }

    @Operation(summary = "Get all records of a book", description = "Return a list of all records based on book id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all records", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Record.class))))
    @GetMapping(value = "/all/{bookId}/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Record>> getAllRecordsOfBook(@PathVariable Long bookId) {
        return new ResponseEntity<>(recordService.getRecordsOfBook(bookId), HttpStatus.OK);
    }
}
