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

    <c:if test="${resume.contacts.size() > 0}">
        <ul class="contacts">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
                <li><%=contactEntry.getKey().toHtml(contactEntry.getValue())%></li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${resume.sections.size() > 0}">
        <div class="resume-sections">

            <c:if test="${resume.sections.get(SectionType.OBJECTIVE) != null}">
                <div class="resume-s">
                    <h2>${SectionType.OBJECTIVE.title}</h2>
                    ${resume.sections.get(SectionType.OBJECTIVE).content}
                </div>
            </c:if>

            <c:if test="${resume.sections.get(SectionType.PERSONAL) != null}">
                <div class="resume-s">
                    <h2>${SectionType.PERSONAL.title}</h2>
                        ${resume.sections.get(SectionType.PERSONAL).content}
                </div>
            </c:if>

            <c:if test="${resume.sections.get(SectionType.ACHIEVEMENT) != null}">
                <div class="resume-s">
                    <h2>${SectionType.ACHIEVEMENT.title}</h2>
                    <ul>
                        <c:forEach var="item" items="${resume.sections.get(SectionType.ACHIEVEMENT).content}">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <c:if test="${resume.sections.get(SectionType.QUALIFICATIONS) != null}">
                <div class="resume-s">
                    <h2>${SectionType.QUALIFICATIONS.title}</h2>
                    <ul>
                        <c:forEach var="item" items="${resume.sections.get(SectionType.QUALIFICATIONS).content}">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <c:if test="${resume.sections.get(SectionType.EXPERIENCE) != null}">
                <div class="resume-s organization-s">
                    <h2>${SectionType.EXPERIENCE.title}</h2>
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
                </div>
            </c:if>

            <c:if test="${resume.sections.get(SectionType.EDUCATION) != null}">
                <div class="resume-s organization-s">
                    <h2>${SectionType.EDUCATION.title}</h2>
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
                </div>
            </c:if>

        </div>
    </c:if>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>