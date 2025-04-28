package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends Section {
    private List<String> content;

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public void print() {
        for (String s : content) {
            System.out.println("\t > " + s);
        }
    }
}
