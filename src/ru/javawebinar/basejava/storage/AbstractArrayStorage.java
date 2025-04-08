package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = findIndex(r.getUuid());
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + r.getUuid());
        } else {
            storage[index] = r;
        }
    }

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("Storage overflow");
            return;
        }

        int index = findIndex(r.getUuid());
        if (index >= 0) {
            System.out.println("Resume already exists! uuid: " + r.getUuid());
        } else {
            insertElement(r, -(index + 1));
            size++;
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + uuid);
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + uuid);
        } else {
            removeElement(index);
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    protected abstract int findIndex(String uuid);

    protected abstract void insertElement(Resume r, int index);

    protected abstract void removeElement(int index);
}
