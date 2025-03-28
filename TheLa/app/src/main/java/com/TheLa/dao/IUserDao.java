package com.TheLa.dao;

import com.TheLa.models.UserModel;

public interface IUserDao {
    UserModel addUser(UserModel user);
    boolean updateUser(UserModel user);
    UserModel getUserFindByEmail(String email);
    UserModel getUserFindByUserId(Long userId);
}
