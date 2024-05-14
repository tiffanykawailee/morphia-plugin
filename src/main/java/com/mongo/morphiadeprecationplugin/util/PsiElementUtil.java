package com.mongo.morphiadeprecationplugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceFactory;

public class PsiElementUtil {

  private final PsiElementFactory psiElementFactory;

  public PsiElementUtil(final Project project) {
    this.psiElementFactory = PsiElementFactory.getInstance(project);
  }

  public PsiImportStatement createBaseDaoImportStatement() {
    PsiClass aClass = psiElementFactory.createClassFromText("BaseDao").;
    return psiElementFactory.createImportStatement(aClass);
  }

  public PsiJavaCodeReferenceElement createBaseDaoReference() {
    return psiElementFactory.createPackageReferenceElement(
        "com.xgen.cloud.common.dao.base._public.impl.BaseDao");
  }

  public PsiJavaCodeReferenceElement createPsiReference(final String modelName) {
    return psiElementFactory.createPackageReferenceElement(modelName);
  }

}
