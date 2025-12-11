package util

import util.Line.Companion.lineOf
import kotlin.math.abs

data class LongPosition(val x: Long, val y: Long)

data class DoublePosition(val x: Double, val y: Double)

data class Position(val x: Int, val y: Int) : Comparable<Position> {
    operator fun plus(offset: Offset): Position {
        return Position(x + offset.x, y + offset.y)
    }

    operator fun minus(offset: Offset): Position {
        return Position(x - offset.x, y - offset.y)
    }

    operator fun minus(offset: Position): Offset {
        return Offset(x - offset.x, y - offset.y)
    }

    val directNeightbours: List<Position>
        get() = listOf(
            this + Offset(1, 0),
            this + Offset(-1, 0),
            this + Offset(0, 1),
            this + Offset(0, -1),
        )

    val allNeighbours: List<Position>
        get() = directNeightbours + listOf(
            this + Offset(1, 1),
            this + Offset(-1, -1),
            this + Offset(-1, 1),
            this + Offset(1, -1),
        )

    val sectors: List<List<Position>>
        get() = listOf(
            listOf(this, this + Offset(1, 0), this + Offset(1, 1), this + Offset(0, 1)),
            listOf(this, this + Offset(0, 1), this + Offset(-1, 1), this + Offset(-1, 0)),
            listOf(this, this + Offset(-1, 0), this + Offset(-1, -1), this + Offset(0, -1)),
            listOf(this, this + Offset(0, -1), this + Offset(1, -1), this + Offset(1, 0)),
        )

    fun getCellsWithinRadius(radius: Int): Set<Position> {
        return Bounds(radius * 2 + 1, radius * 2 + 1).positions
            .map { this + Offset(it.x - radius, it.y - radius) }
            .filter { manhattanDistanceFrom(it) in (1..radius) }
            .toSet()
    }

    fun manhattanDistanceFrom(other: Position): Int {
        return abs(other.x - x) + abs(other.y - y)
    }

    fun toOffset(): Offset {
        return Offset(x, y)
    }

    override fun compareTo(other: Position): Int {
        val xDiff = x - other.x
        val yDiff = y - other.y
        if (xDiff != 0) return xDiff
        if (yDiff != 0) return yDiff
        return 0
    }
}


// TODO: Always include ends
// But do NOT count an intersection when the starting point of the line lies on it
sealed class Line(val pos1: Position, val pos2: Position) {

    abstract fun intersects(other: Line): Boolean
    abstract fun contains(position: Position): Boolean

    fun sharesPointWith(other: Line): Boolean {
        return pos1 == other.pos1 || pos1 == other.pos2 || pos2 == other.pos1 || pos2 == other.pos2
    }

    class HorizontalLine(pos1: Position, pos2: Position) : Line(pos1, pos2) {

        private val minX = minOf(pos1.x, pos2.x)
        private val maxX = maxOf(pos1.x, pos2.x)

        val y = pos1.y

        fun getXRange(): IntRange = minX + 1..<maxX

        override fun intersects(other: Line): Boolean = when {
            sharesPointWith(other) -> false
            other is HorizontalLine -> false
            other is VerticalLine -> {
                val intersectsHorizontally = other.x in getXRange()
                val intersectsVertically = y in other.getYRange()
                intersectsVertically && intersectsHorizontally
            }

            else -> TODO("Not possible")
        }

        override fun contains(position: Position): Boolean {
            return position.x in getXRange() && position.y == y
        }
    }

    class VerticalLine(pos1: Position, pos2: Position) : Line(pos1, pos2) {

        val minY = minOf(pos1.y, pos2.y)
        val maxY = maxOf(pos1.y, pos2.y)

        val x = pos1.x

        fun getYRange(): IntRange = minY + 1..<maxY

        override fun intersects(other: Line): Boolean = when {
            sharesPointWith(other) -> false
            other is VerticalLine -> false
            other is HorizontalLine -> {
                val intersectsVertically = other.y in getYRange()
                val intersectsHorizontally = x in other.getXRange()
                intersectsVertically && intersectsHorizontally
            }

            else -> TODO("Not possible")
        }

        override fun contains(position: Position): Boolean {
            return position.y in getYRange() && position.x == x
        }
    }

    companion object {
        fun lineOf(pos1: Position, pos2: Position): Line = when {
            pos1.x == pos2.x -> VerticalLine(pos1, pos2)
            pos1.y == pos2.y -> HorizontalLine(pos1, pos2)
            else -> throw IllegalArgumentException("No support for diagonal lines atm")
        }
    }
}

data class Rectangle(val corner1: Position, val corner2: Position) {

    val allCorners = listOf(
        corner1,
        Position(corner2.x, corner1.y),
        corner2,
        Position(corner1.x, corner2.y),
    )

    val edges = (allCorners.zipWithNext() + (allCorners.last() to allCorners.first()))
        .map { lineOf(it.first, it.second) }

    val area: Long
        get() {
            val (width, height) = (corner1 - corner2).abs() + Offset(1, 1)
            return width.toLong() * height.toLong()
        }

    fun contains(position: Position): Boolean {
        val minX = minOf(corner1.x, corner2.x)
        val maxX = maxOf(corner1.x, corner2.x)
        val minY = minOf(corner1.y, corner2.y)
        val maxY = maxOf(corner1.y, corner2.y)

        return position.x in minX + 1..<maxX && position.y in minY + 1..<maxY
    }

}

data class Offset(val x: Int, val y: Int) {
    fun abs(): Offset {
        return Offset(abs(x), abs(y))
    }

    operator fun plus(other: Offset): Offset {
        return Offset(x + other.x, y + other.y)
    }
}

enum class Direction(val offset: Offset) {
    RIGHT(Offset(1, 0)),
    RIGHT_UP(Offset(1, 1)),
    UP(Offset(0, 1)),
    LEFT_UP(Offset(-1, 1)),
    LEFT(Offset(-1, 0)),
    LEFT_DOWN(Offset(-1, -1)),
    DOWN(Offset(0, -1)),
    RIGHT_DOWN(Offset(1, -1));

    fun turnLeft(): Direction = when (this) {
        RIGHT -> UP
        DOWN -> RIGHT
        LEFT -> DOWN
        UP -> LEFT
        else -> throw IllegalArgumentException("Cannot rotate right from $this")
    }

    fun turnRight(): Direction = when (this) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        UP -> RIGHT
        else -> throw IllegalArgumentException("Cannot rotate right from $this")
    }

    fun toChar(): Char {
        return when (this) {
            RIGHT -> '>'
            UP -> '^'
            LEFT -> '<'
            DOWN -> 'v'
            else -> throw IllegalArgumentException("No char for $this")
        }
    }
}

fun List<Position>.sortedByPosition(): List<Position> {
    return sortedWith { a, b -> compareValuesBy(a, b, { it.y }, { it.x }) }
}
