package actions;

import com.google.common.base.Joiner;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OpenInVimAction extends AnAction {
    protected static final String ERROR_DURING_OPENING_TERMINAL_MESSAGE = "Error during opening terminal app. You current command for opening terminal is: %s %s. Check if it is correct.";

    protected static final String OPTIONS_NOT_PROVIDED_MESSAGE = "You do not provide needed settings for OpenInTerminal plugin. Please set them before usage.";

    protected static final String NOTIFICATION_DISPLAY_ID = "OpenInVim";

    protected static final String NOTIFICATION_TITLE = "OpenInVim plugin";

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);


//        OpenInTerminalSettingsState openInTerminalSettingsState = OpenInTerminalSettings.getInstance().getState();

//        if (openInTerminalSettingsState != null) {

            String filePath = file.getCanonicalPath(); //getPath(e, file);

            String openInVimCommand = Joiner.on(" ").join(
                    "/usr/bin/gnome-terminal",
                    "-x bash -c \"vim\" ",
                    filePath);
        Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID, NOTIFICATION_TITLE,
                "Command: " + openInVimCommand,
                NotificationType.WARNING));
            try {
                Runtime.getRuntime().exec(openInVimCommand);
            } catch (IOException e1) {
                Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID, NOTIFICATION_TITLE,
                        e1.getMessage(),
                        NotificationType.WARNING));
            }
//        } else {
//            notifyAboutUnsetOptions();
//        }
    }

    protected String getPath(AnActionEvent e, VirtualFile file) {
        return file.isDirectory() ? file.getPath() : file.getParent().getPath();
    }

//    @NotNull
//    protected abstract String getPath(AnActionEvent e, VirtualFile file,
//                                      OpenInTerminalSettingsState openInTerminalSettingsState);

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setVisible(e.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }

//    protected void notifyAboutIncorrectOptions(OpenInTerminalSettingsState openInTerminalSettingsState) {
//        Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID, NOTIFICATION_TITLE,
//                String.format(ERROR_DURING_OPENING_TERMINAL_MESSAGE,
//                        openInTerminalSettingsState.getTerminalCommand(),
//                        openInTerminalSettingsState.getTerminalCommandOptions()),
//                NotificationType.ERROR));
//    }

    protected void notifyAboutUnsetOptions() {
        Notifications.Bus.notify(new Notification(NOTIFICATION_DISPLAY_ID, NOTIFICATION_TITLE,
                OPTIONS_NOT_PROVIDED_MESSAGE,
                NotificationType.WARNING));
    }
}
