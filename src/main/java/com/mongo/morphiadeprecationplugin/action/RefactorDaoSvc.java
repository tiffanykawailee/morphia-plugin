package com.mongo.morphiadeprecationplugin.action;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.RegisterToolWindowTask;
import com.intellij.openapi.wm.RegisterToolWindowTaskBuilder;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiImportStatementBase;
import com.intellij.psi.PsiJavaFile;
import com.intellij.ui.content.Content;
import java.util.concurrent.atomic.AtomicReference;

@Service
public final class RefactorDaoSvc {

  private static final Logger LOG = Logger.getInstance(RefactorDaoSvc.class);

  public void refactorImports(final Project project, final PsiJavaFile psiJavaFile) {
    ConsoleView consoleView = activateToolWindowAndGetConsoleView(project);
    ToolWindowManager.getInstance(project).invokeLater(() -> {
      WriteCommandAction.runWriteCommandAction(project, () -> {
        PsiImportStatementBase[] allImportStatements = psiJavaFile.getImportList()
            .getAllImportStatements();
        for (PsiImportStatementBase psiImportStatementBase : allImportStatements) {
          // Simple method to just find references to morphia and remove
          if (psiImportStatementBase.getImportReference() != null
              && psiImportStatementBase.getImportReference().getText().contains("morphia")) {
            consoleView.print("Removing " + psiImportStatementBase.getImportReference().getText() + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
            psiImportStatementBase.delete();
          }
        }
      });
    });
  }

  private ConsoleView activateToolWindowAndGetConsoleView(final Project project) {
    ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Dao Refactor");
    toolWindow.activate(null);
    return TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
  }

}
