package ViolationDetectors;

import CodeFragments.ClassLevelCodeFragment;
import CodeFragments.MethodLevelCodeFragment;
import CodeFragments.PackageLevelCodeFragment;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.Map;

public class PackageLevelViolationDetector extends AnAction implements  ViolationDetector  {
    String valueTHI00J = "";
    String valueSER01J = "";
    String valueNUM10J = "";
    String valueSEC07J = "";
    String valueFIO02J = "";

    String rule1Detection;
    String rule2Detection;
    String rule3Detection;
    String rule4Detection;
    String rule5Detection;

    @Override
    public String rule1Detection() {
        try {
            rule1Detection=detectViolationTHI00J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule1Detection;
    }

    @Override
    public String rule2Detection() {
        try {
            rule2Detection=detectViolationSER01J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule2Detection;
    }

    public String rule3Detection() {
        try {
            rule3Detection=detectViolationNUM10J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule3Detection;
    }

    public String rule4Detection() {
        try {
            rule4Detection=detectViolationSEC07J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule4Detection;
    }

    public String rule5Detection() {
        try {
            rule5Detection=detectViolationFIO02J();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule5Detection;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
    }

    public String detectViolationTHI00J() throws Exception
    {
        PackageLevelCodeFragment cc1 = new PackageLevelCodeFragment();
        MethodLevelCodeFragment cc2 = new MethodLevelCodeFragment();
        boolean flag_interface = false;
        if (!cc1.ImplementedInterfaces.isEmpty() && (!cc2.methodCalls.isEmpty())) {
            ArrayList<Integer> lines = new ArrayList<Integer>();
            ArrayList<Integer> columns = new ArrayList<Integer>();
            ArrayList<Integer> ends = new ArrayList<Integer>();
            for (int j=0; j<cc1.ImplementedInterfaces.size(); j++)
            {
                if (cc1.ImplementedInterfaces.get(j).equals("Runnable")) {
                    flag_interface = true;
                }
            }
            if (flag_interface) {
                for (int i = 1; i < cc2.methodCalls.size() + 1; i++) {
                    lines.add(cc2.methodCalls.get(i).get(0));
                    columns.add(cc2.methodCalls.get(i).get(1));
                    ends.add(cc2.methodCalls.get(i).get(2));
                }
                lce.put("package_rule1_line", lines);
                lce.put("package_rule1_column", columns);
                lce.put("package_rule1_end", ends);
                valueTHI00J = "THI00-J violated at line " + lines;
                return valueTHI00J;
            }
        }
        valueTHI00J = "";
        return valueTHI00J;
    }

    public String detectViolationSER01J() throws Exception
    {
        PackageLevelCodeFragment cc1 = new PackageLevelCodeFragment();
        ClassLevelCodeFragment cc2 = new ClassLevelCodeFragment();
        boolean flag_serializable = false;
        boolean violation = false;
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();

        for(Map.Entry<String, ArrayList<Integer>> entry : cc2.methodSignatures.entrySet())
        {
            violation = true;
            lines.add(entry.getValue().get(0));
            columns.add(entry.getValue().get(1));
            ends.add(entry.getValue().get(2));


        }
        for(int j=0; j< cc1.ImplementedInterfaces.size(); j++)
        {
            if(cc1.ImplementedInterfaces.get(j).equals("Serializable"))
            {
                flag_serializable = true;
            }
        }
        if(violation && flag_serializable)
        {
            lce.put("package_rule2_line", lines);
            lce.put("package_rule2_column", columns);
            lce.put("package_rule2_end", ends);
            valueSER01J = "SER01-J violated at line " + lines;
            return valueSER01J;
        }
        valueSER01J = "";
        return valueSER01J;
    }
    public String detectViolationNUM10J() throws Exception
    {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        boolean violation = false;
        MethodLevelCodeFragment cc1 = new MethodLevelCodeFragment();
        if(cc1.ObjectCReationExpress.size()>0){
            for(int i=0;i<cc1.ObjectCReationExpress.size(); i++)
            {
                if (((cc1.ObjectCReationExpress.get(i).getArguments().size()>0)&&(cc1.ObjectCReationExpress.get(i).getArguments().get(0).isDoubleLiteralExpr()))&&(cc1.ObjectCReationExpress.get(i).getTypeAsString().equals("BigDecimal"))){
                    lines.add(cc1.ObjectCReationExpress.get(i).getArguments().get(0).getBegin().get().line);
                    columns.add(cc1.ObjectCReationExpress.get(i).getArguments().get(0).getBegin().get().column);
                    ends.add(cc1.ObjectCReationExpress.get(i).getArguments().get(0).getEnd().get().column);
                    violation = true;
                    //+cc1.ObjectCReationExpress.get(i).getArguments().get(0).getBegin().get().line
                }
            }
        }
        if(violation)
        {
            lce.put("package_rule3_line", lines);
            lce.put("package_rule3_column", columns);
            lce.put("package_rule3_end", ends);
            valueNUM10J = "NUM10J violated at " + lines;
            return valueNUM10J;
        }
        return valueNUM10J;
    }

    public String detectViolationSEC07J() throws Exception
    {
        MethodLevelCodeFragment cc1 = new MethodLevelCodeFragment();
        ClassLevelCodeFragment cc2 = new ClassLevelCodeFragment();
        boolean violation = false;
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        int lineNo;

        if((!cc2.methoddeclarations.isEmpty())&&(!cc1.ObjectCReationExpress.isEmpty())){
            for(int i=0;i<cc2.methoddeclarations.size();i++)
            {
                if(cc2.methoddeclarations.get(i).getNameAsString().equals("getPermissions"))
                {
                    int getPermissions_start=cc2.methoddeclarations.get(i).getBody().get().getRange().get().begin.line;
                    int getPermissions_end=cc2.methoddeclarations.get(i).getBody().get().getRange().get().end.line;
                    for(int j=0;j<cc1.ObjectCReationExpress.size();j++)
                    {
                        lineNo = cc1.ObjectCReationExpress.get(j).getBegin().get().line;
                        if((cc1.ObjectCReationExpress.get(j).getTypeAsString().equals("Permissions"))&&((getPermissions_start<=lineNo)&&(lineNo<=getPermissions_end)))
                        {
                            lines.add(cc1.ObjectCReationExpress.get(j).getBegin().get().line);
                            columns.add(cc1.ObjectCReationExpress.get(j).getBegin().get().column);
                            ends.add(cc1.ObjectCReationExpress.get(j).getEnd().get().column);
                            violation = true;
                        }
                    }
                }
            }
        }
        if(violation){
            lce.put("package_rule4_line", lines);
            lce.put("package_rule4_column", columns);
            lce.put("package_rule4_end", ends);
            valueSEC07J = "SEC07J violated at " + lines;
            return valueSEC07J;
        }
        return  valueSEC07J;
    }

    public String detectViolationFIO02J() throws Exception
    {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Integer> ends = new ArrayList<Integer>();
        String fileDeleteInstance;
        boolean violation = false;
        MethodLevelCodeFragment cc1 = new MethodLevelCodeFragment();
        for(int i=0;i<cc1.calledMethodNames.size();i++){
            for(int j=0;j<cc1.ObjectCReationExpress.size();j++)
            {
                if((cc1.calledMethodNames.get(i).getNameAsString().equals("delete"))&&(cc1.ObjectCReationExpress.get(j).getTypeAsString().equals("File"))&&(cc1.calledMethodNames.get(i).getChildNodes().get(0).toString().equals(cc1.ObjectCReationExpress.get(j).getParentNode().get().getChildNodes().get(1).toString()))){
                    fileDeleteInstance = cc1.calledMethodNames.get(i).getParentNode().get().toString();
                    if(!cc1.IfStatements.isEmpty())
                    {
                        for(int k=0;k<cc1.IfStatements.size();k++)
                        {
                            if((!cc1.IfStatements.get(k).getCondition().toString().equals(fileDeleteInstance)))
                            {
                                if(!lines.contains(cc1.calledMethodNames.get(i).getBegin().get().line)){
                                    lines.add(cc1.calledMethodNames.get(i).getBegin().get().line);
                                    columns.add(cc1.calledMethodNames.get(i).getBegin().get().column);
                                    ends.add(cc1.calledMethodNames.get(i).getEnd().get().column);
                                }
                                violation = true;
                            }
                        }
                    }
                    else{
                        if(!lines.contains(cc1.calledMethodNames.get(i).getBegin().get().line)){
                            lines.add(cc1.calledMethodNames.get(i).getBegin().get().line);
                            columns.add(cc1.calledMethodNames.get(i).getBegin().get().column);
                            ends.add(cc1.calledMethodNames.get(i).getEnd().get().column);
                        }
                        violation = true;
                    }
                }
            }
        }
        if(violation){
            lce.put("package_rule5_line", lines);
            lce.put("package_rule5_column", columns);
            lce.put("package_rule5_end", ends);
            valueFIO02J = "FIO02J violated at " + lines;
            return valueFIO02J;
        }
        return  valueFIO02J;
    }
}