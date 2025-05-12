package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    public AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected void processUpdate(Path searchKey, Resume r) {

    }

    @Override
    protected void processSave(Path searchKey, Resume r) {

    }

    @Override
    protected Resume processGet(Path searchKey) {
        return null;
    }

    @Override
    protected void processDelete(Path searchKey) {

    }

    @Override
    protected Path getSearchKey(String uuid) {
        return null;
    }

    @Override
    protected boolean isExist(Path searchKey) {
        return false;
    }

    @Override
    protected List<Resume> getAll() {
        return List.of();
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::processDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
