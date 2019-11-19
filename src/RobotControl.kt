import java.awt.Robot
import java.awt.event.KeyEvent

class RobotControl {
    companion object {
        private val robot = Robot()
        fun keepActive(repeatDelay: Long) {
            robot.keyPress(KeyEvent.VK_ALT)
            robot.keyRelease(KeyEvent.VK_ALT)
            Thread.sleep(repeatDelay)
        }
    }
}