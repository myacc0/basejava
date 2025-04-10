package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        Resume r = new Resume("uuid3");
        storage.update(r);

        Resume updatedR = storage.get(r.getUuid());
        Assert.assertNotNull(updatedR);
        Assert.assertEquals(updatedR, r);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        Resume r = new Resume("dummy");
        storage.update(r);
    }

    @Test
    public void save() {
        int sizeBefore = storage.size();
        Resume r = new Resume("uuid4");
        storage.save(r);

        Resume savedResume = storage.get(r.getUuid());
        Assert.assertEquals(sizeBefore + 1, storage.size());
        Assert.assertNotNull(savedResume);
        Assert.assertEquals(savedResume, r);
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExist() {
        Resume r = new Resume("uuid3");
        storage.save(r);
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        Resume r = new Resume("uuid4");
        try {
            storage.save(r);
            Assert.fail("Storage overflow before expected!");
        } catch (AssertionError e) {
            throw new StorageException(e.getMessage(), r.getUuid());
        }
    }

    @Test
    public void get() {
        String uuid = "uuid3";
        Resume r = storage.get(uuid);
        Assert.assertNotNull(r);
        Assert.assertEquals(r.getUuid(), uuid);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void delete() {
        int sizeBefore = storage.size();
        String uuid = "uuid3";
        storage.delete(uuid);

        Assert.assertEquals(sizeBefore - 1, storage.size());
        Assert.assertThrows(NotExistStorageException.class, () -> storage.get(uuid));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertNotNull(resumes);
        Assert.assertEquals(storage.size(), resumes.length);
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }
}