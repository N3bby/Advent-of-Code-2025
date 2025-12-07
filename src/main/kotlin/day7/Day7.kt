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

data class TachyonManifold(val start: Position, val splitters: List<Position>, val manifoldExitDistance: Int) {
    fun simulateBeam(): TachyonBeamPath {
        val startNode = Beam(start, null)
        val leaves = stackOf<TachyonBeamNode>(startNode)

        fun getNextNode(position: Position): TachyonBeamNode = when (position) {
            in splitters -> BeamSplit(position, null, null)
            else -> Beam(position, null)
        }

        while (leaves.isNotEmpty()) {
            val node = leaves.pop()
            if (node.position.y == manifoldExitDistance) continue

            when (node) {
                is Beam -> {
                    getNextNode(node.position + Offset(0, 1)).also {
                        node.next = it
                        leaves.push(it)
                    }
                }

                is BeamSplit -> {
                    getNextNode(node.position + Offset(-1, 0)).also {
                        node.nextLeft = it
                        leaves.push(it)
                    }
                    getNextNode(node.position + Offset(1, 0)).also {
                        node.nextRight = it
                        leaves.push(it)
                    }
                }
            }
        }

        return startNode
    }
}

typealias TachyonBeamPath = TachyonBeamNode

fun TachyonBeamPath.getBeamSplits(): List<BeamSplit> =
    getNodes().filterIsInstance<BeamSplit>()

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
