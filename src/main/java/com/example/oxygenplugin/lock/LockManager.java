package com.example.oxygenplugin.lock;

import java.util.HashSet;
import java.util.Set;

/**
 * 文档加锁/解锁管理器。
 * 通过生成.lock文件或元数据实现加锁，支持多用户并发控制。
 */
public class LockManager {
    private final Set<String> lockedDocs = new HashSet<>();

    /**
     * 加锁文档。
     * @param docPath 文档路径
     * @return 是否加锁成功
     */
    public boolean lock(String docPath) {
        // TODO: 生成.lock文件或写入元数据
        return lockedDocs.add(docPath);
    }

    /**
     * 解锁文档。
     * @param docPath 文档路径
     * @return 是否解锁成功
     */
    public boolean unlock(String docPath) {
        // TODO: 删除.lock文件或清除元数据
        return lockedDocs.remove(docPath);
    }

    /**
     * 检查文档是否已加锁。
     */
    public boolean isLocked(String docPath) {
        return lockedDocs.contains(docPath);
    }
}
