import java.io.File

fun main() {
    val inFile = File("res/input1.txt")
    var rowNbr = 0
    inFile.forEachLine{ parseInput(it, rowNbr++) }
    println(calculatePartNumber())
    println(calculateGears())
}

// Helper stuff

var numbersList = ArrayList<NumberObj>()
var symbolsMap: MutableMap<Int, ArrayList<Int>> = mutableMapOf()
var gearList = ArrayList<Pair<Int, Int>>()

data class NumberObj(var row: Int, var start: Int, var end: Int, var num: String)

fun parseInput(row: String, rowNbr: Int) {
    row.forEachIndexed{ index: Int, c: Char ->
        if (c == '.') {
            // ignore
        } else if (c.isDigit()) {
            addToNumbers(c, rowNbr, index)
        } else {
            if (c == '*') {
                gearList.add(Pair(rowNbr, index))
            }
            if (symbolsMap.containsKey(rowNbr)) {
                symbolsMap[rowNbr]!!.add(index)
            } else {
                symbolsMap.put(rowNbr, arrayListOf(index))
            }
        }
    }
}

fun addToNumbers(c: Char, rowNbr: Int, index: Int) {
    if (!numbersList.isEmpty()) {
        var lastElement = numbersList.last()
        if (lastElement.row == rowNbr && lastElement.end == index-1) {
            lastElement.end = index
            lastElement.num = lastElement.num + c
            return
        }
    }
    numbersList.add(NumberObj(rowNbr, index, index, c.toString()))
}

// Part 1

fun calculatePartNumber(): Int {
    var accumulate = 0
    numbersList.forEach{ numObj ->
        accumulate += checkNumber(numObj)
    }
    return accumulate
}

fun checkNumber(numObj: NumberObj): Int {
    var symbolSet: MutableSet<Int> = mutableSetOf()
    symbolsMap.toList().filter{ (fst, _) -> fst in (numObj.row-1..numObj.row+1)}.forEach{(_, snd) -> symbolSet.addAll(snd)}
    if (symbolSet.any{ it in (numObj.start-1..numObj.end+1) }) {
        return numObj.num.toInt()
    }
    return 0
}

// Part 2

fun calculateGears(): Int {
    var accumulate = 0
    gearList.forEach{ (row, index) ->
        accumulate += checkGear(row, index)
    }
    return accumulate
}

fun checkGear(row: Int, index: Int): Int {
    var tempList = numbersList.filter{ it.row in (row-1..row+1) && index in (it.start-1..it.end+1) }
    if (tempList.size == 2) {
        return tempList[0].num.toInt() * tempList[1].num.toInt()
    }
    return 0
}