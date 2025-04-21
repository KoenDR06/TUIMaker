package me.koendev.tuimaker

import me.koendev.tuimaker.elements.TUIElement
import java.awt.Color

class TUI(val width: Int, val height: Int) {
    val elements: MutableList<TUIElement> = mutableListOf()
    var eventHandler: (char: Char) -> Unit = { char ->
        when (char) {
            'q' -> throw Exception("Quit application")
            else -> {}
        }
    }
    var closeHandler: () -> Unit = {}

    fun show() {
        // Prints: Enable alternate buffer; Hide cursor
        print("\u001B[?1049h\u001B[?25l")
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw </dev/tty"))

        for (element in elements) {
            element.draw()
        }

        while (true) {
            val char = System.`in`.read().toChar()
            print('\b')

            try {
                eventHandler(char)
            } catch (e: Exception) {
                Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw </dev/tty"))
                // Prints: Disable alternate buffer; Show cursor
                print(resetColor() + "\u001B[?1049l\u001B[?25h")

                closeHandler()
                break
            }
        }
    }

    companion object {
        fun moveCursor(x: Int, y: Int) = "\u001b[$y;${x}H"
        fun setForegroundColor(c: Color) = "\u001B[38;2;${c.red};${c.green};${c.blue};m"
        fun setBackgroundColor(c: Color) = "\u001B[48;2;${c.red};${c.green};${c.blue};m"
        fun resetColor() = "\u001B[0m"

        fun write(x: Int, y: Int, text: String, color: Color) {
            val finalText = text.replace("\n", "\u001B[E\u001B[${x-1}C")
            print(
                setForegroundColor(color) +
                moveCursor(x, y) +
                finalText +
                resetColor()
            )
        }
    }
}