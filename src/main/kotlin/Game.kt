
class Game {
    var points = 0
      private set
    var rounds = 0
      private set
    private var bonusRoll = false
    private var lastOddRoll = 0

    fun roll(points :Int) {
        if (isOver()) throw IllegalStateException("Cannot roll more than 20 times per Game.")
        rounds++
        checkPointRange(points)
        if (rounds%2==1)
            lastOddRoll = points
        if (rounds%2==1 && bonusRoll)
            this.points += points*2
        else
            this.points += points
        checkForBonusRoll(points)
    }

    private fun checkPointRange(points :Int) {
        if (points < 0 || points > 10) throw IllegalArgumentException("Cannot roll fewer than 0 points or more than 10 points.")
        if (rounds % 2 == 0 && lastOddRoll + points > 10) throw IllegalStateException("Cannot roll more than 10 in a Frame.")
    }

    private fun checkForBonusRoll(points :Int) {
        if (rounds%2==0)
            bonusRoll = lastOddRoll+points == 10
    }

    fun isOver() = rounds >= if(bonusRoll) 21 else 20
}
