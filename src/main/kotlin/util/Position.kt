package util

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class LongPosition(val x: Long, val y: Long)

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

data class Line(val pos1: Position, val pos2: Position) {

    val manhattanDistance: Int get() = pos1.manhattanDistanceFrom(pos2)

    fun crossesX(x: Int): Boolean {
        return x in (min(pos1.x, pos2.x)..max(pos1.x, pos2.x))
    }

    fun crossesY(y: Int): Boolean {
        return y in (min(pos1.y, pos2.y)..max(pos1.y, pos2.y))
    }

}

data class Offset(val x: Int, val y: Int) {
    fun abs(): Offset {
        return Offset(abs(x), abs(y))
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
