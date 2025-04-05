import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    void clear() {
        Arrays.fill(storage, 0, size, null);
    }

    void save(Resume r) {
        storage[size++] = r;
    }

    Resume get(String uuid) {
        int index = indexOf(uuid);
        return (index == -1) ? null : storage[index];
    }

    void delete(String uuid) {
        int index = indexOf(uuid);
        if (index != -1) {
            storage[index] = storage[--size];
            storage[size] = null;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        System.arraycopy(storage, 0, resumes, 0, size);
        return resumes;
    }

    int size() {
        if (size == 0) {
            for (Resume resume : storage) {
                if (resume == null) break;
                size++;
            }
        }
        return size;
    }

    private int indexOf(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
