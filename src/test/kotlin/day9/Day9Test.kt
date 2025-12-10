package day9

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Rectangle
import util.cartesianProduct
import util.readInput
import util.removeDuplicates

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
//        val input = """
//            7,1
//            11,1
//            11,7
//            9,7
//            9,5
//            2,5
//            2,3
//            7,3
//        """.trimIndent()
        val input = readInput(9)

        val tiles = parseRedTiles(input)
        val polygon = Polygon(tiles)

        val possibleRectangles = cartesianProduct(tiles, tiles).removeDuplicates()
            .map { Rectangle(it.first, it.second) }

        val fullyContainedRectangles = possibleRectangles
            // Normally we'd have to check if the points of the rectangle are inside the polygon.
            // But this is redundant since our candidate rectangle corners are always on the edges of the polygon.
            .filter { rectangle -> rectangle.edges.none { polygon.intersects(it) } }
            .map { rectangle ->
//                val grid = Grid.generate(Bounds(14, 9)) { position ->
//                    when {
//                        rectangle.corner1 == position || rectangle.corner2 == position -> 'O'
//                        tiles.contains(position) -> '#'
//                        polygon.edges.any { edge -> edge.contains(position) } -> 'X'
//                        else -> '.'
//                    }
//                }
//                println(grid.print())

                rectangle
            }

        val largestFullyContainedArea = fullyContainedRectangles
            .maxOf { it.area }

        assertThat(largestFullyContainedArea).isEqualTo(24)
    }
}

