import com.sun.tools.javac.Main
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.image.BufferedImage
import javax.swing.*

class LockScreen(title: String) : JFrame(), KeyListener {
    private var password: String? = null

    init {
        createUI(title)
    }

    fun readPassword(): Boolean {
        this.password = showPasswordPrompt()
        return this.password != null
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
        val frameIconImage =
            Toolkit.getDefaultToolkit().getImage(Main::javaClass.javaClass.classLoader.getResource("icon.png"))
        val hiddenCursor = Toolkit.getDefaultToolkit()
            .createCustomCursor(BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), null)

        title = frameTitle
        contentPane.background = UIControl.lockScreenColor
        contentPane.cursor = hiddenCursor
        iconImage = frameIconImage

        add(drawBox(frameIconImage), BorderLayout.CENTER)
    }

    private fun drawBox(image: Image): Box {
        val iconLabel = JLabel(scaleImage(image, 150, 150))
        iconLabel.alignmentX = JLabel.CENTER_ALIGNMENT

        val infoLabel = JLabel("press cmd/ctrl + shift + backspace to unlock the screen")
        infoLabel.alignmentX = JLabel.CENTER_ALIGNMENT
        infoLabel.foreground = if (UIControl.lockScreenColor == Color.BLACK) Color.WHITE else Color.BLACK

        val vBox = Box.createVerticalBox()
        vBox.add(Box.createVerticalGlue())
        vBox.add(iconLabel)
        vBox.add(infoLabel)
        vBox.add(Box.createVerticalGlue())

        return vBox
    }

    private fun scaleImage(image: Image, w: Int, h: Int): ImageIcon {
        val resizedImg = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val g2 = resizedImg.createGraphics()

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2.drawImage(image, 0, 0, w, h, null)
        g2.dispose()

        return ImageIcon(resizedImg)
    }

    private fun showPasswordPrompt(): String? {
        val passwordField: JTextField = JPasswordField()
        val ob = arrayOf(JLabel("Password"), passwordField)

        val result = JOptionPane.showOptionDialog(
            this,
            ob,
            "sfp - Screen Lock",
            JOptionPane.OK_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            arrayOf("OK"),
            null
        )

        if (result == JOptionPane.OK_OPTION) {
            if (passwordField.text != null && passwordField.text.isNotBlank()) {
                return passwordField.text
            }

            JOptionPane.showMessageDialog(this, "Password cannot be blank")
        }

        return null
    }

    override fun keyPressed(e: KeyEvent?) {
        if (e!!.isMetaDown && e.isShiftDown && e.keyCode == KeyEvent.VK_BACK_SPACE) {
            if (this.password != null && !this.password.equals(showPasswordPrompt())) {
                return
            }

            dispose()
        }
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyReleased(e: KeyEvent?) {}
}