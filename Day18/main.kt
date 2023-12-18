import java.io.File

var digList: MutableList<Dig> = mutableListOf()
data class Dig(var dir: Char, var length: Int, var col: String)

var digMap: MutableMap<Int,MutableSet<Pair<Int,String>>> = mutableMapOf()
var digList: MutableList<Pair<Int,Int>> = mutableListOf()


fun main() {
    var inFile = File("input.txt")
    inFile.forEachLine{ 
        var ins = it.split(' ')
        digList.add(Dig(ins[0].first(), ins[1].toInt(), ins[2])) 
    }
    var prev = Pair(0,0)
    for (dig in digList) {
        when(dig.dir) {
            'R' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                digList.add(prev)
                prev = Pair(prev.first, prev.second+1)
            }
            'L' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                digList.add(prev)
                prev = Pair(prev.first, prev.second-1)
            }
            'U' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                digList.add(prev)
                prev = Pair(prev.first-1, prev.second)
            }
            'D' -> repeat(dig.length) { 
                if (!digMap.containsKey(prev.first)) digMap.put(prev.first, mutableSetOf())
                digMap.get(prev.first)?.add(Pair(prev.second,dig.col)) 
                digList.add(prev)
                prev = Pair(prev.first+1, prev.second)
            }
            else -> println("ERROR no such direction")
        }
    }
    var size = 0
    for (p in digList.zipWithNext()) {
        var p1 = p.first
        var p2 = p.second
        size += (p1.first*p2.second) - (p1.second*p2.first)
    }
    println(size)
    var b = digMap.values.map{ it.size }.sum()
    var i = 0
    for(key in digMap.keys) {
        var row = digMap.get(key)!!.map{ it.first }.sorted()
        var diff = row.last() - row.first()
        i += diff+1 - row.size
    }
    size = i + (b/2) - 1
}