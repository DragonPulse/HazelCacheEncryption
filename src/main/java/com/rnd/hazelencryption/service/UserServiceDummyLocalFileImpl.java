package com.rnd.hazelencryption.service;


import com.rnd.hazelencryption.HazelcastEncryptionDemoApplication;
import com.rnd.hazelencryption.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceDummyLocalFileImpl implements UserService {

    private static final String USERS_FILE = "users.json";
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    @Cacheable(HazelcastEncryptionDemoApplication.USERS_CACHE)
    public User getUser(long accountId) {

        // Exaggerate delay, to simulate a slow query to DB
        // Do not try this at home! (to be read production)
        sleep();

        InputStream fileURI = this.getClass().getClassLoader().getResourceAsStream(USERS_FILE);

        JsonReader reader = new JsonReader(new InputStreamReader(fileURI));
        User[] users = GSON.fromJson(reader, User[].class);
        Optional<User> user = Arrays.stream(users).filter(u -> u.accountId == accountId).findFirst();

        return user.orElse(null);
    }

    @Override
    public List<User> getUsers() {
        InputStream fileURI = this.getClass().getClassLoader().getResourceAsStream(USERS_FILE);

        JsonReader reader = new JsonReader(new InputStreamReader(fileURI));
        User[] users = GSON.fromJson(reader, User[].class);
        return Arrays.asList(users);
    }

    private static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignore) {
        }
    }
}
