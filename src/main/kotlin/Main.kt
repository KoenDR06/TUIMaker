package me.koendev.tuimaker

import me.koendev.tuimaker.borders.SingleBorder
import me.koendev.tuimaker.elements.Box
import java.awt.Color

fun main(args: Array<String>) {
    val w = args[1].toInt()
    val h = args[0].toInt()

    val tui = TUI(w, h)
    val box = Box(tui, 1, 1, 20, 20, SingleBorder(), Color.WHITE, "This is a testing header")
    tui.elements.add(box)

    tui.show()
}