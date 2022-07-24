package com.rose.tasksbackend.services;

import com.rose.tasksbackend.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskService extends JpaRepository<Task, String> {
    @Query("SELECT t FROM Task t WHERE t.username=:username AND t.deleted=0")
    List<Task> getTasksByUsername(@Param("username") String username);

    @Query("SELECT t FROM Task t WHERE t.username=:username")
    List<Task> getTasksByUsernameForSync(@Param("username") String username);

    @Query("SELECT t FROM Task t WHERE t.username=:username AND t.deleted=0 AND t.uuid=:uuid")
    Task getTaskById(@Param("username") String username, @Param("uuid") String uuid);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.username=:username AND t.uuid=:uuid AND t.deleted=0")
    long getTaskIdCount(@Param("username") String username, @Param("uuid") String uuid);

    default boolean isTaskIdExisted(String username, String uuid) {
        return getTaskIdCount(username, uuid) > 0;
    }
}
