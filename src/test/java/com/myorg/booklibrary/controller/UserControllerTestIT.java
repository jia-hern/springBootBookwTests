package com.myorg.booklibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.myorg.booklibrary.entity.ResetPasswordDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.repository.UserRepository;
import com.myorg.booklibrary.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTestIT {
        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        UserRepository userRepository;

        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;

        @Autowired
        BookService bookService;

        @Test
        void contextLoads() {
                assertNotNull(mockMvc);
        }

        @DisplayName("Test get user by Id - no jwt")
        @Test
        public void getUserTest_noJwt_returnsForbidden() throws Exception {
                RequestBuilder request = MockMvcRequestBuilders.get("/user/2");
                mockMvc.perform(request).andExpect(status().isForbidden());
        }

        @DisplayName("Test valid get user by Id - with jwt")
        @WithMockUser("spring")
        @Test
        public void validGetUserTest_withJwt_returnsOk() throws Exception {
                RequestBuilder request = MockMvcRequestBuilders.get("/user/2");
                mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").value("user"));
        }

        @DisplayName("Test invalid get user by Id - with jwt")
        @WithMockUser("spring")
        @Test
        public void invalidGetUserTest_withJwt_returnsNotFound() throws Exception {
                RequestBuilder request = MockMvcRequestBuilders.get("/user/8");
                mockMvc.perform(request)
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @DisplayName("Test create valid user")
        @Test
        public void validUserCreationTest_returnsCreated() throws Exception {
                User newUser = new User("user5", "password");
                RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newUser));
                mockMvc.perform(request)
                                .andExpect(status().isCreated());
        }

        @DisplayName("Test create invalid user")
        @Test
        public void invalidUserCreationTest_returnsBadRequest() throws Exception {
                User newUser = new User("     ", "password");
                RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newUser));
                mockMvc.perform(request)
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message[0]").value("Username cannot be blank"));
        }

        @DisplayName("Test update valid user")
        @Test
        public void validUserUpdateTest_returnsCreated() throws Exception {
                User updateUser = new User("user_updated", "newPassword");
                updateUser.setId(Long.valueOf(3));
                RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUser));
                mockMvc.perform(request)
                                .andExpect(status().isCreated());
        }

        @DisplayName("Test update invalid user")
        @Test
        public void invalidUserUpdateTest_returnsBadReq() throws Exception {
                User updateUser = new User("     ", "newPassword");
                updateUser.setId(Long.valueOf(2));
                RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUser));
                mockMvc.perform(request)
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message[0]").value("Username cannot be blank"));
        }

        @DisplayName("Test reset user password")
        @Test
        public void validResetUserPassword_returnsNoContent() throws Exception {
                ResetPasswordDTO resetPasswordObj = new ResetPasswordDTO();
                resetPasswordObj.setUsername("user");
                resetPasswordObj.setPassword("password");
                resetPasswordObj.setNewPassword("newPassword");
                RequestBuilder request = MockMvcRequestBuilders.post("/user/reset")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resetPasswordObj));
                mockMvc.perform(request)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").value("user"));
                User userSaved = userRepository.findById(Long.valueOf(2)).get();
                assertTrue(bCryptPasswordEncoder.matches("newPassword", userSaved.getPassword()));
        }

        @DisplayName("Test invalid reset user password")
        @Test
        public void invalidResetUserPassword_returnsBadRequest() throws Exception {
                ResetPasswordDTO resetPasswordObj = new ResetPasswordDTO();
                resetPasswordObj.setUsername("user");
                resetPasswordObj.setPassword("password");
                resetPasswordObj.setNewPassword("1");
                RequestBuilder request = MockMvcRequestBuilders.post("/user/reset")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resetPasswordObj));

                mockMvc.perform(request)
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message[0]").value("New password is too short"));
        }

        @DisplayName("Test authentication username does not exist")
        @Test
        public void authenticationTest_invalidUsername_returnsNotFound() throws Exception {
                User user = new User("user8", "password");
                RequestBuilder request = MockMvcRequestBuilders.get("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user));
                mockMvc.perform(request).andExpect(status().isNotFound())
                                .andExpect(jsonPath("$").value("Username does not exist"));
        }

        @DisplayName("Test authentication password different")
        @Test
        public void authenticationTest_invalidPassword_returnsUnauthorized() throws Exception {
                User user = new User("user", "wrongPassword");
                RequestBuilder request = MockMvcRequestBuilders.get("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user));
                mockMvc.perform(request).andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$").value("You provided an incorrect password"));
        }

        @DisplayName("Test authentication success")
        @Test
        public void authenticationTest_success_returnsOk() throws Exception {
                User user = new User("admin", "password");
                RequestBuilder request = MockMvcRequestBuilders.get("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user));
                mockMvc.perform(request).andExpect(status().isOk())
                                .andExpect(header().exists("Authorization"));
        }
}
