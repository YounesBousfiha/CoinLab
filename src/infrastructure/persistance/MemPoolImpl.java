package infrastructure.persistance;

import domain.entity.Transaction;
import domain.repository.MempoolRepository;

import java.sql.Connection;

public class MemPoolImpl implements MempoolRepository {
    private final Connection db;

    public MemPoolImpl(Connection db) {
        this.db = db;
    }
}
