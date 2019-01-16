package CodeFragments;

import Tools.LiveParser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassLevelCodeFragment extends AnAction {
    public Map<String, Integer> methodNames = new HashMap<String, Integer>();
    public List<MethodDeclaration> methoddeclarations = new ArrayList<MethodDeclaration>();
    public List<String> ClassVarNonPrimitiveList = new ArrayList<String>();
    public Map<String, ArrayList<Integer>> methodSignatures = new HashMap<>();
    public List<FieldDeclaration> clssvardeclarations = new ArrayList<FieldDeclaration>();
    public List<AssignExpr> constructorAssignStmt = new ArrayList<AssignExpr>();
    public static String className = "";

    public ClassLevelCodeFragment() {
        JavaParser.getStaticConfiguration().setAttributeComments(false);
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(LiveParser.getInstance().editorText);
        } finally {
        }
        VoidVisitor<Map<String, Integer>> MethodVisitor = new MethodVisitor();
        MethodVisitor.visit(cu, methodNames);
        VoidVisitor<List<MethodDeclaration>> MethodVisitorNode = new MethodVisitorNode();
        MethodVisitorNode.visit(cu, methoddeclarations);
        VoidVisitor<List<String>> ClassVarNonPrimitiveVisitor = new ClassVarNonPrimitiveVisitor();
        ClassVarNonPrimitiveVisitor.visit(cu, ClassVarNonPrimitiveList);
        VoidVisitor<Map<String, ArrayList<Integer>>> MethodDeclarationVisitor = new MethodDeclarationVisitor();
        MethodDeclarationVisitor.visit(cu,methodSignatures);
        VoidVisitor<List<FieldDeclaration>> ClassVarVisitor = new ClassVarVisitor();
        ClassVarVisitor.visit(cu, clssvardeclarations);
        VoidVisitor<List<AssignExpr>> ConstructorVisitor = new ConstructorVisitor();
        ConstructorVisitor.visit(cu, constructorAssignStmt);
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Map<String, Integer>> {
        @Override
        public void visit(MethodDeclaration n , Map<String, Integer> collector) {
            collector.put(n.getNameAsString(),n.getName().getBegin().get().line);
        }
    }

    private static class MethodVisitorNode extends VoidVisitorAdapter<List<MethodDeclaration>> {
        @Override
        public void visit(MethodDeclaration n, List<MethodDeclaration> collector) {
            super.visit(n, collector);
            collector.add(n);
        }
    }

    private static class MethodDeclarationVisitor extends VoidVisitorAdapter<Map<String, ArrayList<Integer>>> {
        int line,column,end;
        @Override
        public void visit(MethodDeclaration n, Map<String, ArrayList<Integer>> arg) {
            super.visit(n, arg);

            if((n.getNameAsString().equals("readObject")&&(!(n.isPrivate()&&!n.isStatic())))||(n.getNameAsString().equals("writeObject")&&(!(n.isPrivate()&&!n.isStatic())))|| n.getNameAsString().equals("readResolve")&&(n.isStatic()||n.isPrivate()) || (n.getNameAsString().equals("writeReplace")&&(n.isStatic()||n.isPrivate())))
            {
                line = n.getName().getBegin().get().line;
                column = n.getName().getBegin().get().column;
                end = n.getName().getEnd().get().column;
                arg.put(n.getDeclarationAsString(),new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);
                }});
            }

        }
    }

    private static class ClassVarNonPrimitiveVisitor extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, List<String> collector) {
            super.visit(n, collector);
            for (FieldDeclaration ff : n.getFields()) {
                if(!ff.getVariable(0).getType().isPrimitiveType() && ff.getModifiers().contains(Modifier.PRIVATE)){
                    collector.add(ff.getVariable(0).getType().toString() + "_" + ff.getVariable(0).getNameAsString());
                }
            }
        }
    }

    private static class ClassVarVisitor extends VoidVisitorAdapter<List<FieldDeclaration>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, List<FieldDeclaration> collector) {
            super.visit(n, collector);
            className = n.getNameAsString();
            for (FieldDeclaration ff : n.getFields()) {
                collector.add(ff);
            }
        }
    }

    private static class ConstructorVisitor extends VoidVisitorAdapter<List<AssignExpr>> {
        @Override
        public void visit(ConstructorDeclaration n, List<AssignExpr> collector) {
            super.visit(n, collector);
            for (Statement s : n.getBody().getStatements()){
                if (s instanceof ExpressionStmt) {
                    ExpressionStmt est = (ExpressionStmt) s;
                    if (est.getExpression() instanceof AssignExpr) {
                        collector.add(est.getExpression().asAssignExpr());
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }
}
