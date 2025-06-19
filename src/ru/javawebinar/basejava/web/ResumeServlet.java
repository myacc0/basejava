package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final Storage storage = new SqlStorage(
            Config.get().getDbUrl(),
            Config.get().getDbUser(),
            Config.get().getDbPassword());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uuid = req.getParameter("uuid");
        String fullName = req.getParameter("fullName").trim();

        if (uuid == null) {
            Resume r = new Resume(fullName);
            setContacts(r, req);
            setSections(r, req);
            storage.save(r);
            resp.sendRedirect("resume?action=edit&uuid=" + r.getUuid());
        }
        else {
            Resume r = storage.get(uuid);
            r.setFullName(fullName);
            setContacts(r, req);
            setSections(r, req);
            storage.update(r);
            resp.sendRedirect("resume");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("resumes", storage.getAllSorted());
            req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
            return;
        }

        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                resp.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                req.setAttribute("resume", r);
                req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);
                break;
            case "edit":
                r = storage.get(uuid);
                req.setAttribute("resume", r);
                req.setAttribute("achievement", convertListSection(r, SectionType.ACHIEVEMENT));
                req.setAttribute("qualifications", convertListSection(r, SectionType.QUALIFICATIONS));
                req.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(req, resp);
                break;
            case "add":
                req.getRequestDispatcher("/WEB-INF/jsp/add.jsp").forward(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    private void setContacts(Resume r, HttpServletRequest req) {
        for (ContactType type : ContactType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private void setSections(Resume r, HttpServletRequest req) {
        for (SectionType type : SectionType.values()) {
            String value = req.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case OBJECTIVE, PERSONAL -> {
                        r.addSection(type, new TextSection(value));
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        List<String> items = Arrays.stream(value.split("\n"))
                                .map(String::trim)
                                .filter(s -> !s.isBlank())
                                .toList();
                        r.addSection(type, new ListSection(items));
                    }
                }
            } else {
                r.getContacts().remove(type);
            }
        }
    }

    private String convertListSection(Resume r, SectionType type) {
        ListSection section = (ListSection) r.getSections().get(type);
        return section != null ? String.join("\n", section.getContent()) : "";
    }

}
