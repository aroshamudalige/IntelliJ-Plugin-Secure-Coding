package Tools;

import CodeFragments.*;
import Countermeasures.Countermeasures_data;
import ViolationDetectors.*;
import com.intellij.codeInsight.hint.TooltipController;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static ViolationDetectors.ViolationDetector.lce;

public class LiveParser extends AnAction {
    public static String editorText;
    static String rule1; //method level
    static String rule2; //method level
    static String rule3; //class level
    static String rule4; //class level
    static String rule5; //package level
    static String rule6; //package level
    static String rule7; //class level
    static String rule8; //class level
    static String rule9; //method level
    static String rule10; //method level
    static String rule11; //package level
    static String rule12; //package level
    static String rule13; //method level
    static String rule14; //package level
    static String rule15; //class level

    static Countermeasures_data Countermeasure_data = new Countermeasures_data();
    private static LiveParser instance;

    public static LiveParser getInstance(){
        if(instance == null){
            instance = new LiveParser();
        }
        return instance;
    }

    static {
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        final TypedAction typedAction = actionManager.getTypedAction();
        TypedActionHandler handler = typedAction.getHandler();
        typedAction.setupHandler(new MyTypedHandler(handler));
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
    }

    public static class MyTypedHandler implements TypedActionHandler {
        JPanel myToolWindowContent = new JPanel();
        Box box = Box.createVerticalBox();
        Box countermeasure_box = Box.createVerticalBox();
        private final TypedActionHandler handler;

