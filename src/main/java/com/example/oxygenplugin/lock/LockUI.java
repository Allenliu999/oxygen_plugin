package com.example.oxygenplugin.lock;

import com.example.oxygenplugin.ui.DialogUtils;
import javax.swing.*;
import java.util.List;

/**
 * 工具栏按钮和弹窗交互实现文档加锁/解锁。
 */
public class LockUI {
    private final LockManager lockManager = new LockManager();

    /**
     * 弹窗选择 map 文件并加锁/解锁（供主菜单栏调用）
     */
    public void showLockDialog(java.util.List<java.io.File> mapList) {
        java.util.List<String> mapNameList = new java.util.ArrayList<>();
        for (java.io.File f : mapList) {
            mapNameList.add(f.getAbsolutePath());
        }
        String selectedMap = DialogUtils.selectMapDialog(mapNameList);
        if (selectedMap == null) {
            JOptionPane.showMessageDialog(null, "未选择 map，操作取消");
            return;
        }
        if (lockManager.isLocked(selectedMap)) {
            if (lockManager.unlock(selectedMap)) {
                JOptionPane.showMessageDialog(null, "map 已解锁: " + selectedMap);
            }
        } else {
            if (lockManager.lock(selectedMap)) {
                JOptionPane.showMessageDialog(null, "map 已加锁: " + selectedMap);
            }
        }
    }
}
