package com.mongo.morphiadeprecationplugin.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class RefactorDaoAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    BrowserUtil.browse("https://stackoverflow.com/questions/ask");
  }
}
