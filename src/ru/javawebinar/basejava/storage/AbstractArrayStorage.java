package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractArrayStorage extends AbstractStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public List<Resume> getAllSorted() {
        return Stream.of(Arrays.copyOfRange(storage, 0, size))
                .sorted(RESUME_COMPARATOR)
                .toList();
    }

    public int size() {
        return size;
    }

    @Override
    protected final void processUpdate(Object searchKey, Resume r) {
        storage[(int) searchKey] = r;
    }

    @Override
    protected final void processSave(Object searchKey, Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        insertElement(r, -((int) searchKey + 1));
        size++;
    }

    @Override
    protected final Resume processGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected final void processDelete(Object searchKey) {
        removeElement((int) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void removeElement(int index);
}
