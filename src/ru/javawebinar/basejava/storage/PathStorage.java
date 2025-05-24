package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.serializers.ResumeSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final ResumeSerializer resumeSerializer;

    public PathStorage(String dir, ResumeSerializer resumeSerializer) {
        Objects.requireNonNull(dir, "directory must not be null");
        this.directory = Paths.get(dir);
        this.resumeSerializer = resumeSerializer;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected void processUpdate(Path path, Resume r) {
        try {
            resumeSerializer.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void processSave(Path path, Resume r) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + path, path.getFileName().toString(), e);
        }
        processUpdate(path, r);
    }

    @Override
    protected Resume processGet(Path path) {
        try {
            return resumeSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void processDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Error file delete", path.getFileName().toString(), e);
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
        return getDirectoryItems().stream().map(this::processGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getDirectoryItems().forEach(this::processDelete);
    }

    @Override
    public int size() {
        return getDirectoryItems().size();
    }

    private List<Path> getDirectoryItems() {
        try (Stream<Path> items = Files.list(directory)) {
            return items.collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Error read directory", null, e);
        }
    }
}
