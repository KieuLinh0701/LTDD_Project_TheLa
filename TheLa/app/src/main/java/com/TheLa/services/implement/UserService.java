package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.UserDao;
import com.TheLa.models.UserModel;
import com.TheLa.services.IUserService;

public class UserService extends ViewModel implements IUserService {
    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    @Override
    public UserModel addUser(UserModel user) {
        return userDao.addUser(user);
    }

    @Override
    public boolean updateUser(UserModel user) {
        return userDao.updateUser(user);
    }

    @Override
    public UserModel getUserFindByEmail(String email) {
        return userDao.getUserFindByEmail(email);
    }

    @Override
    public UserModel getUserFindByUserId(Long userId) {
        return userDao.getUserFindByUserId(userId);
    }
}
