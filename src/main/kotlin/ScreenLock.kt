import com.sun.tools.javac.Main
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.swing.*

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

        val iconLabel = JLabel(ImageIcon(frameIconImage))
        iconLabel.alignmentX = JLabel.CENTER_ALIGNMENT

        val infoLabel = JLabel("press ctrl + alt + shift + del to unlock the screen")
        infoLabel.alignmentX = JLabel.CENTER_ALIGNMENT

        val hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), null)

        val vBox = Box.createVerticalBox()
        vBox.add(Box.createVerticalGlue())
        vBox.add(iconLabel)
        vBox.add(infoLabel)
        vBox.add(Box.createVerticalGlue())

        title = frameTitle
        contentPane.background = Color.WHITE
        contentPane.cursor = hiddenCursor
        iconImage = frameIconImage

        add(vBox, BorderLayout.CENTER)
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e!!.isControlDown && e.isShiftDown && e.keyCode == 127) {
            dispose()
        }
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {}
}