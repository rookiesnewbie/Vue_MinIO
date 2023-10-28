package com.example.minio.admin;

/**
 * @Author LiTeng
 * @Date 2023/10/15 17:46
 * Version 1.0
 * @Description
 */

public class User {
    private String name;
    private int id;
    private int age;

    public User() {
    }

    public User(String name, int id, int age) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + this.name + '\'' +
                ",id+" + this.id +
                ",age+" + this.age +
                "}";
    }
}