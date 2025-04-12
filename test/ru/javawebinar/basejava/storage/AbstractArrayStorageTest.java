package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String DUMMY = "dummy";

    private static final Resume RESUME1 = new Resume(UUID_1);
    private static final Resume RESUME2 = new Resume(UUID_2);
    private static final Resume RESUME3 = new Resume(UUID_3);
    private static final Resume RESUME4 = new Resume(UUID_4);
    private static final Resume RESUME_DUMMY = new Resume(DUMMY);

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME1);
        storage.save(RESUME2);
        storage.save(RESUME3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assert.assertArrayEquals(storage.getAll(), new Resume[]{});
    }

    @Test
    public void update() {
        storage.update(RESUME3);
        Assert.assertSame(RESUME3, storage.get(RESUME3.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_DUMMY);
    }

    @Test
    public void save() {
        storage.save(RESUME4);
        assertGet(RESUME4);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveAlreadyExist() {
        storage.save(RESUME3);
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow() {
        storage.clear();
        for (int i = 0; i < STORAGE_LIMIT; i++) {
            try {
                storage.save(new Resume("UUID" + i));
            } catch (StorageException e) {
                Assert.fail("Storage overflow before expected!");
            }
        }
        storage.save(RESUME_DUMMY);
    }

    @Test
    public void get() {
        assertGet(RESUME1);
        assertGet(RESUME2);
        assertGet(RESUME3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(DUMMY);
    }

    @Test
    public void delete() {
        storage.delete(UUID_3);
        assertSize(2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test
    public void getAll() {
        Resume[] expected = new Resume[] {RESUME1, RESUME2, RESUME3};
        Assert.assertArrayEquals(expected, storage.getAll());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    private void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }
}