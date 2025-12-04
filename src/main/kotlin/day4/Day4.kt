package day4

import day4.GridState.TOILET_PAPER_ROLL
import util.Grid

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

fun Grid<GridState>.findReachableRollCount() =
    getPositionsWhereValue { it == TOILET_PAPER_ROLL }
        .count { position ->
            val neighbours = position.allNeighbours.filter { isInBounds(it) }
            neighbours.count { getAtPosition(it) == TOILET_PAPER_ROLL } < 4
        }