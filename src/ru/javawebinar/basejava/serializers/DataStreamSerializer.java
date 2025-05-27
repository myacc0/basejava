package ru.javawebinar.basejava.serializers;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.*;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements ResumeSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> e: contacts.entrySet()) {
                dos.writeUTF(e.getKey().name());
                dos.writeUTF(e.getValue());
            }

            dos.writeUTF(((TextSection) r.getSections().get(SectionType.OBJECTIVE)).getContent());
            dos.writeUTF(((TextSection) r.getSections().get(SectionType.PERSONAL)).getContent());

            writeListSection((ListSection) r.getSections().get(SectionType.ACHIEVEMENT), dos);
            writeListSection((ListSection) r.getSections().get(SectionType.QUALIFICATIONS), dos);

            writeOrganizationSection((OrganizationSection) r.getSections().get(SectionType.EXPERIENCE), dos);
            writeOrganizationSection((OrganizationSection) r.getSections().get(SectionType.EDUCATION), dos);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactsSize = dis.readInt();
            for (int i = 0 ; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            resume.getSections().put(SectionType.OBJECTIVE, new TextSection(dis.readUTF()));
            resume.getSections().put(SectionType.PERSONAL, new TextSection(dis.readUTF()));

            resume.getSections().put(SectionType.ACHIEVEMENT, readListSection(dis));
            resume.getSections().put(SectionType.QUALIFICATIONS, readListSection(dis));

            resume.getSections().put(SectionType.EXPERIENCE, readOrganizationSection(dis));
            resume.getSections().put(SectionType.EDUCATION, readOrganizationSection(dis));
            return resume;
        }
    }

    private void writeListSection(ListSection section, DataOutputStream dos) throws IOException {
        List<String> items = section.getContent();
        dos.writeInt(items.size());
        for (String i: items) {
            dos.writeUTF(i);
        }
    }

    private ListSection readListSection(DataInputStream dis) throws IOException {
        int itemsSize = dis.readInt();
        ListSection section = new ListSection();
        for (int i = 0 ; i < itemsSize; i++) {
            section.getContent().add(dis.readUTF());
        }
        return section;
    }

    private void writeOrganizationSection(OrganizationSection section, DataOutputStream dos) throws IOException {
        List<Organization> items = section.getContent();
        dos.writeInt(items.size());
        for (Organization i: items) {
            dos.writeUTF(i.getName());
            dos.writeUTF(i.getWebsite());

            dos.writeInt(i.getPeriods().size());
            for (Period p : i.getPeriods()) {
                dos.writeUTF(DateUtil.format(p.getStartDate()));
                dos.writeUTF(DateUtil.format(p.getEndDate()));
                dos.writeUTF(p.getDescription());
            }
        }
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis) throws IOException {
        int itemsSize = dis.readInt();
        OrganizationSection section = new OrganizationSection();
        for (int i = 0 ; i < itemsSize; i++) {
            Organization o = new Organization();
            o.setName(dis.readUTF());
            o.setWebsite(dis.readUTF());

            int periodsSize = dis.readInt();
            for (int j = 0; j < periodsSize; j++) {
                Period p = new Period();
                p.setStartDate(DateUtil.parse(dis.readUTF()));
                p.setEndDate(DateUtil.parse(dis.readUTF()));
                p.setDescription(dis.readUTF());
                o.getPeriods().add(p);
            }
            section.getContent().add(o);
        }
        return section;
    }

}
