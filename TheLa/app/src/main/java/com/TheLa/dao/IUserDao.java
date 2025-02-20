package com.TheLa.dao;

import com.TheLa.models.User;

public interface IUserDao {
    User addUser(User user);
    boolean updateUser(User user);
    User getUserFindByEmail(String email);
}
