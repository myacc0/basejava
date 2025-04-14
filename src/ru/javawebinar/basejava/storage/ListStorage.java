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
    protected void processUpdate(int index, Resume r) {
        storage.set(index, r);
    }

    @Override
    protected void processSave(int index, Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume processGet(int index) {
        return storage.get(index);
    }

    @Override
    protected void processDelete(int index) {
        storage.remove(index);
    }

    @Override
    protected int findIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
