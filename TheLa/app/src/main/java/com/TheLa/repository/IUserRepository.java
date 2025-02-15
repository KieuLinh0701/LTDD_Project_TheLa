package com.TheLa.repository;

import com.TheLa.models.User;

public interface IUserRepository {
    User addUser(User user);
    boolean updateUser(User user);
    User getUserFindByEmail(String email);
}
