import java.io.File

var gardenSet: MutableSet<Pair<Int,Int>> = mutableSetOf()
var start: Pair<Int,Int> = Pair(-1,-1)

var possibleLocations: MutableSet<Pair<Int,Int>> = mutableSetOf()
var finalLocations: MutableSet<Pair<Int,Int>> = mutableSetOf()

var rows: Int = 0
var cols: Int = 0

fun main() {
    var inFile = File("tinput.txt")
    inFile.readLines().forEachIndexed{row, it -> 
        rows = row
        it.forEachIndexed{col, s -> 
            if(s != '#') gardenSet.add(Pair(row,col))
            if(s == 'S') start = Pair(row,col)
            cols = col
        }
    }
    rows++
    cols++
    // Part 1
    possibleLocations.add(start)
    var nextLocations: MutableSet<Pair<Int,Int>> = mutableSetOf()

    for( i in 1..64) {
        for(location in possibleLocations) {
            nextLocations.addAll(getLocations(location))
        }
        possibleLocations.clear()
        possibleLocations.addAll(nextLocations)
        nextLocations.clear()
    }
    println(possibleLocations.size)

    // Part 2
    possibleLocations.clear()
    nextLocations.clear()
    possibleLocations.add(start)
    val steps = 5000
    for(i in 1..steps) {
        for(location in possibleLocations) {
            nextLocations.addAll(getLocationsInf(location))
        }
        possibleLocations.clear()
        possibleLocations.addAll(nextLocations)
        if(steps % 2 != 0 && i % 2 != 0) {
            // uneven number of steps
            finalLocations.addAll(nextLocations)
        } else if( steps % 2 == 0 && i % 2 == 0) {
            // even number of steps
            finalLocations.addAll(nextLocations)
        }
        nextLocations.clear()
    }
    finalLocations.addAll(possibleLocations)
    println(finalLocations.size)
}

fun getLocations(location: Pair<Int,Int>): MutableList<Pair<Int,Int>> {
    var temp: MutableList<Pair<Int,Int>> = mutableListOf()
    var u = Pair(location.first-1, location.second)
    var d = Pair(location.first+1, location.second)
    var l = Pair(location.first, location.second-1)
    var r = Pair(location.first, location.second+1)
    for(p in listOf(u,d,l,r)) {
        if(p in gardenSet) temp.add(p)
    }
    return temp
}

// Part 2
// infinite map -> modulo the coords and check on initial map?
// possible other speedups?
// Every garden we reach after an uneven number of steps will be part of the final answer so we can ignore previously visited locations
// if we remember these
fun getLocationsInf(location: Pair<Int,Int>): MutableList<Pair<Int,Int>> {
    var temp: MutableList<Pair<Int,Int>> = mutableListOf()
    var u = Pair(location.first-1, location.second)
    var d = Pair(location.first+1, location.second)
    var l = Pair(location.first, location.second-1)
    var r = Pair(location.first, location.second+1)
    for(p in listOf(u,d,l,r)) {
        var t = Pair(p.first % rows, p.second % cols)
        if(t.first < 0) t = Pair(t.first + rows, t.second)
        if(t.second < 0) t = Pair(t.first, t.second + cols)
        if(t in gardenSet && !(p in finalLocations)) temp.add(p)
    }
    return temp
}
