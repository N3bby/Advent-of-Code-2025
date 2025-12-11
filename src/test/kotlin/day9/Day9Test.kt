package day9

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day9KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
        """.trimIndent()

        val largestArea = parseRedTiles(input).findLargestArea()

        assertThat(largestArea).isEqualTo(50)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(9)

        val largestArea = parseRedTiles(input).findLargestArea()

        assertThat(largestArea).isEqualTo(4755278336)
    }

    @Test
    fun `part 2 - example input`() {
        val input = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
        """.trimIndent()
        val tiles = parseRedTiles(input)

        val largestFullyContainedArea = Polygon(tiles)
            .findFullyContainedRectangles(tiles.getAllPossibleRectangles())
            .maxOf { it.area }

        assertThat(largestFullyContainedArea).isEqualTo(24)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(9)
        val tiles = parseRedTiles(input)

        val largestFullyContainedArea = Polygon(tiles)
            .findFullyContainedRectangles(tiles.getAllPossibleRectangles())
            .maxOf { it.area }

        assertThat(largestFullyContainedArea).isEqualTo(1534043700)

    }
}

