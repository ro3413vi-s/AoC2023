import java.io.File

var roundStones: MutableSet<Pair<Int,Int>> = mutableSetOf()
var squareStones: MutableSet<Pair<Int,Int>> = mutableSetOf()
var weights: MutableList<Int> = mutableListOf()

var cols: Int = 0
var rows: Int = 0

fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ parseData(it, rows++) }
    for (i in 0..200) {
        tilt() // North
        turnData()
        tilt() // West
        turnData()
        tilt() // South
        turnData()
        tilt() // East
        turnData()
        var acc = 0
        roundStones.forEach{ acc += cols - it.first }
        weights.add(acc)
    }
    println(weights)
    println(weights.size)
    var cycle: MutableList<Int> = mutableListOf()
    for(i in 0..weights.lastIndex) {
        if(!cycle.contains(weights[weights.lastIndex-i])) {
            cycle.add(0, weights[weights.lastIndex-i])
        }else {
            break
        }
    }
    println(cycle)
    var c = (1000000000-weights.size-1) % cycle.size
    println(c)
    println(cycle[c])

}

fun parseData(input: String, row: Int) {
    cols++
    input.forEachIndexed{index, it -> 
        if(it == '#') {
            squareStones.add(Pair(row, index)) 
        } else if( it == 'O') {
            roundStones.add(Pair(row, index)) }
    }
}

fun tilt() {
    for (col in 0..cols) {
        var roundCol: MutableList<Pair<Int,Int>> = roundStones.filter{ it -> it.second == col}.sortedBy{it.first}.toMutableList()
        var squareCol = squareStones.filter{ it -> it.second == col}.sortedBy{it.first}
        roundStones.removeAll(roundCol)
        var top = 0
        var index = 0
        while(index <= roundCol.lastIndex) {
            if(Pair(top,col) in roundCol || Pair(top,col) in squareCol) {
                top++
            } else if(roundCol[index].first <= top) {
                index++
            } else if(!squareCol.takeLastWhile{ it.first > top }.isEmpty() &&
                    roundCol[index].first > squareCol.takeLastWhile{ it.first > top }.first().first) {
                top = squareCol.takeLastWhile{ it.first > top }.first().first
            } else {
                roundCol[index] = Pair(top++,col)
            }
        }
        roundStones.addAll(roundCol)
    }
}

fun turnData() {
    var round = roundStones.toMutableList()
    roundStones.removeAll(round)
    for (index in 0..round.lastIndex) {
        round[index] = Pair(round[index].second, cols-1-round[index].first)
    }
    roundStones.addAll(round)

    var square = squareStones.toMutableList()
    squareStones.removeAll(square)
    for (index in 0..square.lastIndex) {
        square[index] = Pair(square[index].second, cols-1-square[index].first)
    }
    squareStones.addAll(square)
}
