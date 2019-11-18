import java.awt.*
import java.awt.event.ActionListener
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    try {
        if (SystemTray.isSupported()) {
            val systemTray: SystemTray = SystemTray.getSystemTray()
            val image: Image = Toolkit.getDefaultToolkit().getImage("download.png")
            val popupMenu = PopupMenu()
            val action = MenuItem("Close")

            action.addActionListener(ActionListener {
                exitProcess(0)
            })

            popupMenu.add(action)

            val trayIcon = TrayIcon(image, "Close", popupMenu)
            trayIcon.isImageAutoSize = true
            systemTray.add(trayIcon)
        }

        val repeatDelay: Long = if (args.size >= 2 && args[1] != null) args[1].toLong() else 2000
        RobotControl.keepActive(repeatDelay)
    } catch (ex: Exception) {
        println("Argument is not a valid number")
    }
}
