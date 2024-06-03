package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    //:TODO возможности юзера: создать группу, вступить в группу
    //:TODO возможности админа группы: удалить юзера из группы, удалить группу
    //:TODO возможности админа: удалить пользователя, удалить группу

}
