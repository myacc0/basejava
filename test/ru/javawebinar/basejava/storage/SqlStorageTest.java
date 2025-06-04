package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.StorageException;

public class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(
                new SqlStorage(
                        Config.get().getDbUrl(),
                        Config.get().getDbUser(),
                        Config.get().getDbPassword()));
    }

    @Override
    @Test(expected = StorageException.class)
    public void saveAlreadyExist() {
        super.saveAlreadyExist();
    }
}
