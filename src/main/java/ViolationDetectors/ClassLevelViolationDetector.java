package ViolationDetectors;

import CodeFragments.ClassLevelCodeFragment;
import CodeFragments.MethodLevelCodeFragment;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.List;

public class ClassLevelViolationDetector extends AnAction implements  ViolationDetector {
    String valueMET09J = "";
    String valueOBJ05J = "";
    String valueOBJ01J = "";
    String valueOBJ10J = "";
    String valueDCL00J = "";

    String rule1Detection;
    String rule2Detection;
    String rule3Detection;
    String rule4Detection;
    String rule5Detection;

    public String rule1Detection(){
        try {
            rule1Detection=   detectViolationMET09J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule1Detection;
    }
    public String rule2Detection(){
        try {
            rule2Detection=   detectViolationOBJ05J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule2Detection;
    }

    public String rule3Detection(){
        try {
            rule3Detection=   detectViolationOBJ01J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule3Detection;
    }

    public String rule4Detection(){
        try {
            rule4Detection=   detectViolationDCL00J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule4Detection;
    }

    public String rule5Detection(){
        try {
            rule5Detection=detectViolationOBJ10J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule5Detection;
    }

    public String detectViolationMET09J() {
        ClassLevelCodeFragment cc = new ClassLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isviolated = false;
        for (MethodDeclaration n:cc.methoddeclarations
                ) {
            if(n.getNameAsString().equals("equals")){
                isviolated = true;
                lines.add(n.getName().getBegin().get().line);
                columns.add(n.getName().getBegin().get().column);
                ends.add(n.getName().getEnd().get().column);
            }
        }
        for (MethodDeclaration n:cc.methoddeclarations
                ) {
            if(n.getNameAsString().equals("hashCode")){
                isviolated = false;
            }
        }
        if(isviolated){
            lce.put("class_rule1_line", lines);
            lce.put("class_rule1_column", columns);
            lce.put("class_rule1_end", ends);
            valueMET09J = "MET09-J violated at line " + lines;
            return  valueMET09J;
        }
        valueMET09J = "";
        return  valueMET09J;
    }

    public String detectViolationOBJ05J() {
        MethodLevelCodeFragment mcf =new MethodLevelCodeFragment();
        ClassLevelCodeFragment ccf = new ClassLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isviolated = false;
        for (String n:ccf.ClassVarNonPrimitiveList
                ) {
                    for (ReturnStmt r:mcf.returnstatementlist
                            ) {
                        if(r.getChildNodes().get(0).toString().equals(n.split("_")[1])){
                            isviolated = true;
                            lines.add(r.getBegin().get().line);
                            columns.add(r.getBegin().get().column);
                            ends.add(r.getEnd().get().column);
                        }
                    }
                }
        if(isviolated){
            lce.put("class_rule2_line", lines);
            lce.put("class_rule2_column", columns);
            lce.put("class_rule2_end", ends);
            valueOBJ05J="OBJ05-J violated at line " + lines;
            return valueOBJ05J;
        }
        valueOBJ05J= "";
        return valueOBJ05J;
    }

    public String detectViolationDCL00J() {
        boolean constvarfound = false;
        List<String> varnamestocheck = new ArrayList<String>();
        List<String> assignExpInCons = new ArrayList<String>();
        ClassLevelCodeFragment ccf = new ClassLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isviolated = false;
        if (!ccf.clssvardeclarations.isEmpty()) {
            for (FieldDeclaration member : ccf.clssvardeclarations) {
                if(member.getVariable(0).getTypeAsString().equals(ccf.className)){
                    constvarfound = true;
                    lines.add(member.getBegin().get().line);
                    columns.add(member.getBegin().get().column);
                    ends.add(member.getEnd().get().column);
                }
                if(constvarfound && !member.getVariable(0).getTypeAsString().equals(ccf.className)){
                    varnamestocheck.add(member.getVariable(0).getNameAsString());
                }
            }
        }
        if (!ccf.constructorAssignStmt.isEmpty()) {
            for (AssignExpr member : ccf.constructorAssignStmt) {
                assignExpInCons.add(member.getValue().toString());
            }
        }
        for (String x: varnamestocheck
                ) {
            for (String y: assignExpInCons
                    ) {
                if(y.contains(x)){
                    isviolated = true;
                }
            }
        }
        if(isviolated){
            lce.put("class_rule4_line", lines);
            lce.put("class_rule4_column", columns);
            lce.put("class_rule4_end", ends);
            valueDCL00J = "DCL00-J violated at line " + lines;
            return valueDCL00J;
        }
        return valueDCL00J;

    }

    public String detectViolationOBJ01J() {
        MethodLevelCodeFragment mcf =new MethodLevelCodeFragment();
        ClassLevelCodeFragment ccf = new ClassLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isViolated = false;
        for (FieldDeclaration fd:ccf.clssvardeclarations
                ) {
            if(fd.isPublic()){
                for (ReturnStmt returnitem:mcf.returnstatementlist
                        ) {
                    if(returnitem.getChildNodes().get(0).toString().equals(fd.getVariable(0).toString().split("\\=")[0].replaceAll("\\s+","")) || returnitem.getChildNodes().get(0).toString().split("\\.")[0].toString().equals(fd.getVariable(0).toString().split("\\=")[0].replaceAll("\\s+","")) || returnitem.getChildNodes().get(0).toString().split("\\[")[0].toString().equals(fd.getVariable(0).toString().split("\\=")[0].replaceAll("\\s+",""))){
                        isViolated = true;
                    }
                }
                for (AssignExpr assignex: mcf.AssignExprlist
                        ) {
                    if(assignex.getTarget().toString().equals(fd.getVariable(0).toString())){
                        isViolated = true;
                    }
                }
                for (UnaryExpr unaryex: mcf.UnaryExpressionslist
                        ) {
                    if(unaryex.getExpression().toString().equals(fd.getVariable(0).toString())){
                        isViolated = true;
                    }
                }
                if(isViolated){
                    lines.add(fd.getBegin().get().line);
                    columns.add(fd.getBegin().get().column);
                    ends.add(fd.getEnd().get().column);
                }
            }
        }
        if(isViolated){
            lce.put("class_rule3_line", lines);
            lce.put("class_rule3_column", columns);
            lce.put("class_rule3_end", ends);
            valueOBJ01J = "OBJ01-J violated at line " + lines;
            return valueOBJ01J;
        }
        return valueOBJ01J;
    }

    public String detectViolationOBJ10J() {
        ClassLevelCodeFragment ccf = new ClassLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isviolated = false;
        if (!ccf.clssvardeclarations.isEmpty()) {
            for (FieldDeclaration member : ccf.clssvardeclarations) {
                if(member.isPublic() && member.isStatic() && !member.isFinal()){
                    isviolated = true;
                    lines.add(member.getBegin().get().line);
                    columns.add(member.getBegin().get().column);
                    ends.add(member.getEnd().get().column);
                }
            }
        }
        if(isviolated){
            lce.put("class_rule5_line", lines);
            lce.put("class_rule5_column", columns);
            lce.put("class_rule5_end", ends);
            valueOBJ10J = "OBJ10-J violated at line " + lines;
            return valueOBJ10J;
        }
        return valueOBJ10J;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }
}
