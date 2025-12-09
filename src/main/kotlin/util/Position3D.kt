package util

import kotlin.math.pow
import kotlin.math.sqrt

data class Position3D(val x: Int, val y: Int, val z: Int) {

    fun distanceFrom(other: Position3D): Double {
        return sqrt(
            (x.toDouble() - other.x.toDouble()).pow(2) +
            (y.toDouble() - other.y.toDouble()).pow(2) +
            (z.toDouble() - other.z.toDouble()).pow(2)
        )
    }

}
