package com.roaa.treading.service;

import com.roaa.treading.domain.VerificationType;
import com.roaa.treading.entity.User;

public interface UserService {

    public User findUserByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long id) throws Exception;
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);
    User updatePassword(User user, String newPassword);

}
