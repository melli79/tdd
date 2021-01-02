
class Game {
    var points = 0
      private set
    var rounds = 0
      private set
    private var bonusRolls = 0
    private var lastOddRoll = 0

    fun roll(points :Int) {
        handleRound()
        checkPointRange(points)
        if (rounds%2==1 && points<10)
            lastOddRoll = points
        if (bonusRolls>0 && rounds<=20)
            this.points += points*2
        else
            this.points += points
        checkForBonusRoll(points)
    }

    private fun handleRound() {
        if (isOver()) throw IllegalStateException("Cannot roll more than 20 times per Game.")
        rounds++
    }

    private fun checkPointRange(points :Int) {
        if (points < 0 || points > 10) throw IllegalArgumentException("Cannot roll fewer than 0 points or more than 10 points.")
        if (rounds % 2 == 0 && lastOddRoll + points > 10) throw IllegalStateException("Cannot roll more than 10 in a Frame.")
    }

    private fun checkForBonusRoll(points :Int) {
        if (rounds%2==0)
            bonusRolls = if (lastOddRoll+points == 10) 1 else 0
        else if (points==10) {
            bonusRolls = 2
            if (rounds<20)
                rounds++
        } else if (bonusRolls>0)
            bonusRolls--
    }

    fun isOver() = rounds >= 20+bonusRolls
}
