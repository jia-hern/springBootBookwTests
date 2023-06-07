package com.myorg.booklibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.myorg.booklibrary.entity.RoleDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTestIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    List<User> users;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @DisplayName("Test update user role by id - no jwt")
    @Test
    public void updateUserTest_noJwt_returnsForbidden() throws Exception {
        RoleDTO roleDTO = new RoleDTO("admin");
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDTO));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test update user role by id - jwt, not admin")
    @WithMockUser(roles = "user")
    @Test
    public void updateUserTest_withJwt_notAdmin_returnsForbidden() throws Exception {
        RoleDTO roleDTO = new RoleDTO("admin");
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDTO));
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test update user role by id - jwt, admin")
    @WithMockUser(roles = "admin")
    @Test
    public void updateUserTest_withJwt_admin_returnsOk() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole("admin");
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDTO));
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User of username user3 has new role of admin"));
    }

    @DisplayName("Test update user invalid role by id - jwt, admin")
    @WithMockUser(roles = "admin")
    @Test
    public void updateUserTestInvalidRole_withJwt_admin_returnsBadReq() throws Exception {
        RoleDTO roleDTO = new RoleDTO("weirdRole");
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDTO));
        mockMvc.perform(request).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Role should be user or admin"));
    }

    @DisplayName("Test get all users - no jwt")
    @Test
    public void getAllUsersTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/all");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all users - jwt, not admin")
    @WithMockUser(roles = "user")
    @Test
    public void getAllUsersTest_withJwt_notAdmin_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/all");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test get all users - jwt, admin")
    @WithMockUser(roles = "admin")
    @Test
    public void getAllUsersTest_withJwt_admin_returnsOk() throws Exception {
        List<User> users = (List<User>) userRepository.findAll();

        RequestBuilder request = MockMvcRequestBuilders.get("/admin/all");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].role").value("admin"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].username").value("user"))
                .andExpect(jsonPath("$[1].role").value("user"))
                .andExpect(jsonPath("$.size()").value(users.size()));
    }

    @DisplayName("Test delete user by id - no jwt")
    @Test
    public void deleteUserTest_noJwt_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/admin/5");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete user by id - jwt, not admin")
    @WithMockUser(roles = "user")
    @Test
    public void deleteUserTest_withJwt_notAdmin_returnsForbidden() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/admin/5");
        mockMvc.perform(request).andExpect(status().isForbidden());
    }

    @DisplayName("Test delete user by id - jwt, admin")
    @WithMockUser(roles = "admin")
    @Test
    public void deleteUserTest_withJwt_admin_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/admin/5");
        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @DisplayName("Test delete user by invalid id - jwt, admin")
    @WithMockUser(roles = "admin")
    @Test
    public void deleteUserTestInvalid_withJwt_admin_returnsNoContent() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/admin/10");
        mockMvc.perform(request).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message[0]").value("Cannot delete non-existing resource"));
    }
}
