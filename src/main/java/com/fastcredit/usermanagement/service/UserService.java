package com.fastcredit.usermanagement.service;


import com.fastcredit.usermanagement.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

   List<Users> findDetailsByUsername(String username);

   Optional<Users> findByUsername(String username);

   Optional<Users> findByEmail(String email);

   Users findByUsernameOrEmailAndPassword(String usernameOrEmail,String password);

   Users findByUsernameOrEmail(String usernameOrEmail);
}
