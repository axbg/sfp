import com.sun.tools.javac.Main
import java.awt.*
import kotlin.system.exitProcess

fun main() {
    if (SystemTray.isSupported()) {
        val systemTray: SystemTray = SystemTray.getSystemTray()
//        val image: Image = Toolkit.getDefaultToolkit().getImage(Main::javaClass.javaClass.classLoader.getResource("icons/sfp.png"))
        val image: Image = Toolkit.getDefaultToolkit().getImage("")
        val popupMenu = PopupMenu()

        popupMenu.add(UIControl.bindRefreshRateAction())
        popupMenu.add(UIControl.bindCloseAction())

        val trayIcon = TrayIcon(image, "SFP", popupMenu)
        trayIcon.isImageAutoSize = true
        systemTray.add(trayIcon)

        while (true) {
            RobotControl.keepActive(UIControl.repeatDelay)
        }
    } else {
        exitProcess(1)
    }
}
