package storage;

import exceptions.BobException;

public interface IStorage<T> {
    T load() throws BobException;

    void save(T item) throws BobException;
}
