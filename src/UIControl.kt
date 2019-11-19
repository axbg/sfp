import RefreshRateEnum.*
import java.awt.Menu
import java.awt.MenuItem
import kotlin.system.exitProcess

class UIControl {
    companion object {
        var repeatDelay: Long = 50000

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
            refreshRateAction.add(bindDelayControl(thirtySeconds, THIRTY_SECONDS))
            refreshRateAction.add(bindDelayControl(oneMinute, ONE_MINUTE))
            refreshRateAction.add(bindDelayControl(fiveMinutes, FIVE_MINUTES))
            refreshRateAction.add(bindDelayControl(tenMinutes, TEN_MINUTES))
            refreshRateAction.add(bindDelayControl(thirtyMinutes, THIRTY_MINUTES))

            return refreshRateAction
        }

        private fun bindDelayControl(menuItem: MenuItem, delay: RefreshRateEnum): MenuItem {
            menuItem.addActionListener {
                repeatDelay = delay.refreshRate
            }
            return menuItem
        }
    }
}