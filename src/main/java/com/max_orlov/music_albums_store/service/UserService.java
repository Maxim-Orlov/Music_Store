package com.max_orlov.music_albums_store.service;

import com.max_orlov.music_albums_store.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String userName);

    User saveNewUser(String userName, String password);

}
