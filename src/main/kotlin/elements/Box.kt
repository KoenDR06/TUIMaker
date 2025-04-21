package me.koendev.tuimaker.elements

import me.koendev.tuimaker.TUI
import me.koendev.tuimaker.borders.BorderStyle
import java.awt.Color

class Box(
    val tui: TUI,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    borderStyle: BorderStyle,
    borderColor: Color,
    header: String? = null,
    foreColor: Color = Color.WHITE,
    backColor: Color = Color.BLACK,
    val padding: Pair<Int, Int> = Pair(2, 1)
) {
    private var _borderStyle: BorderStyle
    var borderStyle: BorderStyle
        get() = _borderStyle
        set(value) {
            _borderStyle = value
            draw()
        }

    private var _borderColor: Color
    var borderColor: Color
        get() = _borderColor
        set(value) {
            _borderColor = value
            draw()
        }

    private var _foreColor: Color
    var foreColor: Color
        get() = _foreColor
        set(value) {
            _foreColor = value
            writeContent()
        }

    private var _backColor: Color
    var backColor: Color
        get() = _backColor
        set(value) {
            _backColor = value
            writeContent()
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
        _borderColor = borderColor
        _foreColor = foreColor
        _backColor = backColor
        _header = header
    }

    fun draw() {
        if (!tui.shown) return
        if (x < 1 || x-1+width > tui.width || y < 1 || y-1+height > tui.height ) throw Exception("Box falls (partly) out of bounds")

        var str = ""
        str += borderStyle.cul + borderStyle.hor.toString().repeat(width-2) + borderStyle.cur

        for (line in y + 1..<y + height - 1) {
            str += TUI.moveCursor(x, line) + borderStyle.ver +
                    TUI.moveCursor(x + width - 1, line) + borderStyle.ver
        }

        str += TUI.moveCursor(x, y + height - 1) + borderStyle.cbl + borderStyle.hor.toString().repeat(width-2) + borderStyle.cbr

        TUI.write(x, y, str, borderColor, backColor)

        if (header != null) {
            TUI.write(x + 2, y, "${borderStyle.cur} ${header!!.take(width-8)} ${borderStyle.cul}", borderColor, backColor)
        }
    }

    fun writeContent() {
        if (!tui.shown) return
        TUI.write(x+1, y+1, (" ".repeat(width-2)+'\n').repeat(height-2), foreColor, backColor)

        val text = content.split('\n')
            .map { it.chunked(width - 2*(padding.first+1)) }
            .fold(listOf<String>()) { acc, curr -> if (curr.isEmpty()) acc + listOf("") else acc + curr } // This is ugly but preserves \n\n
            .take(height-padding.second-1)
            .joinToString("\n")

        TUI.write(x+padding.first+1, y+padding.second+1, text, foreColor, backColor)
    }
}