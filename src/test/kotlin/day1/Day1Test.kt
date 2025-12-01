package day1

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.readInput

class Day1KtTest {

    @Test
    fun `part 1 - example input`() {
        val input = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
        """.trimIndent()

        val rotations = parseRotations(input.lines())
        val (_, timesPointedAtZero) = Dial().rotate(rotations)

        assertThat(timesPointedAtZero).isEqualTo(3)
    }

    @Test
    fun `part 1 - puzzle input`() {
        val input = readInput(1)

        val rotations = parseRotations(input.lines())
        val (_, timesPointedAtZero) = Dial().rotate(rotations)

        assertThat(timesPointedAtZero).isEqualTo(1165)
    }

    @Test
    fun `part 2 - example input`() {
        val input = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
        """.trimIndent()

        val rotations = parseRotations(input.lines()).normalizeRotations()
        val (_, timesPointedAtZero) = Dial().rotate(rotations)

        assertThat(timesPointedAtZero).isEqualTo(6)
    }

    @Test
    fun `part 2 - puzzle input`() {
        val input = readInput(1)

        val rotations = parseRotations(input.lines()).normalizeRotations()
        val (_, timesPointedAtZero) = Dial().rotate(rotations)

        assertThat(timesPointedAtZero).isEqualTo(6496)
    }

}

