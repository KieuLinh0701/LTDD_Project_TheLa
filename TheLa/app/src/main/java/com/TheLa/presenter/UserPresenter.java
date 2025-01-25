package com.TheLa.presenter;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.User;
import com.TheLa.repository.UserRepository;

public class UserPresenter extends ViewModel {
    private UserRepository userRepository;

    public UserPresenter() {
        userRepository = new UserRepository();
    }

    public void addUser(User user) {
        userRepository.addUser(user);
    }

    public User getUserFindByEmail(String email) {
        return userRepository.getUserFindByEmail(email);
    }
}
