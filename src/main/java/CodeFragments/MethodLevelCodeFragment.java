package CodeFragments;

import Tools.LiveParser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodLevelCodeFragment extends AnAction {

    public Map<Integer, ArrayList<Integer>> catchClause = new HashMap<Integer, ArrayList<Integer>>();
    public Map<Integer, ArrayList<Integer>> forCounter = new HashMap<Integer, ArrayList<Integer>>();
    public List<ReturnStmt> returnstatementlist = new ArrayList<ReturnStmt>();
    public Map<Integer, ArrayList<Integer>> methodCalls = new HashMap<Integer, ArrayList<Integer>>();
    public List<UnaryExpr> UnaryExpressionslist = new ArrayList<UnaryExpr>();
    public List<AssignExpr> AssignExprlist = new ArrayList<AssignExpr>();
    public Map<Integer, ArrayList<Integer>> throwStatement = new HashMap<Integer, ArrayList<Integer>>();
    public Map< Integer, ArrayList<Integer>> finallystmtBlock = new HashMap<Integer, ArrayList<Integer>>();
    public List<ObjectCreationExpr> ObjectCReationExpress = new ArrayList<>();
    public Map<String, ArrayList<Integer>> equalsmethodArguments= new HashMap<String, ArrayList<Integer>>();
    public List<String> arraysList = new ArrayList<>();
    public List<IfStmt> IfStatements = new ArrayList<>();
    public List<MethodCallExpr> calledMethodNames = new ArrayList<>();

    public MethodLevelCodeFragment(){
        JavaParser.getStaticConfiguration().setAttributeComments(false);

        CompilationUnit cu;
        try {
            cu = JavaParser.parse(LiveParser.getInstance().editorText);
        } finally {
        }

        VoidVisitor<Map<Integer, ArrayList<Integer>>> CatchClauseVisitor = new CatchClauseVisitor();
        CatchClauseVisitor.visit(cu, catchClause);

        BlockVisitor BlockVisitor = new BlockVisitor();
        BlockVisitor.visit(cu, forCounter);

        VoidVisitor<List<ReturnStmt>> ReturnVisitor = new ReturnVisitor();
        ReturnVisitor.visit(cu, returnstatementlist);

        VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>> MethodCallVisitor = new MethodCallVisitor();
        MethodCallVisitor.visit(cu, methodCalls);

        VoidVisitor<List<UnaryExpr>> UnaryExprVisitor = new UnaryExprVisitor();
        UnaryExprVisitor.visit(cu, UnaryExpressionslist);

        VoidVisitor<List<AssignExpr>> AssignExprVisitor = new AssignExprVisitor();
        AssignExprVisitor.visit(cu, AssignExprlist);

        VoidVisitor<Map<Integer, ArrayList<Integer>>> ThrowVisitor = new ThrowStatementVisitor();
        ThrowVisitor.visit(cu,throwStatement);

        VoidVisitor<Map<Integer, ArrayList<Integer>>> finallyBlockVisitor = new FinallyBlockVisitor();
        finallyBlockVisitor.visit(cu,finallystmtBlock);

        VoidVisitor<List<ObjectCreationExpr>> ObjectCreationExprVisitor = new ObjectCreationExprVisitor();
        ObjectCreationExprVisitor.visit(cu, ObjectCReationExpress);

        ArrayVisitor ArrayListVisitor = new ArrayVisitor();
        ArrayListVisitor.visit(cu,arraysList);

        VoidVisitor<Map<String, ArrayList<Integer>>> equalVisitor = new EqualsVisitor();
        equalVisitor.visit(cu, equalsmethodArguments);

        VoidVisitor<List<IfStmt>> IfStatementVisitor = new IfStatementVisitor();
        IfStatementVisitor.visit(cu,IfStatements);

        VoidVisitor<List<MethodCallExpr>> MethodCallExprVisitor = new MethodCallExprVisitor();
        MethodCallExprVisitor.visit(cu,calledMethodNames);
    }

    private static class CatchClauseVisitor extends VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>> {
        int x = 1;
        int line, column, end;

        @Override
        public void visit(com.github.javaparser.ast.stmt.CatchClause n, Map<Integer, ArrayList<Integer>> collector) {
            super.visit(n, collector);
            if (String.valueOf(n.getParameter().getType()).equals("NullPointerException") || String.valueOf(n.getParameter().getType()).equals("Exception") || String.valueOf(n.getParameter().getType()).equals("Throwable")) {
                line = n.getParameter().getType().getBegin().get().line;
                column = n.getParameter().getType().getBegin().get().column;
                end = n.getParameter().getType().getEnd().get().column;
                collector.put(x, new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);
                }});
                x++;
            }
        }
    }

    private static class BlockVisitor extends VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>> {
        int x = 1;
        int line,column,end;
        @Override
        public void visit(ForStmt n, Map<Integer, ArrayList<Integer>> collector) {
            super.visit(n, collector);
            if (n.getInitialization().size()!=0 && String.valueOf(n.getInitialization().get(0).getChildNodes().get(0).getChildNodes().get(0)).equals("float")) {
                line = n.getInitialization().get(0).getChildNodes().get(0).getChildNodes().get(0).getBegin().get().line;
                column = n.getInitialization().get(0).getChildNodes().get(0).getChildNodes().get(0).getBegin().get().column;
                end = n.getInitialization().get(0).getChildNodes().get(0).getChildNodes().get(0).getEnd().get().column;
                collector.put(x,new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);}});
                x++;
            }
        }
    }

    private static class ReturnVisitor extends VoidVisitorAdapter<List<ReturnStmt>> {
        @Override
        public void visit(ReturnStmt n, List<ReturnStmt> collector) {
            super.visit(n, collector);
            if(!n.toString().equals("return;")){
                collector.add(n);
            }
        }
    }

    private static class MethodCallVisitor extends VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>>
    {
        int x = 1;
        int line,column,end;
        @Override
        public void visit(MethodCallExpr n, Map<Integer, ArrayList<Integer>> arg) {
            super.visit(n, arg);
            if(n.getNameAsString().equals("run"))
            {
                line = n.getName().getBegin().get().line;
                column = n.getName().getBegin().get().column;
                end = n.getName().getEnd().get().column;
                arg.put(x,new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);}});
                x++;
            }
        }
    }

    private static class UnaryExprVisitor extends VoidVisitorAdapter<List<UnaryExpr>> {
        @Override
        public void visit(UnaryExpr n, List<UnaryExpr> collector) {
            super.visit(n, collector);
            collector.add(n);
        }
    }

    private static class AssignExprVisitor extends VoidVisitorAdapter<List<AssignExpr>> {
        @Override
        public void visit(AssignExpr n, List<AssignExpr> collector) {
            super.visit(n, collector);
            collector.add(n);
        }
    }

    private static class ThrowStatementVisitor extends VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>> {
        int x = 1;
        int line,column,end;
        @Override
        public void visit(ThrowStmt n, Map<Integer, ArrayList<Integer>> collector) {
            super.visit(n, collector);
            if(String.valueOf(n.getExpression().getChildNodes().get(0)).equals("RuntimeException")||String.valueOf(n.getExpression().getChildNodes().get(0)).equals("Exception")||String.valueOf(n.getExpression().getChildNodes().get(0)).equals("Throwable")) {
                line = n.getExpression().getChildNodes().get(0).getBegin().get().line;
                column = n.getExpression().getChildNodes().get(0).getBegin().get().column;
                end = n.getExpression().getChildNodes().get(0).getEnd().get().column;
                collector.put(x, new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);}}); //n.getInitialization().get(0).getChildNodes().get(0).getChildNodes().get(0)
                x+=1;
            }
        }
    }

    private static class FinallyBlockVisitor extends VoidVisitorAdapter<Map<Integer, ArrayList<Integer>>> {
        int x = 1;
        int line,column,end;

        @Override
        public void visit(TryStmt n, Map<Integer, ArrayList<Integer>> collector) {
            super.visit(n, collector);
            if(n.getFinallyBlock().flatMap(fb -> fb.findFirst(ReturnStmt.class)).isPresent()||n.getFinallyBlock().flatMap(fb -> fb.findFirst(BreakStmt.class)).isPresent()||n.getFinallyBlock().flatMap(fb -> fb.findFirst(ContinueStmt.class)).isPresent()||n.getFinallyBlock().flatMap(fb -> fb.findFirst(ThrowStmt.class)).isPresent()) {
                if(n.getFinallyBlock().flatMap(fb -> fb.findFirst(ReturnStmt.class)).isPresent()){
                    line = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ReturnStmt.class)).get().getBegin().get().line;
                    column = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ReturnStmt.class)).get().getBegin().get().column;
                    end = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ReturnStmt.class)).get().getEnd().get().column;
                    collector.put(x, new ArrayList<Integer>() {{
                        add(line);
                        add(column);
                        add(end);}});
                    x++;
                }
                if(n.getFinallyBlock().flatMap(fb -> fb.findFirst(BreakStmt.class)).isPresent()){
                    line = n.getFinallyBlock().flatMap(fb -> fb.findFirst(BreakStmt.class)).get().getBegin().get().line;
                    column = n.getFinallyBlock().flatMap(fb -> fb.findFirst(BreakStmt.class)).get().getBegin().get().column;
                    end = n.getFinallyBlock().flatMap(fb -> fb.findFirst(BreakStmt.class)).get().getEnd().get().column;
                    collector.put(x, new ArrayList<Integer>() {{
                        add(line);
                        add(column);
                        add(end);}});
                    x++;
                }
                if(n.getFinallyBlock().flatMap(fb -> fb.findFirst(ContinueStmt.class)).isPresent()){
                    line = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ContinueStmt.class)).get().getBegin().get().line;
                    column = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ContinueStmt.class)).get().getBegin().get().column;
                    end = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ContinueStmt.class)).get().getEnd().get().column;
                    collector.put(x, new ArrayList<Integer>() {{
                        add(line);
                        add(column);
                        add(end);}});
                    x++;
                }
                if(n.getFinallyBlock().flatMap(fb -> fb.findFirst(ThrowStmt.class)).isPresent()){
                    line = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ThrowStmt.class)).get().getBegin().get().line;
                    column = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ThrowStmt.class)).get().getBegin().get().column;
                    end = n.getFinallyBlock().flatMap(fb -> fb.findFirst(ThrowStmt.class)).get().getEnd().get().column;
                    collector.put(x, new ArrayList<Integer>() {{
                        add(line);
                        add(column);
                        add(end);}});
                    x++;
                }
            }
        }
    }

    private static class ObjectCreationExprVisitor extends VoidVisitorAdapter<List<ObjectCreationExpr>> {
        @Override
        public void visit(ObjectCreationExpr n, List<ObjectCreationExpr> collector) {
            super.visit(n, collector);
            collector.add(n);
        }
    }

    private static class ArrayVisitor extends VoidVisitorAdapter<List<String>> {
        @Override
        public void visit(VariableDeclarator n, List<String> collector) {
            super.visit(n, collector);
            if (n.getType().isArrayType()) {
                collector.add(String.valueOf(n.getName()));
            }
        }
    }

    private static class EqualsVisitor extends  VoidVisitorAdapter<Map<String, ArrayList<Integer>>> { //check using Array creation expression
        int line,column,end;
        int x = 1;
        @Override
        public void visit(MethodCallExpr n, Map<String, ArrayList<Integer>> collector) {
            super.visit(n, collector);
            if (n.getName().asString().equals("equals")&&(n.getArguments().size() == 1)) {
                line = n.getArguments().get(0).getBegin().get().line;
                column = n.getArguments().get(0).getBegin().get().column;
                end = n.getArguments().get(0).getEnd().get().column;
                collector.put(String.valueOf(n.getArguments().get(0)),new ArrayList<Integer>() {{
                    add(line);
                    add(column);
                    add(end);}});
                x++;
            }
        }
    }

    public  static class IfStatementVisitor extends VoidVisitorAdapter<List<IfStmt>>
    {
        @Override
        public void visit(IfStmt n, List<IfStmt> arg){
            super.visit(n,arg);
            arg.add(n);
        }
    }

    private static class MethodCallExprVisitor extends VoidVisitorAdapter<List<MethodCallExpr>> {
        @Override
        public void visit(MethodCallExpr n, List<MethodCallExpr> arg) {
            super.visit(n, arg);
            arg.add(n);
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }
}
