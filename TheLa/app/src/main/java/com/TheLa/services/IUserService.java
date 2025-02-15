package com.TheLa.services;

import com.TheLa.models.User;

public interface IUserService {
    User addUser(User user);
    boolean updateUser(User user);
    User getUserFindByEmail(String email);
}
