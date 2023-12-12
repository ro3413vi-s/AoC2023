/*
For each row, count all of the different arrangements of operational and broken springs that meet the given criteria. What is the sum of those counts?
 */

import java.io.File

var indata: MutableList<Pair<String,String>> = mutableListOf()

fun main() {
    var inFile = File("res/input.txt")
    inFile.forEachLine{ indata.add(Pair(it.split(' ')[0], it.split(' ')[1])) }
    var res = 0L
    indata.forEach{ res += findPossibleSolutions(it) }
    println(res)
    var res2 = 0L
    indata.forEach{ (fst,snd): Pair<String,String> ->
        var f: String = fst
        var s: String = snd
        repeat(4) {
            f = f + '?' + fst
            s = s + ',' + snd
        }
        res2 += findPossibleSolutions(Pair(f,s))
    }
    println(res2)
}

fun findPossibleSolutions(input: Pair<String,String>): Long {
    var str = input.first + '.'
    var strLen = str.length
    var values: MutableList<Int> = input.second.split(',').map{ it.toInt() }.toMutableList()
    var valLen = values.size
    var temp: MutableList<MutableList<MutableList<Long>>> = mutableListOf()
    repeat(strLen+1){
        var t: MutableList<MutableList<Long>> = mutableListOf()
        repeat(valLen+2) {
            var y: MutableList<Long> = mutableListOf()
            repeat(strLen+2) {
                y.add(0L)
            }
            t.add(y)
        }
        temp.add(t)
    }
    temp[0][0][0] = 1
    for( x in 0..strLen-1 ) {
        for( y in 0..valLen) {
            for( currentGroup in 0..strLen) {
                var current = temp[x][y][currentGroup]
                if(current == 0L) {
                    continue
                }
                if(str[x] == '.' || str[x] == '?') {
                    if(currentGroup == 0 || currentGroup == values[if(y-1<0) values.lastIndex else y-1]){
                        temp[x+1][y][0] += current
                    }
                }
                if(str[x] == '#' || str[x] == '?') {
                    temp[x+1][y + (if (currentGroup == 0) 1 else 0)][currentGroup+1] += current
                }
            }
        }
    }
    return temp[strLen][valLen][0]
}