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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.myorg.booklibrary.entity.RoleDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.entity.UserDTO;
import com.myorg.booklibrary.service.UserService;
import com.myorg.booklibrary.exception.ErrorResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "Update user roles, get all users and delete users. Only users with admin roles can access these")
public class AdminController {
    UserService userService;

    @Operation(summary = "Update user role by id", description = "Update user role by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful updated user role", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User doesn't exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserRoleById(@PathVariable Long id, @Valid @RequestBody RoleDTO role) {
        User user = userService.updateRole(role.getRole(), id);
        String returnString = "User of username " + user.getUsername() + " has new role of " + user.getRole();
        return new ResponseEntity<>(returnString, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve all users", description = "Return a list of all users")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Delete user by id", description = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deleted user"),
            @ApiResponse(responseCode = "404", description = "Unsuccessful deletion")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
