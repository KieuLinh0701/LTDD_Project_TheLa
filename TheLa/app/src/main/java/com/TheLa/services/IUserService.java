package com.TheLa.services;

import com.TheLa.models.UserModel;

public interface IUserService {
    UserModel addUser(UserModel user);
    boolean updateUser(UserModel user);
    UserModel getUserFindByEmail(String email);
    UserModel getUserFindByUserId(Long userId);
}
