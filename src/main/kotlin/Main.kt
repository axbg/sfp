import com.sun.tools.javac.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.*
import kotlin.system.exitProcess

fun main() {
    if (SystemTray.isSupported()) {
        val systemTray: SystemTray = SystemTray.getSystemTray()
        val popupMenu = PopupMenu()
        val image: Image = Toolkit.getDefaultToolkit().getImage(Main::javaClass.javaClass.classLoader.getResource("icon.png"))

        popupMenu.add(UIControl.bindPauseAction())
        popupMenu.add(UIControl.bindRefreshRateAction())
        popupMenu.add(UIControl.bindLockScreen())
        popupMenu.add(UIControl.bindCloseAction())

        val trayIcon = TrayIcon(image, "SFP", popupMenu)
        trayIcon.isImageAutoSize = true
        systemTray.add(trayIcon)

        GlobalScope.launch {
            RobotControl.keepActive()
        }
    } else {
        exitProcess(1)
    }
}