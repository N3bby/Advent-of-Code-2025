package day7

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Grid
import util.readInput

class Day7KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
        """.trimIndent()

        val manifold = Grid
            .fromString(input, TachyonManifoldState::fromChar)
            .toManifold()

        val splittersReached = manifold.simulateBeam()
            .getBeamSplits()
            .groupBy { it.position }
            .count()

        assertThat(splittersReached).isEqualTo(21)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(7)

        val manifold = Grid
            .fromString(input, TachyonManifoldState::fromChar)
            .toManifold()

        val splittersReached = manifold.simulateBeam()
            .getBeamSplits()
            .groupBy { it.position }
            .count()

        assertThat(splittersReached).isEqualTo(21)
    }
}

