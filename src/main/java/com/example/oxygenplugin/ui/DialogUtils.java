package com.example.oxygenplugin.ui;

import javax.swing.*;
import java.util.List;

/**
 * 弹窗工具类，支持 map 选择、到期提交提醒等。
 */
public class DialogUtils {
    /**
     * 让用户选择要解锁的 map 文件。
     * @param mapList map 文件名列表
     * @return 用户选择的 map 文件名
     */
    public static String selectMapDialog(List<String> mapList) {
        Object[] options = mapList.toArray();
        return (String) JOptionPane.showInputDialog(
                null,
                "请选择要解锁的 map：",
                "解锁 map",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options.length > 0 ? options[0] : null
        );
    }

    /**
     * 到期统一提交提醒。
     */
    public static void showCommitReminder() {
        JOptionPane.showMessageDialog(null, "文档修改已到期，请统一提交！", "提交提醒", JOptionPane.INFORMATION_MESSAGE);
    }
}
