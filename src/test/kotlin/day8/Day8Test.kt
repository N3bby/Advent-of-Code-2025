package day8

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Position3D
import util.allValues
import util.cartesianProduct
import util.removeDuplicates

class Day8KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
        """.trimIndent()

        val junctionBoxes = input.lines()
            .map { it.split(",").map(String::toInt) }
            .map { (x, y, z) -> Position3D(x, y, z) }

        val pairs = cartesianProduct(junctionBoxes, junctionBoxes)
            .filter { it.first != it.second }
            .removeDuplicates()

        val tenShortestConnections = pairs
            .sortedBy { (first, second) -> first.distanceFrom(second) }
            .take(10)

        val unionFind = UnionFind.fromPairs(tenShortestConnections)
        val groups = unionFind.getGroups(pairs.allValues())

        assertThat(groups.size).isEqualTo(11)
        assertThat(groups.filter { it.size == 5 }.size).isEqualTo(1)
        assertThat(groups.filter { it.size == 4 }.size).isEqualTo(1)
        assertThat(groups.filter { it.size == 2 }.size).isEqualTo(2)
        assertThat(groups.filter { it.size == 1 }.size).isEqualTo(7)
    }

}

