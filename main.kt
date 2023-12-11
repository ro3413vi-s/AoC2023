//In fact, the result is that any rows or columns that contain no galaxies should all actually be twice as big.
//Expand the universe, then find the length of the shortest path between every pair of galaxies. What is the sum of these lengths?

import java.io.File

var map: MutableList<MutableList<Char>> = mutableListOf()
var mapFlip: MutableList<MutableList<Char>> = mutableListOf()

fun main() {
    var inFile = File("res/input.txt")
    inFile.forEachLine{ parseRows(it) }
    parseCols()
    println(mapFlip)
    findGalaxies(mapFlip)
    calculateDistance(galaxies)
}

fun parseRows(input: String) {
    if(input.contains('#')) {
        map.add(input.toMutableList())
    } else {
        map.add(input.toMutableList())
        map.add(input.toMutableList())
    }
}

fun parseCols() {
    for (c in 0..map[0].lastIndex) {
        var temp: MutableList<Char> = mutableListOf()
        for (r in 0..map.lastIndex) {
            temp.add(map[r][c])
        }
        if(temp.contains('#')) {
            mapFlip.add(temp)
        }else{
            mapFlip.add(temp)
            mapFlip.add(temp)
        }
    }
}