package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        for (int i = size; i > index; i--) {
            storage[i] = storage[i - 1];
        }
        storage[index] = r;
    }

    @Override
    protected void removeElement(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
    }
}
