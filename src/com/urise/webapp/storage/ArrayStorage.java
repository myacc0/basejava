package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        int index = indexOf(r.getUuid());
        if (index != -1) {
            System.out.println("Resume already exists! uuid: " + r.getUuid());
        } else if (size == storage.length) {
            System.out.println("Storage overflow");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public void update(Resume r) {
        int index = indexOf(r.getUuid());
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + r.getUuid());
        } else {
            storage[index] = r;
        }
    }

    public Resume get(String uuid) {
        int index = indexOf(uuid);
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + uuid);
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = indexOf(uuid);
        if (index == -1) {
            System.out.println("Resume not found! uuid: " + uuid);
        } else {
            size--;
            storage[index] = storage[size];
            storage[size] = null;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        System.arraycopy(storage, 0, resumes, 0, size);
        return resumes;
    }

    public int size() {
        return size;
    }

    private int indexOf(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
