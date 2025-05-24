package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializers.ObjectStreamSerializer;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getPath(), new ObjectStreamSerializer()));
    }
}
