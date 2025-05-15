package ru.javawebinar.basejava.serializers;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializeProcessor {
    private ResumeSerializer resumeSerializer;

    public SerializeProcessor() {
    }

    public SerializeProcessor(ResumeSerializer resumeSerializer) {
        this.resumeSerializer = resumeSerializer;
    }

    public void setResumeSerializer(ResumeSerializer resumeSerializer) {
        this.resumeSerializer = resumeSerializer;
    }

    public void executeWrite(Resume r, OutputStream os) throws IOException {
        resumeSerializer.doWrite(r, os);
    }

    public Resume executeRead(InputStream is) throws IOException {
        return resumeSerializer.doRead(is);
    }

}
