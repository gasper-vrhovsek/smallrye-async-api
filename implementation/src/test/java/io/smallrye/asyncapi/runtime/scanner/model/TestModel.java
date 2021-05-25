package io.smallrye.asyncapi.runtime.scanner.model;

import io.smallrye.asyncapi.spec.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(name = "TestModel")
public class TestModel {
    private List<User> userList;
    private Map<String, User> userMap;

    public TestModel(List<User> userList, Map<String, User> userMap) {
        this.userList = userList;
        this.userMap = userMap;
    }

    public List<User> getUserList() {
        return userList;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }
}
