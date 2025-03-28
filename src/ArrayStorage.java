import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        storage[size()] = r;
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume == null) break;
            if (resume.uuid.equals(uuid))
                return resume;
        }
        return null;
    }

    void delete(String uuid) {
        Resume[] resumes = getAll();
        clear();
        int offset = 0;
        for (int i = 0; i < resumes.length; i++) {
            if (resumes[i].uuid.equals(uuid)) {
                offset += 1;
                continue;
            }
            storage[i - offset] = resumes[i];
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[size()];
        System.arraycopy(storage, 0, resumes, 0, resumes.length);
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
