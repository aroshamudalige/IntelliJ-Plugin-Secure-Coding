package CodeFragments;

import Tools.LiveParser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageLevelCodeFragment extends AnAction {
    public List<String> ImplementedInterfaces = new ArrayList<String>();
    public PackageLevelCodeFragment() {
        JavaParser.getStaticConfiguration().setAttributeComments(false);
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(LiveParser.getInstance().editorText);
        } finally {
        }
        VoidVisitorAdapter<List<String>> ClassVisitor = new ClassVisitor();
        ClassVisitor.visit(cu, ImplementedInterfaces);
    }

    private static class ClassVisitor extends VoidVisitorAdapter<List<String>>
    {
        @Override
        public void visit(ClassOrInterfaceType n, List<String> arg) {
            super.visit(n, arg);
            arg.add(n.getNameAsString());
        }
    }
    @Override
    public void actionPerformed(AnActionEvent e) {
    }
}
