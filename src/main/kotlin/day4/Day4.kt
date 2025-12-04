package day4

import day4.GridState.TOILET_PAPER_ROLL
import util.Grid
import util.Position

enum class GridState {
    EMPTY,
    TOILET_PAPER_ROLL;

    companion object {
        fun fromChar(char: Char) = when (char) {
            '.' -> EMPTY
            '@' -> TOILET_PAPER_ROLL
            else -> throw IllegalArgumentException("Unknown char: $char")
        }
    }
}

fun Grid<GridState>.getRemovableRolls(): List<Position> =
    getPositionsWhereValue { it == TOILET_PAPER_ROLL }
        .filter { position ->
            val neighbours = position.allNeighbours.filter { isInBounds(it) }
            neighbours.count { getAtPosition(it) == TOILET_PAPER_ROLL } < 4
        }

fun Grid<GridState>.findReachableRollCount() = getRemovableRolls().count()

typealias RemovedRolls = Int

fun Grid<GridState>.removeMostRollsOfPaper(): RemovedRolls {
    var removedRolls = 0

    do {
        val removableRolls = getRemovableRolls()
        removableRolls.forEach { setAtPosition(it, GridState.EMPTY) }
        removedRolls += removableRolls.size
    } while (removableRolls.isNotEmpty())

    return removedRolls
}