package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.serializers.ObjectStreamSerializer;
import ru.javawebinar.basejava.serializers.SerializeProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final SerializeProcessor serializeProcessor;

    protected FileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
        this.serializeProcessor = new SerializeProcessor(new ObjectStreamSerializer());
    }

    @Override
    protected void processUpdate(File file, Resume r) {
        try {
            serializeProcessor.executeWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", file.getName(), e);
        }
    }

    @Override
    protected void processSave(File file, Resume r) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        processUpdate(file, r);
    }

    @Override
    protected Resume processGet(File file) {
        try {
            return serializeProcessor.executeRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected void processDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Error file delete", file.getName());
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> resumes = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error read directory", null);
        }
        for (File file: files) {
            resumes.add(processGet(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error read directory", null);
        }
        for (File file : files) {
            processDelete(file);
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Error read directory", null);
        }
        return files.length;
    }

}
