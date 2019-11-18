import java.awt.Robot
import java.awt.event.KeyEvent

class RobotControl {
    companion object {
        fun keepActive(repeatDelay: Long) {
            val robot = Robot()
            while (true) {
                robot.keyPress(KeyEvent.VK_ALT)
                robot.keyRelease(KeyEvent.VK_ALT)
                Thread.sleep(repeatDelay)
            }
        }
    }
}