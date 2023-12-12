/*
For each row, count all of the different arrangements of operational and broken springs that meet the given criteria. What is the sum of those counts?
 */

import java.io.File

var indata: MutableList<Pair<String,String>> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ indata.add(Pair(it.split(' ')[0], it.split(' ')[1])) }
    indata.forEach{ findPossibleSolutions(it) }
}

fun findPossibleSolutions(input: Pair<String,String>) {
    var map: MutableList<String> = input.first.split("\\.+".toRegex()).filter{ !it.isEmpty() }.toMutableList()
    var values: MutableList<Int> = input.second.split(',').map{ it.toInt() }.toMutableList()
    println(map)
    println(values)
    for( i in 0..map.lastIndex) {
        findForcedSolutions(map, values)
    }
}

fun findForcedSolutions(map: MutableList<String>, values: MutableList<Int>) {
    if( map.size == 0) return
    if(map[0].contains('#') && map[0].length == values[0]) {
        map.removeAt(0)
        values.removeAt(0)
    }
    if(map[map.lastIndex].contains('#') && map[map.lastIndex].length == values[values.lastIndex]) {
        map.removeAt(map.lastIndex)
        values.removeAt(values.lastIndex)
    }
    println(map)
    println(values)
}