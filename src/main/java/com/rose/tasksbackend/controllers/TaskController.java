package com.rose.tasksbackend.controllers;

import com.rose.tasksbackend.common.StringUtils;
import com.rose.tasksbackend.data.Task;
import com.rose.tasksbackend.data.User;
import com.rose.tasksbackend.helpers.hashable.PasswordHashableFactory;
import com.rose.tasksbackend.helpers.userauth.AuthConstants;
import com.rose.tasksbackend.helpers.userauth.UserAuth;
import com.rose.tasksbackend.helpers.userauth.UserAuthFactory;
import com.rose.tasksbackend.services.TaskService;
import com.rose.tasksbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/task")
public class TaskController {
    private final TaskService mTaskService;
    private final UserAuth mUserAuth;

    @Autowired
    public TaskController(UserService userService, TaskService taskService) {
        mTaskService = taskService;
        mUserAuth = UserAuthFactory.newUserAuth(userService,
                PasswordHashableFactory.newPasswordHashable());
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<Task>> getAll(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Task> list = mTaskService.getTasksByUsername(user.getUsername());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(path = "/{taskId}")
    public ResponseEntity<Task> getTaskById(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                            @PathVariable("taskId") String taskId) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Task task = mTaskService.getTaskById(user.getUsername(), taskId);
            return new ResponseEntity<>(task, task == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            System.err.println("getTaskById not found: " + taskId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<Task> insertTask(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                           @RequestBody Task body) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (StringUtils.isBlankOrEmpty(body.getUuid())) {
            body.setUuid(UUID.randomUUID().toString());
        }
        long current = System.currentTimeMillis();
        body.setCreatedTime(current);
        body.setModifiedTime(current);
        body.setUsername(user.getUsername());
        body = mTaskService.save(body);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping(path = "/")
    public ResponseEntity<Task> updateTask(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                           @RequestBody Task body) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (mTaskService.isTaskIdExisted(user.getUsername(), body.getUuid())) {
            body.setUsername(user.getUsername());
            body.setModifiedTime(System.currentTimeMillis());
            body = mTaskService.save(body);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Task> deleteTask(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                           @PathVariable("uuid") String uuid) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Task task = mTaskService.getTaskById(user.getUsername(), uuid);

        if (task != null) {
            task.setDeleted(true);
            task.setModifiedTime(System.currentTimeMillis());
            task = mTaskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
