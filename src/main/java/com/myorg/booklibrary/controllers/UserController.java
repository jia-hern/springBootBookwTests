package com.myorg.booklibrary.controllers;

import org.springframework.http.MediaType;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myorg.booklibrary.entity.ResetPasswordDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.service.UserService;
import com.myorg.booklibrary.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Create, find user, authenticate and reset password")
public class UserController {
    UserService userService;

    @Operation(summary = "Get user by id", description = "Return a user name based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of user", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id).getUsername(), HttpStatus.OK);
    }

    @Operation(summary = "Create/Edit User", description = "Create/Edit a user from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful saved the user"),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission")
    })
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Reset user password", description = "Reset a user password from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful reset user password"),
            @ApiResponse(responseCode = "400", description = "Bad request: unsuccessful submission")
    })
    @PostMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO credentials) {
        userService.resetPassword(credentials);
        return new ResponseEntity<>(userService.getUser(credentials.getUsername()).getUsername(), HttpStatus.OK);
    }

    @Operation(summary = "Authentication", description = "Authenticate the user. If successful, header has a bearer token to use for subsequent requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden access without authentication"),
            @ApiResponse(responseCode = "200", description = "Successful authentcation of user")
    })
    @PostMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> authenticateUser(@Valid @RequestBody User user) {
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
