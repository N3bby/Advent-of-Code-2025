package util

sealed class Line(val pos1: Position, val pos2: Position) {

    /** This does not count "grazing" or collinear lines as intersecting */
    abstract fun intersects(other: Line): Boolean
    abstract fun contains(position: Position): Boolean

    class HorizontalLine(pos1: Position, pos2: Position) : Line(pos1, pos2) {

        private val minX = minOf(pos1.x, pos2.x)
        private val maxX = maxOf(pos1.x, pos2.x)

        val y = pos1.y

        fun getXRange(): IntRange = minX + 1..<maxX

        override fun intersects(other: Line): Boolean = when (other) {
            is HorizontalLine -> false
            is VerticalLine -> {
                val intersectsHorizontally = other.x in getXRange()
                val intersectsVertically = y in other.getYRange()
                intersectsVertically && intersectsHorizontally
            }
        }

        override fun contains(position: Position): Boolean {
            return position.x in getXRange() && position.y == y
        }
    }

    class VerticalLine(pos1: Position, pos2: Position) : Line(pos1, pos2) {

        private val minY = minOf(pos1.y, pos2.y)
        private val maxY = maxOf(pos1.y, pos2.y)

        val x = pos1.x

        fun getYRange(): IntRange = minY + 1..<maxY

        override fun intersects(other: Line): Boolean = when (other) {
            is VerticalLine -> false
            is HorizontalLine -> {
                val intersectsVertically = other.y in getYRange()
                val intersectsHorizontally = x in other.getXRange()
                intersectsVertically && intersectsHorizontally
            }
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
