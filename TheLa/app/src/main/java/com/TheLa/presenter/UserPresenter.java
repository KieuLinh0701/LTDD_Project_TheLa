package com.TheLa.presenter;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.User;
import com.TheLa.repository.UserRepository;

public class UserPresenter extends ViewModel {
    private final UserRepository userRepository;

    public UserPresenter() {
        userRepository = new UserRepository();
    }

    public boolean addUser(User user) {
        return userRepository.addUser(user);
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public User getUserFindByEmail(String email) {
        return userRepository.getUserFindByEmail(email);
    }
}
