package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializers.ObjectStreamSerializer;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}
