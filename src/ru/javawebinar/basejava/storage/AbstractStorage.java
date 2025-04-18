package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final void update(Resume r) {
        processUpdate(getExistingSearchKey(r.getUuid()), r);
    }

    public final void save(Resume r) {
        processSave(getNotExistingSearchKey(r.getUuid()), r);
    }

    public final Resume get(String uuid) {
        return processGet(getExistingSearchKey(uuid));
    }

    public final void delete(String uuid) {
        processDelete(getExistingSearchKey(uuid));
    }

    private Object getExistingSearchKey(String uuid) {
        Object searchKey = findIndex(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = findIndex(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void processUpdate(Object searchKey, Resume r);

    protected abstract void processSave(Object searchKey, Resume r);

    protected abstract Resume processGet(Object searchKey);

    protected abstract void processDelete(Object searchKey);

    protected abstract Object findIndex(String uuid);

    protected abstract boolean isExist(Object searchKey);
}
