<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}
        <a href="resume?uuid=${resume.uuid}&action=edit">
            <img src="img/pencil.png">
        </a>
    </h2>
    <ul class="contacts">
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <li><%=contactEntry.getKey().toHtml(contactEntry.getValue())%></li>
        </c:forEach>
    </ul>
    <div class="resume-sections">
        <div class="resume-s">
            <h2>${SectionType.OBJECTIVE.title}</h2>
            ${resume.sections.get(SectionType.OBJECTIVE).content}
        </div>
        <div class="resume-s">
            <h2>${SectionType.PERSONAL.title}</h2>
            ${resume.sections.get(SectionType.PERSONAL).content}
        </div>
        <div class="resume-s">
            <h2>${SectionType.ACHIEVEMENT.title}</h2>
            <c:if test="${resume.sections.get(SectionType.ACHIEVEMENT).content.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${resume.sections.get(SectionType.ACHIEVEMENT).content}">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
        <div class="resume-s">
            <h2>${SectionType.QUALIFICATIONS.title}</h2>
            <c:if test="${resume.sections.get(SectionType.QUALIFICATIONS).content.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${resume.sections.get(SectionType.QUALIFICATIONS).content}">
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
        <div class="resume-s organization-s">
            <h2>${SectionType.EXPERIENCE.title}</h2>
            <c:if test="${resume.sections.get(SectionType.EXPERIENCE).content.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${resume.sections.get(SectionType.EXPERIENCE).content}">
                        <li>
                            <h3><a href="${item.website}">${item.name}</a></h3>
                            <c:forEach var="period" items="${item.periods}">
                                <div class="row">
                                    <p class="col-1">
                                        ${DateUtil.format(period.startDate, "MM/yyyy")} - ${DateUtil.format(period.endDate, "MM/yyyy")}
                                    </p>
                                    <p class="col-3">${period.description}</p>
                                </div>
                            </c:forEach>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
        <div class="resume-s organization-s">
            <h2>${SectionType.EDUCATION.title}</h2>
            <c:if test="${resume.sections.get(SectionType.EDUCATION).content.size() > 0}">
                <ul>
                    <c:forEach var="item" items="${resume.sections.get(SectionType.EDUCATION).content}">
                        <li>
                            <h3><a href="${item.website}">${item.name}</a></h3>
                            <c:forEach var="period" items="${item.periods}">
                                <div class="row">
                                    <p class="col-1">
                                        ${DateUtil.format(period.startDate, "MM/yyyy")} - ${DateUtil.format(period.endDate, "MM/yyyy")}
                                    </p>
                                    <p class="col-3">${period.description}</p>
                                </div>
                            </c:forEach>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>