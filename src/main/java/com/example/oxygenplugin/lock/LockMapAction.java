package com.example.oxygenplugin.lock;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;

import javax.swing.JOptionPane;

public class LockMapAction implements AuthorOperation {
    @Override
    public void doOperation(AuthorAccess authorAccess, ArgumentsMap arguments) throws AuthorOperationException {
        JOptionPane.showMessageDialog(null, "Map 加锁/解锁逻辑触发！（请补充实际业务）");
    }

    @Override
    public ArgumentDescriptor[] getArguments() {
        return new ArgumentDescriptor[0];
    }

    @Override
    public String getDescription() {
        return "对 DITA map 文件进行加锁/解锁操作";
    }
}
