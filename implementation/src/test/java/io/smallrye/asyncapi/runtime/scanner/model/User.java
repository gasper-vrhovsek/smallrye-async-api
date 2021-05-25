package io.smallrye.asyncapi.runtime.scanner.model;

import io.smallrye.asyncapi.spec.annotations.media.Schema;

@Schema(name = "user")
public class User {
    private String name;
    private String surname;
    private long age;

    public User(String name, String surname, long age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public long getAge() {
        return age;
    }
}
