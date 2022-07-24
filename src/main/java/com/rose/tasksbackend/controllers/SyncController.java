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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/sync")
public class SyncController {
    private final TaskService mTaskService;
    private final UserAuth mUserAuth;

    @Autowired
    public SyncController(UserService userService, TaskService taskService) {
        mTaskService = taskService;
        mUserAuth = UserAuthFactory.newUserAuth(userService,
                PasswordHashableFactory.newPasswordHashable(PasswordHashableFactory.FAKE));
    }

    @PostMapping(path = "/")
    public ResponseEntity<List<Task>> syncTasks(@RequestHeader(AuthConstants.AUTH_HEADER_TOKEN_KEY) String token,
                                          @RequestBody List<Task> clientTasks) {
        User user = mUserAuth.authorizeToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Task> updatedTasks = new ArrayList<>();
        List<Task> responseTasks = new ArrayList<>();

        List<Task> serverTasks = mTaskService.getTasksByUsernameForSync(user.getUsername());

        Map<String, Task> clientTasksMap = clientTasks.stream().collect(Collectors.toMap(Task::getUuid, task -> task));
        Map<String, Task> serverTasksMap = serverTasks.stream().collect(Collectors.toMap(Task::getUuid, task -> task));

        for (Task task: serverTasks) {
            if (clientTasksMap.containsKey(task.getUuid())) {
                Task clientTask = clientTasksMap.get(task.getUuid());
                if (clientTask.getModifiedTime() > task.getCreatedTime()) {
                    updatedTasks.add(clientTask);
                } else {
                    responseTasks.add(task);
                }
            } else {
                responseTasks.add(task);
            }
        }

        for (Task task: clientTasks) {
            if (!serverTasksMap.containsKey(task.getUuid())) {
                updatedTasks.add(task);
            }
        }

        mTaskService.saveAll(updatedTasks.stream().peek(task -> task.setUsername(user.getUsername()))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(responseTasks, HttpStatus.OK);
    }
}
