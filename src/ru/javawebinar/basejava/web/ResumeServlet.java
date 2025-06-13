package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private final Storage storage = new SqlStorage(
            Config.get().getDbUrl(),
            Config.get().getDbUser(),
            Config.get().getDbPassword());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        StringBuilder sb = new StringBuilder("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<link rel=\"stylesheet\" href=\"css/styles.css\">")
                .append("<title>Резюме</title>")
                .append("</head>")
                .append("<body>")
                .append("<table>")
                .append("<tr>")
                .append("<th>UUID</th>")
                .append("<th>Full name</th>")
                .append("</tr>")
                .append("<tbody>");

        for (Resume r : storage.getAllSorted()) {
            sb.append("<tr>")
                    .append("<td>").append(r.getUuid()).append("</td>")
                    .append("<td>").append(r.getFullName()).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table>")
                .append("</body>")
                .append("</html>");

        resp.getWriter().write(sb.toString());
    }
}
