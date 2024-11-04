package main.kotlin.os

import LockScreen

class DefaultStrategy : OSStrategy {
    override fun shouldAllowLockWithPassword(): Boolean = true
    override fun applyLockSettings(lockScreen: LockScreen) {}
}