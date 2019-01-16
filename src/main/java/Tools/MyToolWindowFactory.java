package Tools;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;

public class MyToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
//        ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//        Content content = contentFactory.createContent(consoleView.getComponent(), "Results", false);
//        toolWindow.getContentManager().addContent(content);
    }
}