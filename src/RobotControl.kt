import java.awt.Robot
import java.awt.event.KeyEvent

class RobotControl {
    companion object {
        private val robot = Robot()
        fun keepActive() {
            while (true) {
                if (!UIControl.isPaused) {
                    robot.keyPress(KeyEvent.VK_F13)
                    robot.keyRelease(KeyEvent.VK_F13)
                    Thread.sleep(UIControl.repeatDelay)
                } else {
                    break
                }
            }
        }
    }
}