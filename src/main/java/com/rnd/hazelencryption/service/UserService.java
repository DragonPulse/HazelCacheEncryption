package com.rnd.hazelencryption.service;


import com.rnd.hazelencryption.model.User;

import java.util.List;

public interface UserService {

    User getUser(long accountId);

    List<User> getUsers();
}
