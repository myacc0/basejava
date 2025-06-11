package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ResumeTestData {

    public static void main(String[] args) {
        Resume resume = createResume("uuid1", "Григорий Кислин");
        printResume(resume);
    }

    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        resume.setContacts(new HashMap<>());
        resume.setSections(new HashMap<>());

        resume.getContacts().put(ContactType.CELL_PHONE, "+7(921) 855-0482");
        resume.getContacts().put(ContactType.SKYPE, "skype:grigory.kislin");
        resume.getContacts().put(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.getContacts().put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.getContacts().put(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.getContacts().put(ContactType.HOMEPAGE, "https://gkislin.ru/");

        /*
        resume.getSections()
                .put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));

        resume.getSections()
                .put(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        ListSection achievements = new ListSection();
        achievements.setContent(new ArrayList<>());
        achievements.getContent().add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        achievements.getContent().add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
        achievements.getContent().add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievements.getContent().add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        achievements.getContent().add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievements.getContent().add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        achievements.getContent().add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        resume.getSections().put(SectionType.ACHIEVEMENT, achievements);

        ListSection qualifications = new ListSection();
        qualifications.setContent(new ArrayList<>());
        qualifications.getContent().add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualifications.getContent().add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualifications.getContent().add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");
        qualifications.getContent().add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        qualifications.getContent().add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        qualifications.getContent().add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        qualifications.getContent().add("Python: Django.");
        qualifications.getContent().add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualifications.getContent().add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualifications.getContent().add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        qualifications.getContent().add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        qualifications.getContent().add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        qualifications.getContent().add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        qualifications.getContent().add("Родной русский, английский \"upper intermediate\"");
        resume.getSections().put(SectionType.QUALIFICATIONS, qualifications);

        OrganizationSection jobExperience = new OrganizationSection();
        fillJobExperiences(jobExperience);
        resume.getSections().put(SectionType.EXPERIENCE, jobExperience);

        OrganizationSection education = new OrganizationSection();
        fillEducationSpecialities(education);
        resume.getSections().put(SectionType.EDUCATION, education);
        */
        return resume;
    }

    private static void fillJobExperiences(OrganizationSection organization) {
        Organization o1 = new Organization();
        o1.setName("Java Online Projects");
        o1.setWebsite("http://javaops.ru/");
        Period p1 = new Period();
        p1.setStartDate(LocalDate.of(2013, 10, 1));
        p1.setEndDate(LocalDate.now());
        p1.setDescription("Автор проекта \n Создание, организация и проведение Java онлайн проектов и стажировок.");
        o1.setPeriods(List.of(p1));

        Organization o2 = new Organization();
        o2.setName("Wrike");
        o2.setWebsite("https://www.wrike.com/");
        Period p2 = new Period();
        p2.setStartDate(LocalDate.of(2014, 10, 1));
        p2.setEndDate(LocalDate.of(2016, 1, 1));
        p2.setDescription("Старший разработчик (backend) \n Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        o2.setPeriods(List.of(p2));

        Organization o3 = new Organization();
        o3.setName("RIT Center");
        o3.setWebsite("");
        Period p3 = new Period();
        p3.setStartDate(LocalDate.of(2012, 4, 1));
        p3.setEndDate(LocalDate.of(2014, 10, 1));
        p3.setDescription("Java архитектор \n Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python");
        o3.setPeriods(List.of(p3));

        Organization o4 = new Organization();
        o4.setName("Luxoft (Deutsche Bank)");
        o4.setWebsite("http://www.luxoft.ru/");
        Period p4 = new Period();
        p4.setStartDate(LocalDate.of(2010, 12, 1));
        p4.setEndDate(LocalDate.of(2012, 4, 1));
        p4.setDescription("Ведущий программист \n Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.");
        o4.setPeriods(List.of(p4));

        Organization o5 = new Organization();
        o5.setName("Yota");
        o5.setWebsite("https://www.yota.ru/");
        Period p5 = new Period();
        p5.setStartDate(LocalDate.of(2008, 6, 1));
        p5.setEndDate(LocalDate.of(2010, 12, 1));
        p5.setDescription("Ведущий специалист \n Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)");
        o5.setPeriods(List.of(p5));

        Organization o6 = new Organization();
        o6.setName("Enkata");
        o6.setWebsite("http://enkata.com/");
        Period p6 = new Period();
        p6.setStartDate(LocalDate.of(2007, 3, 1));
        p6.setEndDate(LocalDate.of(2008, 6, 1));
        p6.setDescription("Разработчик ПО \n Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).");
        o6.setPeriods(List.of(p6));

        Organization o7 = new Organization();
        o7.setName("Siemens AG");
        o7.setWebsite("https://www.siemens.com/ru/ru/home.html");
        Period p7 = new Period();
        p7.setStartDate(LocalDate.of(2005, 1, 1));
        p7.setEndDate(LocalDate.of(2007, 2, 1));
        p7.setDescription("Разработчик ПО \n Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).");
        o7.setPeriods(List.of(p7));

        Organization o8 = new Organization();
        o8.setName("Alcatel");
        o8.setWebsite("http://www.alcatel.ru/");
        Period p8 = new Period();
        p8.setStartDate(LocalDate.of(1997, 9, 1));
        p8.setEndDate(LocalDate.of(2005, 1, 1));
        p8.setDescription("Инженер по аппаратному и программному тестированию \n Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).");
        o8.setPeriods(List.of(p8));

        organization.setContent(List.of(o1, o2, o3, o4, o5, o6, o7, o8));
    }

    private static void fillEducationSpecialities(OrganizationSection education) {
        Organization o1 = new Organization();
        o1.setName("Coursera");
        o1.setWebsite("https://www.coursera.org/course/progfun");
        Period p1 = new Period();
        p1.setStartDate(LocalDate.of(2013, 3, 1));
        p1.setEndDate(LocalDate.of(2013, 5, 1));
        p1.setDescription("'Functional Programming Principles in Scala' by Martin Odersky");
        o1.setPeriods(List.of(p1));

        Organization o2 = new Organization();
        o2.setName("Luxoft");
        o2.setWebsite("http://www.luxoft-training.ru/training/catalog/course.html?ID=22366");
        Period p2 = new Period();
        p2.setStartDate(LocalDate.of(2011, 3, 1));
        p2.setEndDate(LocalDate.of(2011, 4, 1));
        p2.setDescription("Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'");
        o2.setPeriods(List.of(p2));

        Organization o3 = new Organization();
        o3.setName("Siemens AG");
        o3.setWebsite("http://www.siemens.ru/");
        Period p3 = new Period();
        p3.setStartDate(LocalDate.of(2005, 1, 1));
        p3.setEndDate(LocalDate.of(2005, 4, 1));
        p3.setDescription("3 месяца обучения мобильным IN сетям (Берлин)");
        o3.setPeriods(List.of(p3));

        Organization o4 = new Organization();
        o4.setName("Alcatel");
        o4.setWebsite("http://www.alcatel.ru/");
        Period p4 = new Period();
        p4.setStartDate(LocalDate.of(1997, 9, 1));
        p4.setEndDate(LocalDate.of(1998, 3, 1));
        p4.setDescription("6 месяцев обучения цифровым телефонным сетям (Москва)");
        o4.setPeriods(List.of(p4));

        Organization o5 = new Organization();
        o5.setName("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики");
        o5.setWebsite("http://www.ifmo.ru/");
        Period p5_1 = new Period();
        p5_1.setStartDate(LocalDate.of(1993, 9, 1));
        p5_1.setEndDate(LocalDate.of(1996, 7, 1));
        p5_1.setDescription("Аспирантура (программист С, С++)");
        Period p5_2 = new Period();
        p5_2.setStartDate(LocalDate.of(1987, 9, 1));
        p5_2.setEndDate(LocalDate.of(1993, 7, 1));
        p5_2.setDescription("Инженер (программист Fortran, C)");
        o5.setPeriods(List.of(p5_1, p5_2));

        Organization o6 = new Organization();
        o6.setName("Заочная физико-техническая школа при МФТИ");
        o6.setWebsite("https://mipt.ru/");
        Period p6 = new Period();
        p6.setStartDate(LocalDate.of(1984, 9, 1));
        p6.setEndDate(LocalDate.of(1987, 6, 1));
        p6.setDescription("Закончил с отличием");
        o6.setPeriods(List.of(p6));

        education.setContent(List.of(o1, o2, o3, o4, o5, o6));
    }

    private static void printResume(Resume resume) {
        System.out.println(resume.getFullName());
        System.out.println(" ------ \n");

        resume.getContacts().forEach((k, v) -> {
            System.out.println(k.getTitle() + ": " + v);
        });
        System.out.println("\n");

        resume.getSections()
                .keySet()
                .stream()
                .sorted(Comparator.comparing(SectionType::ordinal))
                .forEach(k -> {
                    System.out.println(k.getTitle());
                    System.out.println(" ------ ");
                    System.out.println(resume.getSections().get(k));
                    System.out.println("\n");
                });
    }

}
