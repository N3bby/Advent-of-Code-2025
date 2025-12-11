package util

data class Rectangle(val corner1: Position, val corner2: Position) {

    val bounds = Region(
        minOf(corner1.x, corner2.x),
        maxOf(corner1.x, corner2.x),
        minOf(corner1.y, corner2.y),
        maxOf(corner1.y, corner2.y)
    )

    val allCorners = listOf(
        corner1,
        Position(corner2.x, corner1.y),
        corner2,
        Position(corner1.x, corner2.y),
    )

    val edges = (allCorners.zipWithNext() + (allCorners.last() to allCorners.first()))
        .map { Line.Companion.lineOf(it.first, it.second) }

    val area: Long
        get() {
            val (width, height) = (corner1 - corner2).abs() + Offset(1, 1)
            return width.toLong() * height.toLong()
        }

    fun strictlyContains(position: Position): Boolean {
        return position.x in bounds.minX + 1..<bounds.maxX && position.y in bounds.minY + 1..<bounds.maxY
    }

}
