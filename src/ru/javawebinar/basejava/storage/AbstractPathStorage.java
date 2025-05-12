package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private final Path directory;

    public AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected void processUpdate(Path path, Resume r) {
        try {
            doWrite(r, path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void processSave(Path path, Resume r) {
        try {
            doWrite(r, path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume processGet(Path path) {
        try {
            return doRead(path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void processDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Error file delete", path.getFileName().toString());
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected List<Resume> getAll() {
        try (Stream<Path> items = Files.list(directory)) {
            return items.map(this::processGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Error read directory", null);
        }
    }

    @Override
    public void clear() {
        try(Stream<Path> items = Files.list(directory)) {
            items.forEach(this::processDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try(Stream<Path> items = Files.list(directory)) {
            return Long.valueOf(items.count()).intValue();
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    protected abstract void doWrite(Resume r, Path path) throws IOException;

    protected abstract Resume doRead(Path path) throws IOException;
}
