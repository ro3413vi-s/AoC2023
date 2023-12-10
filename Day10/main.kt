import java.io.File

var map: MutableList<MutableList<Char>> = mutableListOf()
var distance: MutableList<MutableList<Int>> = mutableListOf()

fun main() {
    var inFile = File("res/input.txt")
    map = inFile.readLines().map{it.toList().toMutableList()}.toMutableList()
    for(row in map) {
        distance.add(mutableListOf())
        repeat(row.size) {_ -> distance[distance.lastIndex].add(-1)}
    }
    var start: Pair<Int, Int> = findStartPos()
    var nextNodes: MutableList< Pair< Pair<Int,Int>, Pair<Int,Int>>> = mutableListOf(Pair(start,start))
    while(!nextNodes.isEmpty()) {
        var temp: MutableList< List<Pair< Pair<Int,Int>, Pair<Int,Int>>>> = mutableListOf()
        for (i in 0..nextNodes.lastIndex) {
            var t = traversePipes(nextNodes[i].first, nextNodes[i].second)
            if (t != null) {
                temp.add(t)
            }
        }
        nextNodes.clear()
        nextNodes = temp.flatten().toMutableList()
        temp.clear()
    }
    var max = 0
    for (d in distance) {
        var t = (d.maxOrNull() ?: 0)
        max = if (t > max) t else max
    }
    println(max)
    // Part 2
    removeClutter()
    findEnclosedTiles()
    findRest()
    println(distance.flatten().filter{ it == -1}.count())
}

// Part 1

fun findStartPos(): Pair<Int, Int> {
    for (r in 0..map.lastIndex) {
        var c = map[r].indexOf('S')
        if (c >= 0) {
            return Pair(r,c)
        }
    }
    return Pair(-1, -1) // should never happen
}

fun traversePipes(current: Pair<Int, Int>, previous: Pair<Int, Int>): List<Pair<Pair<Int,Int>, Pair<Int,Int>>>? {
    var currentStep = distance[current.first][current.second]
    var prevStep = distance[previous.first][previous.second]
    if (currentStep == -1) {
        distance[current.first][current.second] = prevStep + 1
    } else if (currentStep > prevStep+1) {
        distance[current.first][current.second] = prevStep + 1
    } else {
        return null
    }
    var nextNodes = findConnectingPipes(current).filter{ it != previous }
    return nextNodes.map{ Pair(it, current) }
}

fun findConnectingPipes(current: Pair<Int, Int>): MutableList<Pair<Int,Int>> {
    var N = Pair(current.first-1, current.second)
    var S = Pair(current.first+1, current.second)
    var W = Pair(current.first, current.second-1)
    var E = Pair(current.first, current.second+1)
    var nextNodes: MutableList<Pair<Int,Int>> = mutableListOf()
    if( onMap(N) && 
        listOf('S', '|', 'J', 'L').contains(map[current.first][current.second]) &&
        listOf('|', 'F', '7').contains(map[N.first][N.second])
    ) {
        nextNodes.add(N)
    }
    if( onMap(S) && 
        listOf('S', '|', 'F', '7').contains(map[current.first][current.second]) &&
        listOf('|', 'J', 'L').contains(map[S.first][S.second])
    ) {
        nextNodes.add(S)
    }
    if( onMap(W) && 
        listOf('S', '-', 'J', '7').contains(map[current.first][current.second]) &&
        listOf('-', 'F', 'L').contains(map[W.first][W.second])
    ) {
        nextNodes.add(W)
    }
    if( onMap(E) && 
        listOf('S', '-', 'F', 'L').contains(map[current.first][current.second]) &&
        listOf('-', '7', 'J').contains(map[E.first][E.second])) {
        nextNodes.add(E)
    }
    return nextNodes
}

fun onMap(coord: Pair<Int, Int>): Boolean {
    return coord.first >= 0 && coord.second >= 0 &&
            coord.first <= map.lastIndex && coord.second < map[0].size
}

// Part 2

fun removeClutter() {
    for (r in 0..map.lastIndex) {
        for (c in 0..map[0].lastIndex) {
            if (distance[r][c] == -1) {
                map[r][c] = '.'
            }
        }
    }
}

fun findRest() {
    for (r in 0..distance.lastIndex) {
        for (c in 0..distance[0].lastIndex) {
            if (distance[r][c] == -1) {
                var crosses = 0
                var h = r
                var w = c
                while (h < distance.lastIndex && w < distance[0].lastIndex) {
                    h += 1
                    w += 1
                    if (map[h][w] != '.' && map[h][w] != 'L' && map[h][w] != '7') {
                        crosses += 1
                    }
                }
                if (crosses % 2 == 0) {
                    distance[r][c] = -2
                }
            }
        }
    }
}


fun findEnclosedTiles() {
    // set all borders to non-enclosed (unless they are part of the loop) (-2)
    setBorders()
    var changed = true
    var row = 1
    var col = 1
    while(changed) {
        changed = updateEnclosed(row, col)
    }
}

fun setBorders() {
    for (c in 0..distance[0].lastIndex) {
        if (distance[0][c] == -1) {
            distance[0][c] = -2
        }
    }
    for (r in 0..distance.lastIndex) {
        if (distance[r][0] == -1) {
            distance[r][0] = -2
        }
    }
    for (r in 0..distance.lastIndex) {
        if (distance[r][distance[r].lastIndex] == -1) {
            distance[r][distance[r].lastIndex] = -2
        }
    }
    for (c in 0..distance[distance.lastIndex].lastIndex) {
        if (distance[distance.lastIndex][c] == -1) {
            distance[distance.lastIndex][c] = -2
        }
    }
}

fun updateEnclosed(startRow: Int, startCol: Int): Boolean {
    var changed = false
    for(r in startRow..distance.lastIndex-startRow) {
        for (c in startCol..distance[r].lastIndex-startCol) {
            if (distance[r][c] == -1 && touchesNotEnclosed(r,c)) {
                distance[r][c] = -2
                changed = true
            }
        }
    }
    return changed
}

fun touchesNotEnclosed(row: Int, col: Int): Boolean {
    return distance[row-1][col] == -2 ||
        distance[row+1][col] == -2 ||
        distance[row][col-1] == -2 ||
        distance[row][col+1] == -2 
}