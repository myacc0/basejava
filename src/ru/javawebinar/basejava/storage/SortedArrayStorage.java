package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer findIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey, Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected void insertElement(Resume r, int index) {
        System.arraycopy(storage, index, storage, index + 1, (size - index));
        storage[index] = r;
    }

    @Override
    protected void removeElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, (size - index));
    }
}
