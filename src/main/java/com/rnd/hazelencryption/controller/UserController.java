package com.rnd.hazelencryption.controller;



import com.rnd.hazelencryption.HazelcastEncryptionDemoApplication;
import com.rnd.hazelencryption.crypto.CipherServiceImpl;
import com.rnd.hazelencryption.model.CacheObject;
import com.rnd.hazelencryption.model.User;
import com.rnd.hazelencryption.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/users")
@Api("API for Insert in Cache. Whenever the the USer is loaded by AccountId, that User account is encrypted and loaded into Cache ")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private ApplicationContext context;

    @Autowired
    private CipherServiceImpl cipherServiceImpl;

    /**
     * Gets user.
     *
     * @param accountId the account id
     * @return the user
     */
    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    @ApiOperation(notes = "Load  User info  by accountId", value = "Load  User info  by accountId")
    public User getUser(@PathVariable long accountId) {
        return userService.getUser(accountId);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(notes = "Load all Users to see", value = "API to fetch all users ")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Hackish solution to view the cached items using reflection.
     * Only for demo purposes!
     *
     * @return the items cached in Hazelcast
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/cached", method = RequestMethod.GET)
    public Set<CacheObject> getCachedUsers() {
        Set<CacheObject> cacheObjSet = new HashSet<>();
        CacheManager cacheManager = (CacheManager) context.getBean("cacheManager");
        Cache cache = cacheManager.getCache(HazelcastEncryptionDemoApplication.USERS_CACHE);
        Field field = ReflectionUtils.findField(cache.getClass(), "map");
        ReflectionUtils.makeAccessible(field);
        Map map = (Map) ReflectionUtils.getField(field, cache);
        for (Object entry : map.entrySet()){
            cacheObjSet.add(new CacheObject(((Map.Entry<String,String>)entry).getKey(),((Map.Entry<String,String>)entry).getValue()));
        }
        return cacheObjSet;
    }

    /**
     * Decrpty string.
     *
     * @param data the data
     * @return the string
     */
    @RequestMapping(value = "/decrypt/{encryptData}", method = RequestMethod.GET)
    public String decrpty(@PathVariable  String encryptData){
        return cipherServiceImpl.decrypt(encryptData).toString();
    }
}
