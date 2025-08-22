package com.example.oxygenplugin;

import com.example.oxygenplugin.lock.LockUI;
import ro.sync.exml.plugin.Plugin;
import ro.sync.exml.plugin.PluginDescriptor;
import ro.sync.exml.workspace.api.PluginWorkspace;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.editor.WSEditor;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorEditorPage;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorListener;
import ro.sync.ecss.extensions.api.node.AuthorDocument;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 插件主类，注册扩展点、监听器和 UI。
 */
/**
 * Oxygen 插件主类，需显式调用父类构造方法。
 */
public class MainExtensionBundle extends Plugin {
    private static final Logger LOGGER = Logger.getLogger(MainExtensionBundle.class.getName());
    private LockUI lockUI;
    private List<File> mapList;
    private PluginWorkspace workspace;
    // 记录已自动升级版本的 map（绝对路径），避免重复升级
    private final Set<String> upgradedMaps = new HashSet<>();

    public MainExtensionBundle(PluginDescriptor descriptor) {
        super(descriptor);
    }

    /**
     * 插件初始化入口，递归扫描 map 文件，注册菜单和监听器
     */
    public void applicationStarted(PluginDescriptor pluginDescriptor) {
        // 1. 递归扫描 map 文件
        mapList = scanMapFiles(new File("."));
        // 2. 注册加锁/解锁菜单（兼容所有 Oxygen 版本，直接操作主窗口菜单栏）
        lockUI = new LockUI();
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame mainFrame = (javax.swing.JFrame) javax.swing.JOptionPane.getRootFrame();
            if (mainFrame != null) {
                javax.swing.JMenuBar menuBar = mainFrame.getJMenuBar();
                if (menuBar != null) {
                    javax.swing.JMenu customMenu = new javax.swing.JMenu("文档锁定");
                    javax.swing.JMenuItem lockItem = new javax.swing.JMenuItem("加锁/解锁");
                    lockItem.addActionListener(e -> lockUI.showLockDialog(mapList));
                    customMenu.add(lockItem);
                    menuBar.add(customMenu);
                    menuBar.revalidate();
                    menuBar.repaint();
                }
            }
        });
        // 3. 注册编辑器监听器，监听 topic 修改
        workspace.addEditorChangeListener(new ro.sync.exml.workspace.api.listeners.WSEditorChangeListener() {
            @Override
            public boolean editorAboutToBeOpenedVeto(java.net.URL url) { return true; }
            @Override
            public void editorAboutToBeOpened(java.net.URL url) {}
            @Override
            public void editorOpened(java.net.URL url) {
                WSEditor editor = (WSEditor) workspace.getEditorAccess(url, PluginWorkspace.MAIN_EDITING_AREA);
                if (editor != null) {
                    LOGGER.info("[OxygenPlugin] editorOpened: " + url);
                    if (isMapFile(url)) {
                        autoUpgradeMapVersionOnce(url);
                    }
                    registerTopicEditListener(editor, url);
                }
            }
            @Override public void editorClosed(java.net.URL url) {}
            @Override public void editorSelected(java.net.URL url) {}
            @Override public void editorPageChanged(java.net.URL url) {}
            @Override public void editorActivated(java.net.URL url) {}
            @Override public void editorDeactivated(java.net.URL url) {}
            @Override public boolean editorAboutToBeClosed(java.net.URL url) { return true; }
            @Override public void editorRelocated(java.net.URL from, java.net.URL to) {}
            @Override public boolean editorsAboutToBeClosed(java.net.URL[] urls) { return true; }
        }, PluginWorkspace.MAIN_EDITING_AREA);
    }

    /**
     * 判断 url 是否为 map 文件
     */
    private boolean isMapFile(java.net.URL url) {
        String path = url.getPath().toLowerCase();
        return path.endsWith(".ditamap") || path.endsWith(".xml");
    }

    /**
     * 自动升级 map 版本号（只升一次，线程安全，避免重复升级）
     */
    /**
     * 自动升级 map 版本号（只升一次，线程安全，避免重复升级）
     * @param url map 文件 URL
     */
    private synchronized void autoUpgradeMapVersionOnce(java.net.URL url) {
        try {
            File file = new File(url.toURI());
            String absPath = file.getAbsolutePath();
            if (upgradedMaps.contains(absPath)) {
                LOGGER.info("[OxygenPlugin] map 版本号已升级过，跳过: " + absPath);
                return;
            }
            String content = java.nio.file.Files.readString(file.toPath());
            // 简单正则查找 <edition value="x.x"/>
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("(<edition\\s+value=\")([0-9]+\\.[0-9]+)(\"/?>)");
            java.util.regex.Matcher m = p.matcher(content);
            if (m.find()) {
                String oldVersion = m.group(2);
                String[] parts = oldVersion.split("\\.");
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                minor += 1;
                String newVersion = major + "." + minor;
                String newContent = m.replaceFirst("$1" + newVersion + "$3");
                java.nio.file.Files.writeString(file.toPath(), newContent);
                upgradedMaps.add(absPath);
                LOGGER.info("[OxygenPlugin] map 版本号已自动升级: " + oldVersion + " -> " + newVersion + " (" + absPath + ")");
            } else {
                LOGGER.warning("[OxygenPlugin] 未找到 <edition value=.../> 节点: " + absPath);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[OxygenPlugin] map 版本号自动升级失败: " + url, e);
        }
    }

    /**
     * 注册 topic 修改监听器，实现 topic 修改时自动升级 map 版本号（只升一次，线程安全）
     */
    /**
     * 注册 topic 修改监听器，实现 topic 修改时自动升级 map 版本号（只升一次，线程安全）
     * @param editor 当前编辑器
     * @param url map 文件 URL
     */
    private void registerTopicEditListener(WSEditor editor, java.net.URL url) {
        if (editor == null) return;
        if (editor.getCurrentPage() instanceof WSAuthorEditorPage) {
            WSAuthorEditorPage authorPage = (WSAuthorEditorPage) editor.getCurrentPage();
            AuthorDocumentController docController = authorPage.getDocumentController();
            docController.addAuthorListener(new AuthorListener() {
                @Override
                public void documentChanged(AuthorDocument oldDoc, AuthorDocument newDoc) {
                    // 只升级一次
                    autoUpgradeMapVersionOnce(url);
                }
                // 其它 AuthorListener 方法留空实现
                @Override public void beforeContentDelete(ro.sync.ecss.extensions.api.DocumentContentDeletedEvent e) {}
                @Override public void beforeAttributeChange(ro.sync.ecss.extensions.api.AttributeChangedEvent e) {}
                @Override public void beforeContentInsert(ro.sync.ecss.extensions.api.DocumentContentInsertedEvent e) {}
                @Override public void beforeDoctypeChange() {}
                @Override public void beforeAuthorNodeStructureChange(ro.sync.ecss.extensions.api.node.AuthorNode n) {}
                @Override public void beforeAuthorNodeNameChange(ro.sync.ecss.extensions.api.node.AuthorNode n) {}
                @Override public void attributeChanged(ro.sync.ecss.extensions.api.AttributeChangedEvent e) {}
                @Override public void authorNodeNameChanged(ro.sync.ecss.extensions.api.node.AuthorNode n) {}
                @Override public void authorNodeStructureChanged(ro.sync.ecss.extensions.api.node.AuthorNode n) {}
                @Override public void contentDeleted(ro.sync.ecss.extensions.api.DocumentContentDeletedEvent e) {}
                @Override public void contentInserted(ro.sync.ecss.extensions.api.DocumentContentInsertedEvent e) {}
                @Override public void doctypeChanged() {}
            });
            LOGGER.info("[OxygenPlugin] 已为 " + url + " 注册 Author API 文档监听");
        }
    }

    // 伪实现已删除，保留真实实现



    // Oxygen 26+ 插件主类无需实现 stop 方法，如需资源释放可自定义

    /**
     * 递归扫描项目下所有 map 文件（.ditamap/.xml），返回文件列表
     * @param dir 起始目录
     * @return map 文件列表
     */
    private List<File> scanMapFiles(File dir) {
        List<File> maps = new ArrayList<>();
        if (dir == null || !dir.exists()) return maps;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    maps.addAll(scanMapFiles(f));
                } else if (f.getName().endsWith(".ditamap") || f.getName().endsWith(".xml")) {
                    maps.add(f);
                }
            }
        }
        return maps;
    }


}
