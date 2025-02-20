package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.UserDao;
import com.TheLa.models.User;
import com.TheLa.services.IUserService;

public class UserService extends ViewModel implements IUserService {
    private final UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    @Override
    public User addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public User getUserFindByEmail(String email) {
        return userDao.getUserFindByEmail(email);
    }
}
