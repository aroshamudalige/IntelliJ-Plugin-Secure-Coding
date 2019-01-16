package ViolationDetectors;

import CodeFragments.MethodLevelCodeFragment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MethodLevelViolationDetector extends AnAction implements  ViolationDetector{

    String valueERR08J = "";
    String valueNum09J = "";
    String valueErr07J = "";
    String valueErr04J = "";
    String valueExp02J = "";

    String rule1Detection;
    String rule2Detection;
    String rule3Detection;
    String rule4Detection;
    String rule5Detection;

    public String rule1Detection(){
        try {
            rule1Detection=detectViolationNUM09J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule1Detection;

    }
    public String rule2Detection(){
        try {
            rule2Detection=detectViolationERR08J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule2Detection;
    }
    public String rule3Detection(){
        try {
            rule3Detection=detectViolationERR07J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule3Detection;

    }

    public String rule4Detection(){
        try {
            rule4Detection=detectViolationERR04J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule4Detection;

    }
    public String rule5Detection(){
        try {
            rule5Detection=detectViolationEXP02J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule5Detection;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }

    public String detectViolationERR08J() throws Exception {
        MethodLevelCodeFragment cc = new MethodLevelCodeFragment();
        if (!cc.catchClause.isEmpty()) {
            ArrayList<Integer> lines = new ArrayList<Integer>();
            ArrayList<Integer> columns = new ArrayList<Integer>();
            ArrayList<Integer> ends = new ArrayList<Integer>();
            for (int i = 1; i < cc.catchClause.size() + 1; i++) {
                lines.add(cc.catchClause.get(i).get(0));
                columns.add(cc.catchClause.get(i).get(1));
                ends.add(cc.catchClause.get(i).get(2));
            }
            lce.put("method_rule2_line", lines);
            lce.put("method_rule2_column", columns);
            lce.put("method_rule2_end", ends);
            valueERR08J = "ERR08-J violated at line " + lines;
            return valueERR08J;
        } else {
            valueERR08J = "";
            return valueERR08J;
        }
    }
    public String detectViolationNUM09J() throws Exception {
        MethodLevelCodeFragment cc = new MethodLevelCodeFragment();
        if (!cc.forCounter.isEmpty()) {
            ArrayList<Integer> lines = new ArrayList<Integer>();
            ArrayList<Integer> columns = new ArrayList<Integer>();
            ArrayList<Integer> ends = new ArrayList<Integer>();
            for (int i = 1; i < cc.forCounter.size()+1; i++) {
                lines.add(cc.forCounter.get(i).get(0));
                columns.add(cc.forCounter.get(i).get(1));
                ends.add(cc.forCounter.get(i).get(2));
            }
            lce.put("method_rule1_line", lines);
            lce.put("method_rule1_column", columns);
            lce.put("method_rule1_end", ends);
            valueNum09J = "NUM09-J violated at line " + lines;
            return valueNum09J;
        } else {
            valueNum09J = "";
            return valueNum09J;
        }
    }

    public String detectViolationERR07J() throws Exception {
        MethodLevelCodeFragment cc = new MethodLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isViolated = false;
        if (!cc.throwStatement.isEmpty()) {
            for (int i = 1; i < cc.throwStatement.size() + 1; i++) {
                lines.add(cc.throwStatement.get(i).get(0));
                columns.add(cc.throwStatement.get(i).get(1));
                ends.add(cc.throwStatement.get(i).get(2));
                isViolated = true;
            }
        }
        if(isViolated){
            lce.put("method_rule3_line", lines);
            lce.put("method_rule3_column", columns);
            lce.put("method_rule3_end", ends);
            valueErr07J = "ERR07J violated at " + lines;
            return valueErr07J;
        }
        return valueErr07J;
    }


    public String detectViolationERR04J() throws Exception {
        MethodLevelCodeFragment cc = new MethodLevelCodeFragment();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean isViolated = false;
        if (!cc.finallystmtBlock.isEmpty()) {
            for (int i = 1; i < cc.finallystmtBlock.size() + 1; i++) {
                lines.add(cc.finallystmtBlock.get(i).get(0));
                columns.add(cc.finallystmtBlock.get(i).get(1));
                ends.add(cc.finallystmtBlock.get(i).get(2));
                isViolated = true;
            }
        }
        if(isViolated){
            lce.put("method_rule4_line", lines);
            lce.put("method_rule4_column", columns);
            lce.put("method_rule4_end", ends);
            valueErr04J = "ERR04J violated at " + lines;
            return valueErr04J;
        }
        return valueErr04J;
    }

    public String detectViolationEXP02J() throws Exception {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        MethodLevelCodeFragment cc = new MethodLevelCodeFragment();
        boolean isViolated = false;
        if (!cc.equalsmethodArguments.isEmpty()) {
            for (int i = 0; i < cc.arraysList.size(); i++) {
                if (cc.equalsmethodArguments.containsKey(cc.arraysList.get(i))) {
                    lines.add(cc.equalsmethodArguments.get(cc.arraysList.get(i)).get(0));
                    columns.add(cc.equalsmethodArguments.get(cc.arraysList.get(i)).get(1));
                    ends.add(cc.equalsmethodArguments.get(cc.arraysList.get(i)).get(2));
                    isViolated = true;
                }
            }
        }
        if(isViolated){
            lce.put("method_rule5_line", lines);
            lce.put("method_rule5_column", columns);
            lce.put("method_rule5_end", ends);
            valueExp02J = "EXP02J violated at " + lines;
            return valueExp02J;
        }
        return valueExp02J;
    }
}