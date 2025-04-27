package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> implements Storage {
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
    public List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    public int size() {
        return size;
    }

    @Override
    protected final void processUpdate(Integer searchKey, Resume r) {
        storage[searchKey] = r;
    }

    @Override
    protected final void processSave(Integer searchKey, Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", r.getUuid());
        }
        insertElement(r, -(searchKey + 1));
        size++;
    }

    @Override
    protected final Resume processGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected final void processDelete(Integer searchKey) {
        removeElement(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void removeElement(int index);
}
