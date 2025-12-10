package day9

import util.*
import util.Line.Companion.lineOf

typealias RedTile = Position

fun parseRedTiles(input: String): List<RedTile> = input
    .lines()
    .map { it.split(",").map(String::toInt) }
    .map { (x, y) -> Position(x, y) }

fun List<RedTile>.findLargestArea() = cartesianProduct(this, this)
    .removeDuplicates()
    .map { Rectangle(it.first, it.second) }
    .maxOf { it.area }

/** We'll *assume* a polygon with edges that don't cross over each other */
class Polygon(corners: List<Position>) {

    val xOutsideOfPolygon = corners.maxOf { it.x }

    val edges = (corners.zipWithNext() + (corners.last() to corners.first()))
        .map { lineOf(it.first, it.second) }

    fun contains(position: Position): Boolean {
        val ray = lineOf(position, Position(xOutsideOfPolygon, position.y))
        val intersections = edges.count { ray.intersects(it) }


        return intersections % 2 == 1
    }

    fun intersects(line: Line): Boolean {
        return edges.any { edge -> line.intersects(edge) }
    }

}
