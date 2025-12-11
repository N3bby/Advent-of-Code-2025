package day9

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import util.Line.Companion.lineOf
import util.Position

class PolygonTest {

    @Test
    fun contains_convex() {
        // +--+
        // |  |
        // |  |
        // +--+
        val polygon = Polygon(
            listOf(
                Position(0, 0),
                Position(3, 0),
                Position(3, 3),
                Position(0, 3)
            )
        )

        assertThat(polygon.contains(Position(0, 0))).isTrue()
        assertThat(polygon.contains(Position(1, 1))).isTrue()
        assertThat(polygon.contains(Position(-1, 1))).isFalse()
    }

    @Test
    fun contains_concave() {
        // +-+ +-+
        // | | | |
        // | +-+ |
        // |     |
        // +-----+
        val polygon = Polygon(
            listOf(
                Position(0, 0),
                Position(2, 0),
                Position(2, 2),
                Position(4, 2),
                Position(4, 0),
                Position(6, 0),
                Position(6, 4),
                Position(0, 4),
            )
        )

        assertThat(polygon.contains(Position(0, 0))).isTrue()
        assertThat(polygon.contains(Position(1, 1))).isTrue()
        assertThat(polygon.contains(Position(3, 0))).isFalse()
        assertThat(polygon.contains(Position(3, 1))).isFalse()
    }

    @Test
    fun intersects_convex() {
        // +--+
        // |  |
        // |  |
        // +--+
        val polygon = Polygon(
            listOf(
                Position(0, 0),
                Position(3, 0),
                Position(3, 3),
                Position(0, 3)
            )
        )

        val onEdgeWithoutIntersection = lineOf(Position(0, 0), Position(2, 0))
        assertThat(polygon.intersects(onEdgeWithoutIntersection)).isFalse()

        val onEdgeWithIntersection = lineOf(Position(0, 0), Position(4, 0))
        assertThat(polygon.intersects(onEdgeWithIntersection)).isTrue()

        val outside = lineOf(Position(5, 0), Position(8, 0))
        assertThat(polygon.intersects(outside)).isFalse()

        val through = lineOf(Position(-1, 3), Position(5, 3))
        assertThat(polygon.intersects(through)).isTrue()

        val fullyInside = lineOf(Position(1, 3), Position(2, 3))
        assertThat(polygon.intersects(fullyInside)).isFalse()

        val insideTouchingEdges = lineOf(Position(0, 3), Position(3, 3))
        assertThat(polygon.intersects(insideTouchingEdges)).isFalse()
    }
}
