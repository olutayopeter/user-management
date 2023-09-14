package com.fastcredit.usermanagement.service.impl;

import com.fastcredit.usermanagement.repository.UserRepository;
import com.fastcredit.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.fastcredit.usermanagement.model.Users;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired(required = false)
    private final UserRepository userRepository;


    @Override
    public List<Users> findDetailsByUsername(String username) {
        return userRepository.findDetailsByUsername(username);

    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findByUsernameOrEmailAndPassword(String usernameOrEmail, String password) {
        return userRepository.findByUsernameOrEmailAndPassword(usernameOrEmail,usernameOrEmail,password);
    }

    @Override
    public Users findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
    }


}
