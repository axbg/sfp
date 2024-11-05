package main.kotlin.os

class OSStrategyFactory {
    companion object {
        fun obtainCurrentStrategy(): OSStrategy {
            val os = System.getProperty("os.name").lowercase()

            if (os.contains("mac")) {
                return MacStrategy()
            }

            return DefaultStrategy()
        }
    }
}