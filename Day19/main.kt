import java.io.File

var workflowMap: MutableMap<String,List<Rule>> = mutableMapOf()
data class Rule(var statement: String, var dest: String)

var partList: MutableList<xmas> = mutableListOf()
data class xmas(var x: Int, var m: Int, var a: Int, var s: Int)

var rangeList: MutableList<Pair<String, xmasRanges>> = mutableListOf()
data class xmasRanges(var x: Pair<Int,Int>, var m: Pair<Int,Int>, var a: Pair<Int,Int>, var s: Pair<Int,Int>)

var accepted: Long = 0L

fun main() {
    var inFile = File("input.txt")
    parseWorkflows(inFile)
    parseParts(inFile)
    //println(partList)

    // Part 1
    for(part in partList) {
        checkPart(part)
    }
    println(accepted)

    // Part 2
    var x = Pair(1,4000)
    var m = Pair(1,4000)
    var a = Pair(1,4000)
    var s = Pair(1,4000)
    rangeList.add(Pair("in", xmasRanges(x,m,a,s)))
    accepted = 0L
    while(!rangeList.isEmpty()) {
        var next = rangeList.first()
        rangeList = rangeList.drop(1).toMutableList()
        if (next.first == "A") {
            var t = next.second
            accepted +=  (t.x.second-t.x.first+1).toLong() * 
                (t.m.second-t.m.first+1).toLong() * 
                (t.a.second-t.a.first+1).toLong() * 
                (t.s.second-t.s.first+1).toLong()
            continue
        } else if( next.first == "R" ) {
            continue
        } else {
            checkPartInWorkflow2(next.second, workflowMap.get(next.first)!!)
        }
    }
    println(accepted)
}

fun checkPart(part: xmas) {
    var next = checkPartInWorkflow(part, workflowMap.get("in")!!)
    while(true){
        if (next == "A") {
            accepted += part.x + part.m + part.a + part.s
            return
        } else if( next == "R" ) {
            return
        } else {
            next = checkPartInWorkflow(part, workflowMap.get(next)!!)
        }
    }
}

fun checkPartInWorkflow(part: xmas, rules: List<Rule>): String {
    for (rule in rules) {
        if (rule.statement.isEmpty()) return rule.dest
        var rp = rule.statement.split('<', '>')
        var partVal = when(rp[0]) {
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            "s" -> part.s
            else -> Int.MAX_VALUE
        }
        if('<' in rule.statement) {
            if (partVal < rp[1].toInt()) return rule.dest
        } else {
            if (partVal > rp[1].toInt()) return rule.dest
        }
    }
    return ""
}

// Part 2

fun checkPartInWorkflow2(xmas: xmasRanges, rules: List<Rule>) {
    var temp = xmas
    for (rule in rules) {
        if (rule.statement.isEmpty()) {
            rangeList.add(Pair(rule.dest, temp))
            return
        }
        var rp = rule.statement.split('<', '>')
        when(rp[0]) {
            "x" -> {
                var t = checkRange(rule, temp.x)
                temp.x = t.second
                rangeList.add(Pair(rule.dest, xmasRanges(t.first, xmas.m, xmas.a, xmas.s)))
            }
            "m" -> {
                var t = checkRange(rule, temp.m)
                temp.m = t.second
                rangeList.add(Pair(rule.dest, xmasRanges(xmas.x, t.first, xmas.a, xmas.s)))
            }
            "a" -> {
                var t = checkRange(rule, temp.a)
                temp.a = t.second
                rangeList.add(Pair(rule.dest, xmasRanges(xmas.x, xmas.m, t.first, xmas.s)))
            }
            "s" -> {
                var t = checkRange(rule, temp.s)
                temp.s = t.second
                rangeList.add(Pair(rule.dest, xmasRanges(xmas.x, xmas.m, xmas.a, t.first)))
            }
            else -> throw Exception("wrong stuff")
        }
    }
}

fun checkRange(rule: Rule, rng: Pair<Int,Int>): Pair<Pair<Int,Int>,Pair<Int,Int>> {
    var c = rule.statement.split('<', '>')[1].toInt()
    if('<' in rule.statement) {
        if (c in rng.first..rng.second) {
            return Pair(Pair(rng.first, c-1), Pair(c, rng.second))
        } else if( rng.second < c) {
            return Pair(rng , Pair(0,0))
        }
    } else {
        if (c in rng.first..rng.second) {
            return Pair(Pair(c+1, rng.second), Pair(rng.first, c))
        } else if( rng.second > c) {
            return Pair(rng, Pair(0,0))
        }
    }
    return Pair(Pair(0,0), rng)
}


// Helper functions

fun parseWorkflows(inFile: File) {
    var inputs = inFile.readLines().takeWhile{ !it.isEmpty() }.toMutableList()
    for (input in inputs.map{ it.split('{', '}', ',').filter{ !it.isEmpty() }}) {
        var temp: MutableList<Rule> = mutableListOf()
        var t = input.subList(1, input.lastIndex).map{ it.split(':') }
        t.forEach{ temp.add(Rule(it[0], it[1]))}
        temp.add(Rule("", input.last())) // add final rule
        workflowMap.put(input[0], temp)
    }
}

fun parseParts(inFile: File) {
    var inputs = inFile.readLines().takeLastWhile{ !it.isEmpty() }.toMutableList()
    var temp: MutableList<List<Int>> = mutableListOf()
    inputs.forEach{ temp.add(it.trim{ it == '{' || it == '}' }.filter{ !(it in "xmas=") }.split(',').map{ it.toInt() }) }
    for (t in temp) {
        partList.add(xmas(t[0], t[1], t[2], t[3]))
    }
}