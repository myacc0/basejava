package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    protected static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName)
            .thenComparing(Resume::getUuid);

    public final void update(Resume r) {
        LOG.info("Update " + r);
        processUpdate(getExistingSearchKey(r.getUuid()), r);
    }

    public final void save(Resume r) {
        LOG.info("Save " + r);
        processSave(getNotExistingSearchKey(r.getUuid()), r);
    }

    public final Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return processGet(getExistingSearchKey(uuid));
    }

    public final void delete(String uuid) {
        LOG.info("Delete " + uuid);
        processDelete(getExistingSearchKey(uuid));
    }

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> resumes = getAll();
        resumes.sort(RESUME_COMPARATOR);
        return resumes;
    }

    private SK getExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void processUpdate(SK searchKey, Resume r);

    protected abstract void processSave(SK searchKey, Resume r);

    protected abstract Resume processGet(SK searchKey);

    protected abstract void processDelete(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExist(SK searchKey);

    protected abstract List<Resume> getAll();
}
