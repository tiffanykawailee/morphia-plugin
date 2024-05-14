package com.mongo.morphiadeprecationplugin.action;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiImportStatementBase;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiType;
import com.intellij.ui.content.Content;
import com.mongo.morphiadeprecationplugin.util.PsiElementUtil;
import java.util.Arrays;
import java.util.Optional;

@Service
public final class RefactorDaoSvc {

  private static final Logger LOG = Logger.getInstance(RefactorDaoSvc.class);

  public void removeMorphiaImports(final Project project, final PsiJavaFile psiJavaFile) {
    ConsoleView consoleView = activateToolWindowAndGetConsoleView(project);
    ToolWindowManager.getInstance(project).invokeLater(() -> {
      WriteCommandAction.runWriteCommandAction(project, () -> {
        PsiImportStatementBase[] allImportStatements = psiJavaFile.getImportList()
            .getAllImportStatements();
        for (PsiImportStatementBase psiImportStatementBase : allImportStatements) {
          // Simple method to just find references to morphia and remove
          if (psiImportStatementBase.getImportReference() == null) {
            consoleView.print("Import was null for some reason?", ConsoleViewContentType.ERROR_OUTPUT);
            continue;
          }
          if (psiImportStatementBase.getImportReference() != null
              && (psiImportStatementBase.getImportReference().getText().contains("morphia") || psiImportStatementBase.getImportReference().getText().contains("com.xgen.svc.core.dao.base"))) {
            consoleView.print("Removing import: " + psiImportStatementBase.getImportReference().getText() + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
            psiImportStatementBase.delete();
          }
        }
      });
    });
  }

  public void extendNewBaseDao(final Project project, final PsiJavaFile psiJavaFile, final String modelClass) {
    PsiElementUtil psiElementUtil = new PsiElementUtil(project);
    Optional<PsiClass> psiClass = Arrays.stream(psiJavaFile.getClasses()).findFirst();
    if (psiClass.isEmpty()) {
      LOG.error("Unable to get PsiClass for " + psiJavaFile.getName());
      return;
    }
    ConsoleView consoleView = activateToolWindowAndGetConsoleView(project);
    ToolWindowManager.getInstance(project).invokeLater(() -> {
      WriteCommandAction.runWriteCommandAction(project, () -> {
        consoleView.print("Adding new import for BaseDao<T>", ConsoleViewContentType.NORMAL_OUTPUT);
        PsiImportStatement baseDaoImportStatement = psiElementUtil.createBaseDaoImportStatement();
        psiJavaFile.getImportList().add(baseDaoImportStatement);
        consoleView.print("Adding generic for BaseDao and changing extension", ConsoleViewContentType.NORMAL_OUTPUT);
        PsiJavaCodeReferenceElement psiJavaCodeReferenceElement = psiElementUtil.createPsiReference(modelClass);
        PsiType[] typeParameters = psiJavaCodeReferenceElement.getTypeParameters();

        PsiClassType[] extendsListTypes = psiClass.get().getExtendsListTypes();
        for (PsiClassType extendsListType : extendsListTypes) {
        }
      });
    });
  }

  private ConsoleView activateToolWindowAndGetConsoleView(final Project project) {
    ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Dao Refactor");
    ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
    Content content = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Dao Refactor Progress", false);
    toolWindow.getContentManager().addContent(content);
    toolWindow.activate(null);
    return consoleView;
  }

}
