package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getAll() {
        return storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void processUpdate(Integer searchKey, Resume r) {
        storage.set(searchKey, r);
    }

    @Override
    protected void processSave(Integer searchKey, Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume processGet(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void processDelete(Integer searchKey) {
        storage.remove(searchKey.intValue());
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }
}
