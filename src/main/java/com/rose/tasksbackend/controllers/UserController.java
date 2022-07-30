package com.rose.tasksbackend.controllers;

import com.rose.tasksbackend.common.StringUtils;
import com.rose.tasksbackend.data.User;
import com.rose.tasksbackend.helpers.hashable.PasswordHashable;
import com.rose.tasksbackend.helpers.hashable.PasswordHashableFactory;
import com.rose.tasksbackend.helpers.userauth.AuthConstants;
import com.rose.tasksbackend.helpers.userauth.UserAuth;
import com.rose.tasksbackend.helpers.userauth.UserAuthFactory;
import com.rose.tasksbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService mUserService;
    private final PasswordHashable mPasswordHashable;
    private final UserAuth mUserAuth;

    @Autowired
    public UserController(UserService userService) {
        mUserService = userService;
        mPasswordHashable = PasswordHashableFactory.newPasswordHashable(PasswordHashableFactory.FAKE);
        mUserAuth = UserAuthFactory.newUserAuth(mUserService, mPasswordHashable);
    }

    @GetMapping(value = "/")
    public List<User> getUsers() {
        return mUserService.findAll()
                .stream()
                .peek(user -> user.setPassword(StringUtils.EMPTY))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/login/")
    public ResponseEntity<User> login(@RequestBody User body, HttpServletResponse response) {
        try {
            User user = mUserService.getReferenceById(body.getUsername());
            if (mPasswordHashable.verifyHash(body.getPassword(), user.getPassword())) {
                response.setHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY, mUserAuth.generateToken(user, -1L));
                user.setPassword(StringUtils.EMPTY);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                user.setPassword(StringUtils.EMPTY);
                return new ResponseEntity<>(user, HttpStatus.UNAUTHORIZED);
            }
        } catch (EntityNotFoundException e) {
            System.out.println("getUser " + body.getUsername() + " error " + e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<User> insertUser(@RequestBody User body, HttpServletResponse response) {
        if (mUserService.isUsernameExisted(body.getUsername())) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
        response.setHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY, mUserAuth.generateToken(body, -1L));
        body.setPassword(mPasswordHashable.getHash(body.getPassword()));
        User user = mUserService.save(body);
        user.setPassword(StringUtils.EMPTY);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/")
    public ResponseEntity<User> updateUser(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                           @RequestBody User body, HttpServletResponse response) {
        User user = mUserAuth.authorizeToken(token);

        if (user != null && Objects.equals(user.getUsername(), body.getUsername())) {
            boolean isChanged = false;
            if (!mPasswordHashable.verifyHash(body.getPassword(), user.getPassword())) {
                isChanged = true;
                response.setHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY, mUserAuth.generateToken(body, -1L));
                user.setPassword(mPasswordHashable.getHash(body.getPassword()));
            }
            if (!Objects.equals(body.getDisplayName(), user.getDisplayName())) {
                isChanged = true;
                user.setDisplayName(body.getDisplayName());
            }
            if (isChanged) {
                user = mUserService.save(user);
                user.setPassword(StringUtils.EMPTY);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(body, HttpStatus.NOT_MODIFIED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
