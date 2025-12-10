package util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Line.Companion.lineOf

class LineTest {

    @Test
    //  |
    // -+-
    //  |
    fun intersects_middle_shouldBeTrue() {
        val line1 = lineOf(Position(0, 1), Position(2, 1))
        val line2 = lineOf(Position(1, 0), Position(1, 2))

        assertThat(line1.intersects(line2)).isTrue()
        assertThat(line2.intersects(line1)).isTrue()
    }

    @Test
    //   |
    // --+
    //   |
    fun intersects_end_shouldBeFalse() {
        val line1 = lineOf(Position(0, 1), Position(2, 1))
        val line2 = lineOf(Position(2, 0), Position(2, 2))

        assertThat(line1.intersects(line2)).isFalse()
        assertThat(line2.intersects(line1)).isFalse()
    }

    @Test
    // |
    // +--
    // |
    fun intersects_start_shouldBeFalse() {
        val line1 = lineOf(Position(0, 1), Position(2, 1))
        val line2 = lineOf(Position(0, 0), Position(0, 2))

        assertThat(line1.intersects(line2)).isFalse()
        assertThat(line2.intersects(line1)).isFalse()
    }

    @Test
    //   |
    // --|
    //   |
    fun intersects_doesntIntersect_shouldBeFalse() {
        val line1 = lineOf(Position(0, 1), Position(1, 1))
        val line2 = lineOf(Position(2, 0), Position(2, 2))

        assertThat(line1.intersects(line2)).isFalse()
        assertThat(line2.intersects(line1)).isFalse()
    }

}
