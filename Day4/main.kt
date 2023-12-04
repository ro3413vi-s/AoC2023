import java.io.File

data class Card(var winNbrs: List<Int>, var nbrs: List<Int>)
var allCards: ArrayList<Card> = ArrayList<Card>()
var cardTracker: ArrayList<Int> = ArrayList<Int>()

fun main() {
    val inFile = File("res/input.txt")
    inFile.forEachLine{ parseCards(it) }
    println(calculateScore(allCards))
    println(calculateCards())
}

fun parseCards(line: String) {
    var numbers = line.split('|', ':')
    val regex = "\\D+".toRegex()
    var card: Card = Card(numbers[1].trim().split(regex).map{ it.toInt() }, 
            numbers[2].trim().split(regex).map{ it.toInt() } )
    allCards.add(card)
    cardTracker.add(1)
}

// Part 1

fun calculateScore(cards: ArrayList<Card>): Double {
    var accumulate = 0.0
    for (card in cards) {
        var winningNumbers = card.nbrs.filter{ card.winNbrs.contains(it) }
        if (!winningNumbers.isEmpty()) {
            accumulate += Math.pow(2.0, winningNumbers.size - 1.0)
        }
    }
    return accumulate
}

// Part 2

fun calculateCards(): Int {
    var startNbr = 1
    return calculateCardHelper(startNbr)
}

fun calculateCardHelper(cardNbr: Int): Int {
    if (cardNbr > cardTracker.size) {
        return 0
    }
    var card: Card = allCards[cardNbr-1]
    var amount = cardTracker[cardNbr-1]
    var nbrWin = card.nbrs.filter{ card.winNbrs.contains(it) }.size
    for (i in 0..nbrWin-1) {
        cardTracker[cardNbr+i] += amount
    }
    return amount + calculateCardHelper(cardNbr+1)
}