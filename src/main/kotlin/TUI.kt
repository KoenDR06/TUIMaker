package me.koendev.tuimaker

import me.koendev.tuimaker.elements.Box
import java.awt.Color

class TUI(val width: Int, val height: Int) {
    var shown = false

    val elements: MutableList<Box> = mutableListOf()
    var eventHandler: (char: Char) -> Unit = { char ->
        when (char) {
            'q' -> throw Exception("Quit application")
            else -> {}
        }
    }
    var closeHandler: () -> Unit = {}

    fun show() {
        shown = true
        // Prints: Enable alternate buffer; Hide cursor
        print("\u001B[?1049h\u001B[?25l")
        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty raw </dev/tty"))

        for (element in elements) {
            element.draw()
            element.writeContent()
        }

        while (true) {
            val char = System.`in`.read().toChar()
            print('\b')

            try {
                eventHandler(char)
            } catch (e: CloseException) {
                close()
                break
            }
        }
    }

    fun close() {
        shown = false

        Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "stty -raw </dev/tty"))
        // Prints: Disable alternate buffer; Show cursor
        print(resetColor() + "\u001B[?1049l\u001B[?25h")

        // This shit doesn't work unless it waits for a bit, hella strange
        Thread.sleep(10)
        closeHandler()
    }

    companion object {
        fun moveCursor(x: Int, y: Int) = "\u001b[$y;${x}H"
        fun setForegroundColor(c: Color) = "\u001B[38;2;${c.red};${c.green};${c.blue};m"
        fun setBackgroundColor(c: Color) = "\u001B[48;2;${c.red};${c.green};${c.blue};m"
        fun resetColor() = "\u001B[0m"

        fun write(x: Int, y: Int, text: String, foreColor: Color, backColor: Color) {
            val finalText = text.replace("\n", "\u001B[E\u001B[${x-1}C")
            print(
                setForegroundColor(foreColor) +
                setBackgroundColor(backColor) +
                moveCursor(x, y) +
                finalText +
                resetColor()
            )
        }
    }
}
