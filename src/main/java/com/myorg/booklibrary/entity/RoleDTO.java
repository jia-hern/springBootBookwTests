package com.myorg.booklibrary.entity;

import com.myorg.booklibrary.validation.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    @NonNull
    @Role
    private String role;
}
