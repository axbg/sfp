import com.sun.tools.javac.Main
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.WindowConstants

class ScreenLock(title: String) : JFrame(), KeyListener {
    init {
        createUI(title)
    }

    private fun createUI(title: String) {
        drawElements(title)
        addKeyListener(this)
        isUndecorated = true
        isAlwaysOnTop = true
        setSize(200, 200)
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        extendedState = Frame.MAXIMIZED_BOTH
    }

    private fun drawElements(frameTitle: String) {
        val frameIconImage = Toolkit.getDefaultToolkit().getImage(Main::javaClass.javaClass.classLoader.getResource("icon.png"))
        val infoLabel = JLabel("press ctrl + alt + shift + del to unlock the screen", SwingConstants.CENTER)
        val hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                Point(), null)

        title = frameTitle
        contentPane.background = Color.WHITE
        contentPane.cursor = hiddenCursor
        iconImage = frameIconImage
        add(infoLabel, BorderLayout.CENTER)
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e!!.isControlDown && e.isShiftDown && e.keyCode == 127) {
            dispose()
        }
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {}
}