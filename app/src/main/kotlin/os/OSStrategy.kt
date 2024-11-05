package main.kotlin.os

import LockScreen

interface OSStrategy {
    fun shouldAllowLockWithPassword(): Boolean
    fun applyLockSettings(lockScreen: LockScreen)
}