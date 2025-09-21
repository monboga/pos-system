package com.gabriel.pos_system.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.gabriel.pos_system.dto.UserDto;
import com.gabriel.pos_system.model.User;

public interface UserService {
    void saveUser(UserDto userDto, MultipartFile photoFile) throws IOException;

    User findUserByEmail(String email);

    List<User> findAllUsers();

    Page<User> findPaginated(int page, int size, String searchName);

    void updatePassword(User user, String newPassword);

    void updatePassword(String email, String newPassword);
}