        public MyTypedHandler(TypedActionHandler handler) {
            this.handler = handler;
        }
        @Override
        public void execute(@NotNull Editor editor, char c, @NotNull DataContext dataContext) {
            handler.execute(editor, c, dataContext);
            Project project = editor.getProject();
            Document document = editor.getDocument();
            editorText= document.getText();
            TooltipController tooltipController = new TooltipController();
            editor.addEditorMouseMotionListener(new MyEditorMouseMotionListener(tooltipController));

            JScrollPane scrollArea = new JBScrollPane(myToolWindowContent, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollArea.add(myToolWindowContent);

            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Secure Coding Plugin");
            SyntaxHighlighter syntaxhighlighter = new SyntaxHighlighter();
            syntaxhighlighter.annotateoffsets.clear();
            syntaxhighlighter.tooltips.clear();
            this.cleartoolwindow(toolWindow);
            this.removeAllHighlighters(editor);
            try {

                DetectorFactory DetectorFactory = new DetectorFactory();

                ViolationDetector methodLevelDetector=  DetectorFactory.getViolatorType("MethodLevelViolationDetector");
                ViolationDetector  classLevelDetector=  DetectorFactory.getViolatorType("ClassLevelViolationDetector");
                ViolationDetector  packageLevelDetector=  DetectorFactory.getViolatorType("PackageLevelViolationDetector");

                MethodLevelCodeFragment methodLevel= new MethodLevelCodeFragment();
                ClassLevelCodeFragment classLevel = new ClassLevelCodeFragment();
                PackageLevelCodeFragment packageLevel = new PackageLevelCodeFragment();

                myToolWindowContent.setLayout(new GridLayout(1,2));
                myToolWindowContent.setAutoscrolls(true);
                box.setBorder(BorderFactory.createLineBorder(JBColor.BLACK));
                countermeasure_box.setBorder(BorderFactory.createLineBorder(JBColor.BLACK));
                JLabel boxHeading = new JLabel("<html><h3><font color='black'>Violated Rules</font><h3></html>");
                boxHeading.setHorizontalAlignment(JLabel.CENTER);
                boxHeading.setBackground(JBColor.ORANGE);
                box.add(boxHeading);

                JLabel CounterM_boxHeading = new JLabel("<html><h3><font color='black'>Rule Description</font><h3></html>");
                CounterM_boxHeading.setHorizontalAlignment(JLabel.CENTER);
                CounterM_boxHeading.setBackground(JBColor.ORANGE);
                countermeasure_box.add(CounterM_boxHeading);

                if (true) {
                    rule1 = methodLevelDetector.rule1Detection();
                    if(!rule1.equals("")){
                        String tooltip = "Do not use floating-point variables as loop counters";
                        syntaxhighlighter.highlight(editor, document, lce.get("method_rule1_line"), lce.get("method_rule1_column"), lce.get("method_rule1_end"),tooltip);
                        methodLevel.forCounter.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("NUM09J");
                        JLabel jLabel = this.createJLabel(rule1, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/NUM09-J.+Do+not+use+floating-point+variables+as+loop+counters"));
                        //box.add(this.createJLabel(rule1, tooltip));
                    }
                    methodLevel.forCounter.clear();

                    rule2= methodLevelDetector.rule2Detection();
                    if(!rule2.equals("")){
                        String tooltip = "Do not catch NullPointerException or any of its ancestors";
                        syntaxhighlighter.highlight(editor, document, lce.get("method_rule2_line"), lce.get("method_rule2_column"), lce.get("method_rule2_end"), tooltip);
                        methodLevel.catchClause.clear();
                        methodLevel.forCounter.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("ERR08J");
                        JLabel jLabel = this.createJLabel(rule2, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/ERR08-J.+Do+not+catch+NullPointerException+or+any+of+its+ancestors"));
                        //box.add(this.createJLabel(rule2, tooltip));
                    }
                    methodLevel.catchClause.clear();
                    methodLevel.forCounter.clear();

                    rule3 = classLevelDetector.rule1Detection();
                    if(!rule3.equals("")){
                        String tooltip = "Classes that define an equals() method must also define a hashCode() method";
                        syntaxhighlighter.highlight(editor, document, lce.get("class_rule1_line"), lce.get("class_rule1_column"), lce.get("class_rule1_end"), tooltip);
                        classLevel.methoddeclarations.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("MET09J");
                        JLabel jLabel = this.createJLabel(rule3, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/MET09-J.+Classes+that+define+an+equals%28%29+method+must+also+define+a+hashCode%28%29+method"));
                        //box.add(this.createJLabel(rule3, tooltip));
                    }
                    classLevel.methoddeclarations.clear();

                    rule4 =  classLevelDetector.rule2Detection();
                    if(!rule4.equals("")){
                        String tooltip = "Do not return references to private mutable class members";
                        syntaxhighlighter.highlight(editor, document, lce.get("class_rule2_line"), lce.get("class_rule2_column"), lce.get("class_rule2_end"), tooltip);
                        classLevel.ClassVarNonPrimitiveList.clear();
                        methodLevel.returnstatementlist.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("OBJ05J");
                        JLabel jLabel =this.createJLabel(rule4,tooltip);
                        JLabel link = new JLabel(("Click here for more details"));
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/OBJ05-J.+Do+not+return+references+to+private+mutable+class+members"));
                        //box.add(this.createJLabel(rule4, tooltip));
                    }
                    classLevel.ClassVarNonPrimitiveList.clear();
                    methodLevel.returnstatementlist.clear();

                    rule5 = packageLevelDetector.rule1Detection();
                    if(!rule5.equals("")){
                        String tooltip = "Do not invoke Thread.run()";
                        syntaxhighlighter.highlight(editor, document, lce.get("package_rule1_line"), lce.get("package_rule1_column"), lce.get("package_rule1_end"), tooltip);
                        packageLevel.ImplementedInterfaces.clear();
                        methodLevel.methodCalls.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("THI00J");
                        JLabel jLabel = this.createJLabel(rule5, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/pages/viewpage.action?pageId=88487912"));
                        //box.add(this.createJLabel(rule5, tooltip));
                    }
                    packageLevel.ImplementedInterfaces.clear();
                    methodLevel.methodCalls.clear();

                    rule6 = packageLevelDetector.rule2Detection();
                    if(!rule6.equals("")){
                        String tooltip = "Do not deviate from the proper signatures of serialization methods";
                        syntaxhighlighter.highlight(editor, document, lce.get("package_rule2_line"), lce.get("package_rule2_column"), lce.get("package_rule2_end"), tooltip);
                        packageLevel.ImplementedInterfaces.clear();
                        classLevel.methodSignatures.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("SER01J");
                        JLabel jLabel = this.createJLabel(rule6, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/SER01-J.+Do+not+deviate+from+the+proper+signatures+of+serialization+methods"));
                        //box.add(this.createJLabel(rule6, tooltip));
                    }
                    packageLevel.ImplementedInterfaces.clear();
                    classLevel.methodSignatures.clear();

                    rule7 = classLevelDetector.rule4Detection();
                    if(!rule7.equals("")){
                        String tooltip = "Prevent class initialization cycles";
                        syntaxhighlighter.highlight(editor, document, lce.get("class_rule4_line"), lce.get("class_rule4_column"), lce.get("class_rule4_end"), tooltip);
                        classLevel.clssvardeclarations.clear();
                        classLevel.constructorAssignStmt.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("DCL00J");
                        JLabel jLabel = this.createJLabel(rule7, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/DCL00-J.+Prevent+class+initialization+cycles"));
                        //box.add(this.createJLabel(rule7, tooltip));
                    }
                    classLevel.clssvardeclarations.clear();
                    classLevel.constructorAssignStmt.clear();

                    rule8 = classLevelDetector.rule3Detection();
                    if(!rule8.equals("")){
                        String tooltip = "Limit accessibility of fields";
                        syntaxhighlighter.highlight(editor, document, lce.get("class_rule3_line"), lce.get("class_rule3_column"), lce.get("class_rule3_end"), tooltip);
                        classLevel.clssvardeclarations.clear();
                        methodLevel.returnstatementlist.clear();
                        methodLevel.AssignExprlist.clear();
                        methodLevel.UnaryExpressionslist.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("OBJ01J");
                        JLabel jLabel6 = this.createJLabel(rule8, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel6.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel6,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/OBJ01-J.+Limit+accessibility+of+fields"));
                        //box.add(this.createJLabel(rule8, tooltip));
                    }
                    classLevel.clssvardeclarations.clear();
                    methodLevel.returnstatementlist.clear();
                    methodLevel.AssignExprlist.clear();
                    methodLevel.UnaryExpressionslist.clear();

                    rule9= methodLevelDetector.rule3Detection();
                    if(!rule9.equals("")){
                        String tooltip = "Do not throw RuntimeException, Exception, or Throwable";
                        syntaxhighlighter.highlight(editor, document, lce.get("method_rule3_line"), lce.get("method_rule3_column"), lce.get("method_rule3_end"), tooltip);
                        methodLevel.throwStatement.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("ERR07J");
                        JLabel jLabel6 = this.createJLabel(rule9, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel6.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel6,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/ERR07-J.+Do+not+throw+RuntimeException%2C+Exception%2C+or+Throwable"));
                        //box.add(this.createJLabel(rule9, tooltip));
                    }
                    methodLevel.throwStatement.clear();

                    rule10= methodLevelDetector.rule4Detection();
                    if(!rule10.equals("")){
                        String tooltip = "Do not complete abruptly from a finally block";
                        syntaxhighlighter.highlight(editor, document, lce.get("method_rule4_line"), lce.get("method_rule4_column"), lce.get("method_rule4_end"), tooltip);
                        methodLevel.finallystmtBlock.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("ERR04J");
                        JLabel jLabel = this.createJLabel(rule10, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/ERR04-J.+Do+not+complete+abruptly+from+a+finally+block"));
                        //box.add(this.createJLabel(rule10, tooltip));
                    }
                    methodLevel.finallystmtBlock.clear();

                    rule11 = packageLevelDetector.rule3Detection();
                    if(!rule11.equals("")){
                        String tooltip = "Do not construct BigDecimal objects from floating-point literals";
                        syntaxhighlighter.highlight(editor, document, lce.get("package_rule3_line"), lce.get("package_rule3_column"), lce.get("package_rule3_end"), tooltip);
                        methodLevel.ObjectCReationExpress.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("NUM10J");
                        JLabel jLabel = this.createJLabel(rule11, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/NUM10-J.+Do+not+construct+BigDecimal+objects+from+floating-point+literals"));
                        //box.add(this.createJLabel(rule11, tooltip));
                    }
                    methodLevel.ObjectCReationExpress.clear();

                    rule12 = packageLevelDetector.rule4Detection();
                    if(!rule12.equals("")){
                        String tooltip = "Call the superclass's getPermissions() method when writing a custom class loader";
                        syntaxhighlighter.highlight(editor, document, lce.get("package_rule4_line"), lce.get("package_rule4_column"), lce.get("package_rule4_end"), tooltip);
                        classLevel.methoddeclarations.clear();
                        methodLevel.ObjectCReationExpress.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("SEC07J");
                        JLabel jLabel = this.createJLabel(rule12, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/SEC07-J.+Call+the+superclass%27s+getPermissions%28%29+method+when+writing+a+custom+class+loader"));
                        //box.add(this.createJLabel(rule12, tooltip));
                    }
                    classLevel.methoddeclarations.clear();
                    methodLevel.ObjectCReationExpress.clear();

                    rule13= methodLevelDetector.rule5Detection();
                    if(!rule13.equals("")){
                        String tooltip = "Do not use the Object.equals() method to compare two arrays";
                        syntaxhighlighter.highlight(editor, document, lce.get("method_rule5_line"), lce.get("method_rule5_column"), lce.get("method_rule5_end"), tooltip);
                        methodLevel.equalsmethodArguments.clear();
                        methodLevel.arraysList.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("EXP02J");
                        JLabel jLabel = this.createJLabel(rule13, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/EXP02-J.+Do+not+use+the+Object.equals%28%29+method+to+compare+two+arrays"));
                        //box.add(this.createJLabel(rule13, tooltip));
                    }
                    methodLevel.equalsmethodArguments.clear();
                    methodLevel.arraysList.clear();

                    rule14 = packageLevelDetector.rule5Detection();
                    if(!rule14.equals("")){
                        String tooltip = "Detect and handle file-related errors";
                        syntaxhighlighter.highlight(editor, document, lce.get("package_rule5_line"), lce.get("package_rule5_column"), lce.get("package_rule5_end"), tooltip);
                        methodLevel.ObjectCReationExpress.clear();
                        methodLevel.IfStatements.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("FIO02J");
                        JLabel jLabel = this.createJLabel(rule14, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/FIO02-J.+Detect+and+handle+file-related+errors"));
                        //box.add(this.createJLabel(rule14, tooltip));
                    }
                    methodLevel.ObjectCReationExpress.clear();
                    methodLevel.IfStatements.clear();

                    rule15 = classLevelDetector.rule5Detection();
                    if(!rule15.equals("")){
                        String tooltip = "Do not use public static nonfinal fields";
                        syntaxhighlighter.highlight(editor, document, lce.get("class_rule5_line"), lce.get("class_rule5_column"), lce.get("class_rule5_end"), tooltip);
                        classLevel.clssvardeclarations.clear();
                        String CounterMeasure = Countermeasure_data.CountermeasureData.get("OBJ10J");
                        JLabel jLabel = this.createJLabel(rule15, tooltip);
                        JLabel link = new JLabel("Click here for more details");
                        link.setBorder(new EmptyBorder(0,4,0,0));
                        link.setHorizontalAlignment(JLabel.CENTER);
                        jLabel.setHorizontalAlignment(JLabel.CENTER);
                        box.add(setRuleDescription(jLabel,CounterM_boxHeading,link,CounterMeasure,"https://wiki.sei.cmu.edu/confluence/display/java/OBJ10-J.+Do+not+use+public+static+nonfinal+fields"));
                        //box.add(this.createJLabel(rule15, tooltip));
                    }
                    classLevel.clssvardeclarations.clear();

                    myToolWindowContent.add(box);
                    myToolWindowContent.add(countermeasure_box);
                    this.addtotoolwindow(toolWindow);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        public JLabel createJLabel(String rule, String tooltip){
            JLabel jp = new JLabel();
            jp.setHorizontalAlignment(JLabel.CENTER);
            jp.setText("<html><br><font color='#ff4d4d'>" + rule + "</font><br></html>");
            jp.setToolTipText(tooltip);
            return jp;
        }

        public JLabel createCountermeasure (String counterMeasureData)
        {
            JLabel jp = new JLabel();
            jp.setText(counterMeasureData);
            jp.setHorizontalAlignment(JLabel.CENTER);
            return jp;
        }

        public void addtotoolwindow(ToolWindow toolWindow){
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(myToolWindowContent, "", true);
            toolWindow.getContentManager().addContent(content);
        }

        public void cleartoolwindow(ToolWindow toolWindow){
            toolWindow.getContentManager().removeAllContents(true);
            box.removeAll();
            countermeasure_box.removeAll();
            myToolWindowContent.remove(box);
            myToolWindowContent.remove(countermeasure_box);
        }

        public void setLinkToRule(JLabel link,String url){

            link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            link.setToolTipText("Click on this link for more details...");
            link.setForeground(Color.GRAY);
            link.setHorizontalAlignment(JLabel.CENTER);
            Font font = link.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            link.setFont(font.deriveFont(attributes));
            link.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton()>0) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                URI uri = new URI(url);
                                desktop.browse(uri);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            } catch (URISyntaxException ex) {
                                System.out.println(ex.getMessage());
                            }
                        } else {
                            System.out.println("Desktop not supported.");
                        }

                        System.out.print(e.getClickCount());
                    }
                }
            });
        }

        public JLabel setRuleDescription(JLabel label, JLabel labelHeading, JLabel link, String ruleDetails, String url){
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //super.mouseClicked(e);
                    if(e.getButton()==MouseEvent.BUTTON1){
                        countermeasure_box.removeAll();
                        countermeasure_box.add(labelHeading);
                        countermeasure_box.add(createCountermeasure(ruleDetails));
                        setLinkToRule(link,url);
                        countermeasure_box.add(link);
                    }
                }
            });
            //myToolWindowContent.add(countermeasure_box);
            return label;
        }

        public void removeAllHighlighters(Editor editor){
            editor.getMarkupModel().removeAllHighlighters();
        }
    }
}