package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.User;
import com.TheLa.repository.implement.UserRepository;
import com.TheLa.services.IUserService;

public class UserService extends ViewModel implements IUserService {
    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    @Override
    public User getUserFindByEmail(String email) {
        return userRepository.getUserFindByEmail(email);
    }
}
