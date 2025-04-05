import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size = size();

    void clear() {
        Arrays.fill(storage, 0, size, null);
    }

    void save(Resume r) {
        storage[size++] = r;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid))
                return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        int offset = 0;
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                offset = (i < size - 1) ? 1 : 0;
                modified = true;
                size--;
            }
            storage[i] = storage[i + offset];
        }
        if (modified)
            storage[size] = null;
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
        int resumeCount = 0;
        for (Resume resume : storage) {
            if (resume == null) break;
            resumeCount++;
        }
        return resumeCount;
    }
}
