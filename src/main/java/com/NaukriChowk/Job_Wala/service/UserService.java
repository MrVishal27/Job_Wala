package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.UserDTO;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.response.UserResponse;

public interface UserService {

    User findUserById(Long userId) throws Exception;

   // User findUserProfileByJwt(String jwt) throws Exception;


    UserResponse getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);

}
