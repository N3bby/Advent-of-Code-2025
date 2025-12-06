package day6

import ext.transpose
import util.Grid
import util.Region
import util.getSubGrid

fun Grid<Char>.splitByEmptyColumns(): List<Grid<Char>> {
    val emptyColumnIndices = this.columns.withIndex()
        .filter { (_, column) -> column.all { cell -> cell == ' ' } }
        .map { (index, _) -> index }

    val columnSpans = (listOf(-1 to emptyColumnIndices.first())
            + emptyColumnIndices.zipWithNext()
            + listOf(emptyColumnIndices.last() to this.width))

    return columnSpans
        .map { (minX, maxX) -> Region(minX + 1, maxX, 0, this.height) }
        .map { getSubGrid(it) }
}

data class CephalodProblem(val numbers: List<Long>, val operation: Long.(Long) -> Long) {

    fun solve(): Long {
        return numbers.reduce { acc, number -> acc.operation(number) }
    }

    companion object {
        fun fromGrid(grid: Grid<Char>, transposeNumbers: Boolean): CephalodProblem {
            val numbers = grid.rows.dropLast(1)
                .let { if (transposeNumbers) it.transpose() else it }
                .map { it.joinToString(separator = "").trim() }
                .map { it.toLong() }

            val operatorStr = grid.rows.last().joinToString("").trim()
            val operation: Long.(Long) -> Long = when (operatorStr) {
                "+" -> Long::plus
                "*" -> Long::times
                else -> throw IllegalArgumentException("Unknown operator: $operatorStr")
            }

            return CephalodProblem(numbers, operation)        }
    }
}

fun parseCaphalodProblems(input: String, transposeNumbers: Boolean = false): List<CephalodProblem> = Grid.fromString(input)
    .splitByEmptyColumns()
    .map { CephalodProblem.fromGrid(it, transposeNumbers) }
