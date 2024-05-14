package com.mongo.morphiadeprecationplugin.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;

public class RefactorDaoAction extends AnAction {

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    final PsiJavaFile psiFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
    if (editor == null || psiFile == null) {
      return;
    }
    String modelName = Messages.showInputDialog(
        "Provide package name for model that Dao uses (e.g. com.xgen.cloud.user._public.model.AppUser)",
        "Help me please",
        Messages.getQuestionIcon());
    RefactorDaoSvc service = ApplicationManager.getApplication().getService(RefactorDaoSvc.class);
    service.removeMorphiaImports(e.getProject(), psiFile);
    service.extendNewBaseDao(e.getProject(), psiFile, modelName);
  }

  @Override
  public void update(@NotNull final AnActionEvent e) {
    // Get required data keys
    final Project project = e.getProject();
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    final VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
    final String fileName = virtualFile != null ? virtualFile.getName() : null;
    // Set visibility and enable only in case of existing project and editor and if a selection exists
    e.getPresentation().setEnabledAndVisible(
        project != null && editor != null && fileName != null && fileName.contains("Dao")
    );
  }
}
