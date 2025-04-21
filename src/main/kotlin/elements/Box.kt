package me.koendev.tuimaker.elements

import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.BorderStyle
import java.awt.Color

class Box(
    override val tui: TUI,
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    borderStyle: BorderStyle,
    color: Color,
    header: String? = null
) : TUIElement() {
    private var _borderStyle: BorderStyle
    override var borderStyle: BorderStyle
        get() = _borderStyle
        set(value) {
            _borderStyle = value
            draw()
        }

    private var _color: Color
    override var color: Color
        get() = _color
        set(value) {
            _color = value
            draw()
        }

    private var _header: String?
    var header: String?
        get() = _header
        set(value) {
            _header = value
            draw()
        }

    private var _content: String = ""
    var content: String
        get() = _content
        set(value) {
            _content = value
            writeContent()
        }

    init {
        _borderStyle = borderStyle
        _color = color
        _header = header
    }

    override fun draw() {
        if (x < 1 || x-1+width > tui.width || y < 1 || y-1+height > tui.height ) throw Exception("Box falls (partly) out of bounds")

        var str = ""
        str += borderStyle.cul + borderStyle.hor.toString().repeat(width-2) + borderStyle.cur

        for (line in y + 1..<y + height - 1) {
            str += TUI.moveCursor(x, line) + borderStyle.ver +
                    TUI.moveCursor(x + width - 1, line) + borderStyle.ver
        }

        str += TUI.moveCursor(x, y + height - 1) + borderStyle.cbl + borderStyle.hor.toString().repeat(width-2) + borderStyle.cbr

        TUI.write(x, y, str, color)

        if (header != null) {
            TUI.write(x + 2, y, "${borderStyle.cur} ${header!!.take(width-8)} ${borderStyle.cul}", color)
        }
    }

    override fun writeContent() {
        val text = content.split('\n')
            .map { it.chunked(width - 4) }
            .flatten()
            .take(height-2)
            .joinToString("\n")

        TUI.write(x+2, y+1, text, color)
    }
}