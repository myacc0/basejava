package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void processUpdate(String searchKey, Resume r) {
        storage.put(searchKey, r);
    }

    @Override
    protected void processSave(String searchKey, Resume r) {
        storage.put(searchKey, r);
    }

    @Override
    protected Resume processGet(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void processDelete(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }
}
