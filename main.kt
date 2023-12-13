/*
For each row, count all of the different arrangements of operational and broken springs that meet the given criteria. What is the sum of those counts?
 */

import java.io.File

var indata: MutableList<MutableList<String>> = mutableListOf()

fun main() {
    var inFile = File("input.txt")
    var temp = inFile.readLines()
    while(!temp.isEmpty()) {
        indata.add(temp.takeWhile{ !it.isEmpty() }.toMutableList())
        temp = temp.dropWhile{ !it.isEmpty() }.drop(1)
    }
    var acc = 0
    for(input in indata) {
        acc += findHorizontal(input, false)*100
        acc += findHorizontal(rotate(input), false)
    }
    println(acc)
    // Part 2
    acc = 0
    for(input in indata) {
        acc += findHorizontal(input, true)*100
        acc += findHorizontal(rotate(input), true)
    }
    println(acc)
}

fun rotate(rows: MutableList<String>): MutableList<String> {
    var cols: MutableList<String> = mutableListOf()
    for(c in 0..rows[0].lastIndex) {
        cols.add("")
        for (r in 0..rows.lastIndex) {
            cols[c] = cols[c] + rows[r][c]
        }
    }
    return cols
}

fun findHorizontal(rows: MutableList<String>, smudges: Boolean): Int {
    var largestReflection = 0
    var index = 0
    for(i in 0..rows.lastIndex-1) {
        var fst = rows.take(i+1)
        var snd = rows.takeLast(rows.size-(i+1))
        if(snd.size > fst.size) {
            snd = snd.take(fst.size)
        } else if( fst.size > snd.size) {
            fst = fst.takeLast(snd.size)
        }
        if(checkReflection(fst, snd, smudges)) {
            if(largestReflection < fst.size) {
                largestReflection = fst.size
                index = i+1
            }
        }
    }
    return index
}

fun checkReflection(fst: List<String>, s: List<String>, smudges: Boolean): Boolean {
    var snd = s.asReversed()
    if(!smudges) {
        return fst == snd
    }
    var difference = 0
    for(lines in fst.zip(snd)) {
        if(difference > 1) {
            return false // early return
        }
        if(lines.first == lines.second) continue // no need to check this line
        for(i in 0..lines.first.length-1) {
            if(lines.first[i] != lines.second[i]) {
                difference = difference + 1
            }
        }
    }
    return difference == 1
}