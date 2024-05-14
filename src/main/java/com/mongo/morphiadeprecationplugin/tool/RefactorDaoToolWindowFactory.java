package com.mongo.morphiadeprecationplugin.tool;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class RefactorDaoToolWindowFactory implements ToolWindowFactory {

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
    Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Dao Refactor Progress", false);
    toolWindow.getContentManager().addContent(content);
  }
}
