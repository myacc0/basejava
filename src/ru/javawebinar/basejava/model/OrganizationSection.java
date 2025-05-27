package ru.javawebinar.basejava.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationSection extends Section {
    private static final long serialVersionUID = 1L;

    private List<Organization> content;

    public OrganizationSection() {
        this.content = new ArrayList<>();
    }

    public List<Organization> getContent() {
        return content;
    }

    public void setContent(List<Organization> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrganizationSection that = (OrganizationSection) object;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    @Override
    public String toString() {
        return "OrganizationSection{" +
                "content=" + content +
                '}';
    }
}
