package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void processUpdate(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
    }

    @Override
    protected void processSave(Object searchKey, Resume r) {
        storage.put((String) searchKey, r);
    }

    @Override
    protected Resume processGet(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void processDelete(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected Object findIndex(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }
}
