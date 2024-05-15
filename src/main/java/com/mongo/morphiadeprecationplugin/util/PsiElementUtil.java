package com.mongo.morphiadeprecationplugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;

public class PsiElementUtil {

  private final PsiElementFactory psiElementFactory;

  public PsiElementUtil(final Project project) {
    this.psiElementFactory = PsiElementFactory.getInstance(project);
  }

  public PsiImportStatement createBaseDaoImportStatement() {
    PsiJavaCodeReferenceElement packageReferenceElement = psiElementFactory.createPackageReferenceElement(
        "com.xgen.cloud.common.dao.base._public.impl");

    return psiElementFactory.createImportStatement(
        psiElementFactory.createClassFromText("BaseDao", packageReferenceElement.getReferenceNameElement()));
  }

  public PsiJavaCodeReferenceElement createBaseDaoReference() {
    return psiElementFactory.createPackageReferenceElement(
        "com.xgen.cloud.common.dao.base._public.impl.BaseDao");
  }

  public PsiJavaCodeReferenceElement createPsiReference(final String modelName) {
    return psiElementFactory.createPackageReferenceElement(modelName);
  }

}
