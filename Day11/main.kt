import java.io.File

var map: MutableList<MutableList<Char>> = mutableListOf()

fun main() {
    var inFile = File("res/input.txt")
    inFile.forEachLine{ map.add(it.toMutableList()) }
    var galaxies = findGalaxies()
    galaxies = expandUniverse(2, galaxies)
    calculateDistance(galaxies)
    // Part 2
    galaxies.clear()
    galaxies = findGalaxies()
    galaxies = expandUniverse(1000000, galaxies)
    calculateDistance(galaxies)
}

fun findGalaxies(): MutableList<Pair<Int,Int>> {
    var galaxies: MutableList<Pair<Int,Int>> = mutableListOf()
    for(r in 0..map.lastIndex) {
        for(c in 0..map[r].lastIndex) {
            if(map[r][c] == '#') {
                galaxies.add(Pair(r,c))
            }
        }
    }
    return galaxies
}

fun expandUniverse(times: Int, galaxies: MutableList<Pair<Int,Int>>): MutableList<Pair<Int,Int>> {
    var movedRows = 0
    for(r in 0..map.lastIndex) {
        var row = r + movedRows*times
        if(!galaxies.any{ it.first == row }) {
            for(galaxy in galaxies.filter{ it.first > row }) {
                galaxies[galaxies.indexOf(galaxy)] = Pair(galaxy.first+times-1, galaxy.second)
            }
            movedRows += 1
        }
    }
    var movedCols = 0
    for(c in 0..map[0].lastIndex) {
        var col = c + movedCols*times
        if(!galaxies.any{ it.second == col }) {
            for(galaxy in galaxies.filter{ it.second > col }) {
                galaxies[galaxies.indexOf(galaxy)] = Pair(galaxy.first, galaxy.second+times-1)
            }
            movedCols += 1
        }
    }
    return galaxies
}

fun calculateDistance(galaxies: MutableList<Pair<Int,Int>>) {
    var distance: Long = 0L
    for(i in 0..galaxies.size-2) {
        var galaxy = galaxies.elementAt(i)
        for (j in i+1..galaxies.size-1) {
            var dest = galaxies.elementAt(j)
            distance += Math.abs(galaxy.first - dest.first) + Math.abs(galaxy.second - dest.second)
        }
    }
    println(distance)
}
