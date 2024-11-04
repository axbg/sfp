import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import main.kotlin.enums.LockScreenColorEnum
import main.kotlin.enums.LockScreenColorEnum.BLACK
import main.kotlin.enums.LockScreenColorEnum.WHITE
import main.kotlin.enums.RefreshRateEnum
import main.kotlin.enums.RefreshRateEnum.*
import main.kotlin.os.OSStrategy
import main.kotlin.os.OSStrategyFactory
import java.awt.Color
import java.awt.Font
import java.awt.Menu
import java.awt.MenuItem
import kotlin.system.exitProcess

class UIControl {
    companion object {
        private val normalFont = Font(Font.SANS_SERIF, Font.PLAIN, 12)
        private val selectedFont = Font(Font.SERIF, Font.BOLD, 13)

        var repeatDelay: Long = THIRTY_SECONDS.refreshRate
        var lockScreenColor: Color = Color.BLACK
        var isPaused: Boolean = false
        var osStrategy: OSStrategy = OSStrategyFactory.obtainCurrentStrategy()

        internal fun bindCloseAction(): MenuItem {
            val closeAction = MenuItem("Close")
            closeAction.addActionListener {
                exitProcess(0)
            }
            return closeAction
        }

        internal fun bindRefreshRateAction(): Menu {
            val oneSecond = MenuItem("1 second")
            val fiveSeconds = MenuItem("5 seconds")
            val tenSeconds = MenuItem("10 seconds")
            val thirtySeconds = MenuItem("30 seconds")
            val oneMinute = MenuItem("1 minute")
            val fiveMinutes = MenuItem("5 minutes")
            val tenMinutes = MenuItem("10 minutes")
            val thirtyMinutes = MenuItem("30 minutes")

            val refreshRateAction = Menu("Refresh rate")
            refreshRateAction.add(bindDelayControl(oneSecond, ONE_SECOND))
            refreshRateAction.add(bindDelayControl(fiveSeconds, FIVE_SECONDS))
            refreshRateAction.add(bindDelayControl(tenSeconds, TEN_SECONDS))
            refreshRateAction.add(bindDelayControl(thirtySeconds, THIRTY_SECONDS, selectedFont))
            refreshRateAction.add(bindDelayControl(oneMinute, ONE_MINUTE))
            refreshRateAction.add(bindDelayControl(fiveMinutes, FIVE_MINUTES))
            refreshRateAction.add(bindDelayControl(tenMinutes, TEN_MINUTES))
            refreshRateAction.add(bindDelayControl(thirtyMinutes, THIRTY_MINUTES))

            return refreshRateAction
        }

        internal fun bindPauseAction(): MenuItem {
            val pauseAction = MenuItem("Pause")
            pauseAction.addActionListener {
                isPaused = !isPaused
                if (!isPaused) {
                    pauseAction.label = "Pause"
                    GlobalScope.launch {
                        RobotControl.keepActive()
                    }
                } else {
                    pauseAction.label = "Resume"
                }
            }
            return pauseAction
        }

        internal fun bindLockScreen(): Menu {
            val lockScreenMenu = Menu("Lock screen")

            val lockScreenActionNoPassword = MenuItem("Without password")
            lockScreenActionNoPassword.addActionListener {
                generateLockScreen(false)
            }
            lockScreenMenu.add(lockScreenActionNoPassword)

            if (osStrategy.shouldAllowLockWithPassword()) {
                val lockScreenActionPassword = MenuItem("With password")
                lockScreenActionPassword.addActionListener {
                    generateLockScreen(true)
                }
                lockScreenMenu.add(lockScreenActionPassword)
            }

            return lockScreenMenu
        }

        internal fun bindLockScreenColor(): Menu {
            val black = MenuItem("Black")
            val white = MenuItem("White")

            val lockScreenColor = Menu("Lock screen color")
            lockScreenColor.add(bindLockScreenColorControl(black, BLACK))
            lockScreenColor.add(bindLockScreenColorControl(white, WHITE))

            return lockScreenColor
        }

        private fun generateLockScreen(withPassword: Boolean) {
            val lockScreen = LockScreen("SFP Screen Lock")

            if (withPassword && !lockScreen.readPassword()) {
                lockScreen.dispose()
                return
            }

            lockScreen.isVisible = true
            osStrategy.applyLockSettings(lockScreen)
        }

        private fun bindDelayControl(menuItem: MenuItem, delay: RefreshRateEnum, font: Font = normalFont): MenuItem {
            menuItem.font = font
            menuItem.addActionListener {
                repeatDelay = delay.refreshRate
                unselectAll(menuItem.parent as Menu)
                menuItem.font = selectedFont
            }
            return menuItem
        }

        private fun bindLockScreenColorControl(
            menuItem: MenuItem,
            color: LockScreenColorEnum,
            font: Font = normalFont
        ): MenuItem {
            menuItem.font = font
            menuItem.addActionListener {
                lockScreenColor = color.color
                unselectAll(menuItem.parent as Menu)
                menuItem.font = selectedFont
            }
            return menuItem
        }

        private fun unselectAll(menu: Menu) {
            for (i in 0 until menu.itemCount) {
                menu.getItem(i).font = normalFont
            }
        }
    }
}