package com.contour.service.impl;

import com.contour.model.entity.User;
import com.contour.repository.UserRepository;
import com.contour.service.UserService;
import com.contour.service.V2rayService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final V2rayService v2rayService;

    public UserServiceImpl(UserRepository userRepository, V2rayService v2rayService) {
        this.userRepository = userRepository;
        this.v2rayService = v2rayService;
    }

    @Override
    public User insertUser(User user) {
        // server
        // inbound
        // vmess ...
        // user
        v2rayService.insertUser(user);
        return userRepository.save(user);
    }
}
