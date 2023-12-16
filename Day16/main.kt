import java.io.File

data class Beam(var row: Int, var col: Int, var direction: Int) // 0 ^ , 1 > , 2 v , 3 <
var beams: MutableList<Beam> = mutableListOf(Beam(0,-1,1))
var board: MutableList<MutableList<Char>> = mutableListOf()

var splitsUsed: MutableSet<Pair<Int,Int>> = mutableSetOf()
var energized: MutableSet<Pair<Int,Int>> = mutableSetOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ board.add(it.toMutableList())}
    // Part 1
    println(findEnergized())
    // Part 2
    var max = 0
    var r1: Int = -1
    var r2: Int = board.lastIndex+1
    for (c in 0..board[0].lastIndex) {
        // from top
        energized.clear()
        splitsUsed.clear()
        beams.clear()
        beams.add(Beam(r1, c, 2))
        var temp = findEnergized()
        if (temp > max) {
            max = temp
        }
        // from bottom
        energized.clear()
        splitsUsed.clear()
        beams.clear()
        beams.add(Beam(r2, c, 0))
        temp = findEnergized()
        if (temp > max) {
            max = temp
        }
    }
    var c1: Int = -1
    var c2: Int = board[0].lastIndex+1
    for (r in 0..board.lastIndex) {
        // from top
        energized.clear()
        splitsUsed.clear()
        beams.clear()
        beams.add(Beam(r, c1, 1))
        var temp = findEnergized()
        if (temp > max) {
            max = temp
        }
        // from bottom
        energized.clear()
        splitsUsed.clear()
        beams.clear()
        beams.add(Beam(r, c2, 3))
        temp = findEnergized()
        if (temp > max) {
            max = temp
        }
    }
    println(max)
}

fun findEnergized(): Int {
    var sizes: MutableList<Int> = mutableListOf(0,1)
    while(sizes.any{ sizes[0] != it} && beams.size != 0) {
        if(sizes.size == 10) {
            sizes.removeAt(0)
        }
        var temps = beams.toList()
        for(beam in temps) {
            moveBeam(beam)
        }
        sizes.add(energized.size)
    }
    return energized.size
}

fun moveBeam(beam: Beam) {
    when(beam.direction) {
        0 -> beam.row = beam.row-1
        1 -> beam.col = beam.col+1
        2 -> beam.row = beam.row+1
        3 -> beam.col = beam.col-1
        else -> throw Exception("NO SUCH BEAM DIRECTION!!!")
    }
    if(beam.row < 0 || beam.row > board[0].lastIndex || beam.col < 0 || beam.col > board.lastIndex) {
        // outside the board, remove beam
        beams.remove(beam)
        return
    }

    energized.add(Pair(beam.row, beam.col))
    when(board[beam.row][beam.col]) {
        '/' -> rightMirror(beam)
        '\\' -> leftMirror(beam)
        '-' -> horizontalSplit(beam)
        '|' -> verticalSplit(beam)
        else -> if(beams.contains(Beam(beam.row, beam.col, flipDir(beam.direction)))) {
                    beams.remove(Beam(beam.row, beam.col, flipDir(beam.direction)))
                    beams.remove(beam)
                    return
                }
    }
}

fun rightMirror(beam: Beam) {
    // char : "/"
    when(beam.direction) {
        0 -> beam.direction = 1
        1 -> beam.direction = 0
        2 -> beam.direction = 3
        3 -> beam.direction = 2
        else -> throw Exception("NO SUCH BEAM DIRECTION!!!")
    }
}

fun leftMirror(beam: Beam) {
    // char : "\"
    when(beam.direction) {
        0 -> beam.direction = 3
        1 -> beam.direction = 2
        2 -> beam.direction = 1
        3 -> beam.direction = 0
        else -> throw Exception("NO SUCH BEAM DIRECTION!!!")
    }
}

fun horizontalSplit(beam: Beam) {
    // Char: "-"
    if(listOf(0,2).contains(beam.direction)) {
        if(splitsUsed.contains(Pair(beam.row, beam.col))) {
            beams.remove(beam)
            return
        } else {
            splitsUsed.add(Pair(beam.row, beam.col))
        }
        beam.direction = 1
        beams.add(Beam(beam.row, beam.col, 3))
    }
}


fun verticalSplit(beam: Beam) {
    // Char: "|"
    if(listOf(1,3).contains(beam.direction)) {
        if(splitsUsed.contains(Pair(beam.row, beam.col))) {
            beams.remove(beam)
            return
        } else {
            splitsUsed.add(Pair(beam.row, beam.col))
        }
        beam.direction = 0
        beams.add(Beam(beam.row, beam.col, 2))
    }
}

fun flipDir(dir: Int): Int {
    when(dir) {
        0 -> return 2
        1 -> return 3
        2 -> return 0
        3 -> return 1
        else -> return -1
    }
}