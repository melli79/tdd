
class Game {
    var points = 0
      private set
    var rounds = 0
      private set
    private var bonusRolls = 0
    private var tripleRoll = false
    private var lastOddRoll = 0

    fun roll(points :Int) {
        handleRound()
        checkPointRange(points)
        saveOddRollPoints(points)
        addPoints(points)
        checkForBonusRoll(points)
    }

    private fun addPoints(points :Int) {
        if (tripleRoll)
            this.points += points * 3
        else if (bonusRolls > 0 && rounds <= 20)
            this.points += points * 2
        else
            this.points += points
    }

    private fun saveOddRollPoints(points :Int) {
        if (rounds % 2 == 1 && points < 10)
            lastOddRoll = points
    }

    private fun handleRound() {
        if (isOver()) throw IllegalStateException("Cannot roll more than 20 times per Game.")
        rounds++
    }

    private fun checkPointRange(points :Int) {
        if (points < 0 || points > 10) throw IllegalArgumentException("Cannot roll fewer than 0 points or more than 10 points.")
        if (rounds % 2 == 0 && lastOddRoll + points > 10) throw IllegalStateException("Cannot roll more than 10 in a Frame.")
    }

    private fun checkForBonusRoll(points :Int) = when {
        rounds%2==0 -> {
            bonusRolls = if (lastOddRoll + points == 10) 1 else 0
            tripleRoll = false
        }
        points==10 && rounds<=20 -> {
            tripleRoll = bonusRolls>0
            bonusRolls = if (rounds<19) 2 else 3
            if (rounds<20)
                rounds++
            Unit
        }
        else -> {
            if (bonusRolls>0)
                bonusRolls--
            tripleRoll = false
        }
    }

    fun isOver() = rounds >= 20+bonusRolls
}
