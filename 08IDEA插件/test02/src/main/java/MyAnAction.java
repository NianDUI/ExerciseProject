import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @Title: MyAnAction.java
 * @description: TODO
 * @time: 2020/2/10 14:38
 * @author: liyongda
 * @version: 1.0
 */
public class MyAnAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("MyAnAction.actionPerformed(AnActionEvent e)");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);

    }
}
