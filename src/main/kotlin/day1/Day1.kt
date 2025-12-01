package day1

import day1.RotationDirection.LEFT
import day1.RotationDirection.RIGHT

enum class RotationDirection {
    LEFT,
    RIGHT;

    companion object {
        fun fromString(value: String) = when (value) {
            "L" -> LEFT
            "R" -> RIGHT
            else -> throw IllegalArgumentException("Unknown direction: $value")
        }
    }
}

data class Rotation(val direction: RotationDirection, val distance: Int)

fun List<Rotation>.normalizeRotations(): List<Rotation> =
    flatMap { rotation ->
        (0 until rotation.distance).map { Rotation(rotation.direction, 1) }
    }

fun parseRotations(lines: List<String>) =
    lines.map {
        val direction = it.take(1)
        val distance = it.drop(1)
        Rotation(
            RotationDirection.fromString(direction),
            distance.toInt()
        )
    }

data class Dial(val pointingAt: Int = 50, val timesPointedAtZero: Int = 0) {

    fun rotate(rotation: Rotation): Dial {
        val nextPointingAt = when (rotation.direction) {
            LEFT -> pointingAt - rotation.distance
            RIGHT -> pointingAt + rotation.distance
        }.mod(100)

        return copy(
            pointingAt = nextPointingAt,
            timesPointedAtZero = if (nextPointingAt == 0) timesPointedAtZero + 1 else timesPointedAtZero
        )
    }

    fun rotate(sequence: List<Rotation>): Dial =
        sequence.fold(this) { dial, rotation -> dial.rotate(rotation) }

}
