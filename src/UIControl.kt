import RefreshRateEnum.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Font
import java.awt.Menu
import java.awt.MenuItem
import kotlin.system.exitProcess

class UIControl {
    companion object {
        private val normalFont = Font("sans-serif", Font.PLAIN, 12)
        private val selectedFont = Font("sans-serif", Font.BOLD, 12)

        var repeatDelay: Long = 5000
        var isPaused: Boolean = false

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
            refreshRateAction.add(bindDelayControl(fiveSeconds, FIVE_SECONDS, selectedFont))
            refreshRateAction.add(bindDelayControl(tenSeconds, TEN_SECONDS))
            refreshRateAction.add(bindDelayControl(thirtySeconds, THIRTY_SECONDS))
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

        private fun bindDelayControl(menuItem: MenuItem, delay: RefreshRateEnum, font: Font = normalFont): MenuItem {
            menuItem.font = font
            menuItem.addActionListener {
                repeatDelay = delay.refreshRate
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