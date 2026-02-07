package com.max_orlov.music_albums_store.service;

import com.max_orlov.music_albums_store.model.Role;
import com.max_orlov.music_albums_store.model.User;
import com.max_orlov.music_albums_store.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> findByUsername(String userName) {
    return userRepository.findByUserName(userName);
  }

  @Override
  public User saveNewUser(String userName, String password) {
    User user = new User();
    user.setUserName(userName);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole(Role.USER);
    return userRepository.save(user);
  }

}
