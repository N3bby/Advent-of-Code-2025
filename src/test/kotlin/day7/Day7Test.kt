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

        val (simulatedBeam, splittersReached) = manifold.simulateBeam()

        assertThat(splittersReached).isEqualTo(21)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(7)

        val manifold = Grid
            .fromString(input, TachyonManifoldState::fromChar)
            .toManifold()

        val (simulatedBeam, splittersReached) = manifold.simulateBeam()

        assertThat(splittersReached).isEqualTo(1579)
    }

    @Test
    fun `part 2 - example input`() {
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

        val (simulatedBeam, _) = manifold.simulateBeam()
        val timelines = simulatedBeam.paths.value

        assertThat(timelines).isEqualTo(40)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(7)

        val manifold = Grid
            .fromString(input, TachyonManifoldState::fromChar)
            .toManifold()

        val (simulatedBeam, _) = manifold.simulateBeam()
        val timelines = simulatedBeam.paths.value

        assertThat(timelines).isEqualTo(13418215871354)
    }
}

