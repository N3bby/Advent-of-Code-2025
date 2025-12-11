package day10

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day10KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
        """.trimIndent()

        val machines = parseMachines(input)
        val fewestButtonPressesForAllMachines = machines.sumOf { it.getFewestButtonPressesForIndicators() }

        assertThat(fewestButtonPressesForAllMachines).isEqualTo(7)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(10)

        val machines = parseMachines(input)
        val fewestButtonPressesForAllMachines = machines.sumOf { it.getFewestButtonPressesForIndicators() }

        assertThat(fewestButtonPressesForAllMachines).isEqualTo(419)
    }

    @Test
    fun `part 2 - example input`() {
        val input = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
        """.trimIndent()

        val machines = parseMachines(input)
        val fewestButtonPressesForAllMachines = machines.sumOf { it.getFewestButtonPressesForJoltageLA() }

        assertThat(fewestButtonPressesForAllMachines).isEqualTo(33)
    }

    @Test
    fun `part 2 - simplified input`() {
        val input = """
            [...] (0,1) (1,2) (0) (2) {2,2,2}
        """.trimIndent()

        val machine = parseMachines(input).first()

        assertThat(machine.getFewestButtonPressesForJoltageLA()).isEqualTo(4)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(10)

        val machines = parseMachines(input)
        val fewestButtonPressesForAllMachines = machines.sumOf { it.getFewestButtonPressesForJoltageLA() }

        // Too low: 18341
        assertThat(fewestButtonPressesForAllMachines).isEqualTo(33)
    }
}

