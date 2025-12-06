package day6

import util.Grid
import util.Region
import util.getSubGrid

fun Grid<Char>.splitByEmptyColumns(): List<Grid<Char>> {
    val emptyColumnIndices = this.columns.withIndex()
        .filter { (_, column) -> column.all { cell -> cell == ' ' } }
        .map { (index, _) -> index }

    val horizontalRegions = (listOf(-1 to emptyColumnIndices.first())
            + emptyColumnIndices.zipWithNext()
            + listOf(emptyColumnIndices.last() to this.width))

    return horizontalRegions
        .map { (minX, maxX) -> Region(minX + 1, maxX, 0, this.height) }
        .map { getSubGrid(it) }
}

data class CephalodProblem(val numbers: List<Int>, val operation: Int.(Int) -> Int) {

    fun solve(): Int {
        return numbers.reduce { acc, number -> acc.operation(number) }
    }

    companion object {
        fun fromGrid(grid: Grid<Char>): CephalodProblem {
            val rows = grid.rows.map { it.joinToString(separator = "").trim() }
            val numbers = rows.dropLast(1).map { it.toInt() }
            val operation: Int.(Int) -> Int = when (rows.last()) {
                "+" -> Int::plus
                "*" -> Int::times
                else -> throw IllegalArgumentException("Unknown operator: ${rows.last()}")
            }

            return CephalodProblem(numbers, operation)
        }
    }
}
