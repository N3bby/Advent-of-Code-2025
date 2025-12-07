package day7

import day7.TachyonBeamNode.Beam
import day7.TachyonBeamNode.BeamSplit
import ext.stackOf
import util.Grid
import util.Offset
import util.Position

enum class TachyonManifoldState {
    EMPTY,
    SPLITTER,
    START,
    BEAM;

    companion object {
        fun fromChar(char: Char) = when (char) {
            'S' -> START
            '.' -> EMPTY
            '^' -> SPLITTER
            else -> throw IllegalArgumentException("Unknown char: $char")
        }
    }

    override fun toString(): String = when (this) {
        EMPTY -> "."
        SPLITTER -> "^"
        START -> "S"
        BEAM -> "|"
    }
}

fun Grid<TachyonManifoldState>.toManifold(): TachyonManifold {
    val start = getPositionsWhereValue { it == TachyonManifoldState.START }.first()
    val splitters = getPositionsWhereValue { it == TachyonManifoldState.SPLITTER }

    return TachyonManifold(start, splitters, height)
}

typealias SplittersReached = Int

data class TachyonManifold(val start: Position, val splitters: List<Position>, val manifoldExitDistance: Int) {
    fun simulateBeam(): Pair<TachyonBeamPath, SplittersReached> {
        val startNode = Beam(start, null)
        val leaves = stackOf<TachyonBeamNode>(startNode)
        val nodes = mutableMapOf<Position, TachyonBeamNode>()
        var splittersReached = 0

        fun getNextNode(position: Position): TachyonBeamNode {
            if (nodes.containsKey(position)) return nodes[position]!!

            val nextNode = when (position) {
                in splitters -> BeamSplit(position, null, null)
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
                    node.next = getNextNode(node.position + Offset(0, 1))
                }
                is BeamSplit -> {
                    splittersReached++
                    node.nextLeft = getNextNode(node.position + Offset(-1, 0))
                    node.nextRight = getNextNode(node.position + Offset(1, 0))
                }
            }
        }

        return startNode to splittersReached
    }
}

typealias TachyonBeamPath = TachyonBeamNode

sealed class TachyonBeamNode(val position: Position) {

    class Beam(position: Position, var next: TachyonBeamNode?) : TachyonBeamNode(position) {
        override fun getNodes(): List<TachyonBeamNode> {
            return listOf(this) + (next?.getNodes() ?: emptyList())
        }
    }

    class BeamSplit(position: Position, var nextLeft: TachyonBeamNode?, var nextRight: TachyonBeamNode?) : TachyonBeamNode(position) {
        override fun getNodes(): List<TachyonBeamNode> {
            return (listOf(this)
                    + (nextLeft?.getNodes() ?: emptyList())
                    + (nextRight?.getNodes() ?: emptyList()))
        }
    }

    abstract fun getNodes(): List<TachyonBeamNode>
}
