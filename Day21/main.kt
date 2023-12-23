import java.io.File

var gardenSet: MutableSet<Pair<Int,Int>> = mutableSetOf()
var start: Pair<Int,Int> = Pair(-1,-1)

var possibleLocations: MutableSet<Pair<Int,Int>> = mutableSetOf()

var rows: Int = 0
var cols: Int = 0

fun main() {
    var inFile = File("input.txt")
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

    for( i in 1..64) {
        possibleLocations = possibleLocations.map{ getLocations(it) }.flatten().toMutableSet()
    }
    println(possibleLocations.size)

    // Part 2
    possibleLocations.clear()
    possibleLocations.add(start)
    var p1 = 0
    var p2 = 0
    var p3 = 0
    var i = 0
    while(p1 == 0) {
        if (i % (rows) == 65) {
            p1 = p2
            p2 = p3
            p3 = possibleLocations.size
        }
        possibleLocations = possibleLocations.map{ getLocationsInf(it) }.flatten().toMutableSet()
        i++
    }
    var x = 26501365L / rows
    println( p1 + x * (p2-p1 + (x-1) * ((p3-p2-p2+p1)/2) ) )

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
        if(t in gardenSet) temp.add(p)
    }
    return temp
}
