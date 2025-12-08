package day7

import day7.TachyonBeamNode.Beam
import day7.TachyonBeamNode.BeamSplitter
import ext.stackOf
import util.Grid
import util.Offset
import util.Position

enum class TachyonManifoldState {
    EMPTY,
    SPLITTER,
    START;

    companion object {
        fun fromChar(char: Char) = when (char) {
            'S' -> START
            '.' -> EMPTY
            '^' -> SPLITTER
            else -> throw IllegalArgumentException("Unknown char: $char")
        }
    }
}

fun Grid<TachyonManifoldState>.toManifold(): TachyonManifold {
    val start = getPositionsWhereValue { it == TachyonManifoldState.START }.first()
    val splitters = getPositionsWhereValue { it == TachyonManifoldState.SPLITTER }

    return TachyonManifold(start, splitters, height)
}

data class TachyonManifold(val start: Position, val splitters: List<Position>, val manifoldExitDistance: Int) {
    fun simulateBeam(): TachyonBeamPath {
        val startNode = Beam(start, null)
        val leaves = stackOf<TachyonBeamNode>(startNode)
        val nodes = mutableMapOf<Position, TachyonBeamNode>()

        fun createNextNode(position: Position): TachyonBeamNode {
            // Use cache to avoid traversing the same path multiple times
            if (nodes.containsKey(position)) return nodes[position]!!

            val nextNode = when (position) {
                in splitters -> BeamSplitter(position, null, null)
                else -> Beam(position, null)
            }
            leaves.push(nextNode)
            nodes[position] = nextNode

            return nextNode
        }

        while (leaves.isNotEmpty()) {
            val node = leaves.pop()
            if (node.position.y == manifoldExitDistance) continue

            when (node) {
                is Beam -> {
                    node.next = createNextNode(node.position + Offset(0, 1))
                }

                is BeamSplitter -> {
                    node.nextLeft = createNextNode(node.position + Offset(-1, 0))
                    node.nextRight = createNextNode(node.position + Offset(1, 0))
                }
            }
        }

        return startNode
    }
}

typealias TachyonBeamPath = TachyonBeamNode

sealed class TachyonBeamNode(val position: Position) {

    abstract val paths: Lazy<Long>
    abstract val splitters: Lazy<Set<BeamSplitter>>

    class Beam(position: Position, var next: TachyonBeamNode?) : TachyonBeamNode(position) {
        override val paths = lazy {
            if (next == null) 1 else next!!.paths.value
        }
        override val splitters = lazy {
            if (next == null) emptySet() else next!!.splitters.value
        }
    }

    class BeamSplitter(position: Position, var nextLeft: TachyonBeamNode?, var nextRight: TachyonBeamNode?) : TachyonBeamNode(position) {
        override val paths = lazy {
            nextLeft!!.paths.value + nextRight!!.paths.value
        }
        override val splitters = lazy {
            setOf(this) + nextLeft!!.splitters.value + nextRight!!.splitters.value
        }
    }

}
