package day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Grid
import util.readInput

class Day4KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
        """.trimIndent()
        val grid = Grid.fromString(input, GridState::fromChar)

        assertThat(grid.findReachableRollCount()).isEqualTo(13)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(4)
        val grid = Grid.fromString(input, GridState::fromChar)

        assertThat(grid.findReachableRollCount()).isEqualTo(1376)
    }



}

