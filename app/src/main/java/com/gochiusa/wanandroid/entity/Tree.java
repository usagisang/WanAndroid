package com.gochiusa.wanandroid.entity;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Tree {
    private String name;
    private Map<String, Integer> children;

    public Tree() {
        children = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChildren(String childrenName, int childrenId) {
        children.put(childrenName, childrenId);
    }

    public int getChildrenId(String childrenName) {
        Integer id = children.get(childrenName);
        if (id != null) {
            return id;
        } else {
            return 0;
        }
    }

    public Set<Map.Entry<String, Integer>> getAllChildren() {
        return children.entrySet();
    }

    public Set<String> getAllChildrenName() {
        return children.keySet();
    }

}
