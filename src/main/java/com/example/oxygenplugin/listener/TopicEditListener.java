package com.example.oxygenplugin.listener;

import ro.sync.ecss.extensions.api.AuthorListenerAdapter;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import com.example.oxygenplugin.version.VersionManager;
import org.w3c.dom.Document;
import java.util.Set;
import java.util.HashSet;

/**
 * 监听 topic 修改，自动升级 map 版本号（只升一次）。
 */
public class TopicEditListener extends AuthorListenerAdapter {
    private boolean versionUpgraded = false;
    private final VersionManager versionManager = new VersionManager();
    private final Set<String> modifiedTopics = new HashSet<>();
    private final Document mapDoc;

    public TopicEditListener(Document mapDoc) {
        this.mapDoc = mapDoc;
    }

    @Override
    public void authorNodeNameChanged(AuthorNode node) {
        if (isTopicNode(node)) {
            modifiedTopics.add(node.getName());
            if (!versionUpgraded) {
                versionManager.upgradeEdition(mapDoc);
                versionUpgraded = true;
            }
        }
    }

    private boolean isTopicNode(AuthorNode node) {
        return node.getType() == AuthorNode.NODE_TYPE_ELEMENT && "topic".equals(node.getName());
    }

    public Set<String> getModifiedTopics() {
        return modifiedTopics;
    }
}
