package day8

import ext.multiplicationOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

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

        val junctionBoxes = parseJunctionBoxes(input)
        val circuits = getCircuits(junctionBoxes, 10)

        assertThat(circuits.size).isEqualTo(11)
        assertThat(circuits.ofSize(5).count()).isEqualTo(1)
        assertThat(circuits.ofSize(4).count()).isEqualTo(1)
        assertThat(circuits.ofSize(2).count()).isEqualTo(2)
        assertThat(circuits.ofSize(1).count()).isEqualTo(7)

        val result = circuits
            .sortedByDescending { it.size }
            .take(3)
            .multiplicationOf { it.size }

        assertThat(result).isEqualTo(40)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(8)

        val junctionBoxes = parseJunctionBoxes(input)
        val circuits = getCircuits(junctionBoxes, 1000)

        val result = circuits
            .sortedByDescending { it.size }
            .take(3)
            .multiplicationOf { it.size }

        assertThat(result).isEqualTo(46398)
    }

    @Test
    fun `part 2 - example input`() {
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

        val junctionBoxes = parseJunctionBoxes(input)
        val connection = getLastConnectionThatCompletesFullCircuit(junctionBoxes)

        assertThat(connection.first.x * connection.second.x).isEqualTo(25272)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(8)

        val junctionBoxes = parseJunctionBoxes(input)
        val connection = getLastConnectionThatCompletesFullCircuit(junctionBoxes)

        assertThat(connection.first.x.toLong() * connection.second.x.toLong()).isEqualTo(8141888143)

    }
}

