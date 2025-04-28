package ru.javawebinar.basejava.model;

import java.util.List;

public class OrganizationSection extends Section {
    private List<Organization> content;

    public List<Organization> getContent() {
        return content;
    }

    public void setContent(List<Organization> content) {
        this.content = content;
    }

    @Override
    public void print() {
        for (Organization o : content) {
            System.out.println(o.getName());
            System.out.println("[" + o.getWebsite() + "]");

            for (Period p : o.getPeriods()) {
                System.out.printf("[%s - %s]%n", p.getStartDate(), p.getEndDate());
                System.out.println(p.getDescription());
            }
            System.out.println();
        }
    }
}
