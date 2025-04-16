package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void processUpdate(Object searchKey, Resume r) {
        storage.set((int) searchKey, r);
    }

    @Override
    protected void processSave(Object searchKey, Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume processGet(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected void processDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Integer findIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }
}
