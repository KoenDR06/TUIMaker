package me.koendev.tuimaker.elements

import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.BorderStyle
import java.awt.Color

abstract class TUIElement {
    abstract val tui: TUI
    abstract val x: Int
    abstract val y: Int
    abstract val width: Int
    abstract val height: Int
    abstract val borderStyle: BorderStyle
    abstract val color: Color

    internal abstract fun draw()
    internal abstract fun writeContent()
}