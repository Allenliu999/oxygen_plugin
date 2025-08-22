package com.example.oxygenplugin.version;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 版本号管理器，负责升级 map 文件中的 <edition value="x.x"/>。
 */
public class VersionManager {
    /**
     * 升级 map 文件版本号，只升一次。
     * @param mapDoc map 的 DOM 文档对象
     * @return 新版本号
     */
    public String upgradeEdition(Document mapDoc) {
        Element bookid = (Element) mapDoc.getElementsByTagName("bookid").item(0);
        if (bookid != null) {
            Element edition = (Element) bookid.getElementsByTagName("edition").item(0);
            if (edition != null) {
                String value = edition.getAttribute("value");
                String newValue = incrementVersion(value);
                edition.setAttribute("value", newValue);
                return newValue;
            }
        }
        return null;
    }

    /**
     * 版本号自增（如1.5→1.6）。
     */
    private String incrementVersion(String value) {
        try {
            double v = Double.parseDouble(value);
            v += 0.1;
            return String.format("%.1f", v);
        } catch (Exception e) {
            return value;
        }
    }
}
