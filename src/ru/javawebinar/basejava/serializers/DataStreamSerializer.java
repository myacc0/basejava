package ru.javawebinar.basejava.serializers;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataStreamSerializer implements ResumeSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            /*
            writeCollection(dos, r.getContacts().entrySet(), (e) -> {
                dos.writeUTF(e.getKey().name());
                dos.writeUTF(e.getValue());
            });

            dos.writeUTF(((TextSection) r.getSections().get(SectionType.OBJECTIVE)).getContent());
            dos.writeUTF(((TextSection) r.getSections().get(SectionType.PERSONAL)).getContent());

            writeCollection(dos, ((ListSection) r.getSections().get(SectionType.ACHIEVEMENT)).getContent(), dos::writeUTF);
            writeCollection(dos, ((ListSection) r.getSections().get(SectionType.QUALIFICATIONS)).getContent(), dos::writeUTF);

            writeCollection(dos,
                    ((OrganizationSection) r.getSections().get(SectionType.EXPERIENCE)).getContent(),
                    o -> writeOrganization(dos, o));

            writeCollection(dos,
                    ((OrganizationSection) r.getSections().get(SectionType.EDUCATION)).getContent(),
                    o -> writeOrganization(dos, o));
            */
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume r = new Resume(uuid, fullName);

            /*
            Set<Map.Entry<ContactType, String>> contactsEntrySet = new HashSet<>();
            readCollection(dis, contactsEntrySet, d -> Map.entry(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            contactsEntrySet.forEach(entry -> r.addContact(entry.getKey(), entry.getValue()));

            r.addSection(SectionType.OBJECTIVE, new TextSection(dis.readUTF()));
            r.addSection(SectionType.PERSONAL, new TextSection(dis.readUTF()));

            r.addSection(SectionType.ACHIEVEMENT, new ListSection());
            readCollection(dis, ((ListSection) r.getSections().get(SectionType.ACHIEVEMENT)).getContent(), DataInput::readUTF);

            r.addSection(SectionType.QUALIFICATIONS, new ListSection());
            readCollection(dis, ((ListSection) r.getSections().get(SectionType.QUALIFICATIONS)).getContent(), DataInput::readUTF);

            r.addSection(SectionType.EXPERIENCE, new OrganizationSection());
            readCollection(dis, ((OrganizationSection) r.getSections().get(SectionType.EXPERIENCE)).getContent(), this::readOrganization);

            r.addSection(SectionType.EDUCATION, new OrganizationSection());
            readCollection(dis, ((OrganizationSection) r.getSections().get(SectionType.EDUCATION)).getContent(), this::readOrganization);
             */
            return r;
        }
    }

    private interface CollectionItemWriter<T> {
        void write(T item) throws IOException;
    }

    private interface CollectionItemReader<T> {
        T read(DataInputStream dis) throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> c, CollectionItemWriter<T> writer) throws IOException {
        dos.writeInt(c.size());
        for (T item : c) {
            writer.write(item);
        }
    }

    private <T> void readCollection(DataInputStream dis, Collection<T> c, CollectionItemReader<T> reader) throws IOException {
        int itemsSize = dis.readInt();
        for (int i = 0; i < itemsSize; i++) {
            c.add(reader.read(dis));
        }
    }

    private void writeOrganization(DataOutputStream dos, Organization o) throws IOException {
        dos.writeUTF(o.getName());
        dos.writeUTF(o.getWebsite());
        writeCollection(dos, o.getPeriods(), p -> {
            dos.writeUTF(DateUtil.format(p.getStartDate()));
            dos.writeUTF(DateUtil.format(p.getEndDate()));
            dos.writeUTF(p.getDescription());
        });
    }

    private Organization readOrganization(DataInputStream dis) throws IOException {
        Organization o = new Organization();
        o.setName(dis.readUTF());
        o.setWebsite(dis.readUTF());
        readCollection(dis, o.getPeriods(), d -> {
            Period p = new Period();
            p.setStartDate(DateUtil.parse(dis.readUTF()));
            p.setEndDate(DateUtil.parse(dis.readUTF()));
            p.setDescription(dis.readUTF());
            return p;
        });
        return o;
    }

}
