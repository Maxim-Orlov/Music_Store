package com.max_orlov.music_albums_store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "user_name", unique = true)
  private String userName;

  @Column(name = "user_password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role")
  private Role role;

}
