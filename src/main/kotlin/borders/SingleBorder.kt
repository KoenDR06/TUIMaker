package me.koendev.tuimaker.borders

class SingleBorder: BorderStyle() {
    override val hor = '─'
    override val ver = '│'
    override val cul = '┌'
    override val cur = '┐'
    override val cbl = '└'
    override val cbr = '┘'
}