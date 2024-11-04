package main.kotlin.os

import LockScreen
import java.awt.GraphicsEnvironment

class MacStrategy : OSStrategy {
    override fun shouldAllowLockWithPassword(): Boolean = false
    override fun applyLockSettings(lockScreen: LockScreen) {
        val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice

        try {
            gd.fullScreenWindow = lockScreen
            lockScreen.isVisible = true
        } catch (e: Exception) {
            gd.fullScreenWindow = null
        }
    }
}