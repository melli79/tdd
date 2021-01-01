
class Game {
    var points = 0
      private set
    var rounds = 0
      private set
    private var bonusRoll = false
    private var lastOddRoll = 0

    fun roll(points :Int) {
        if (points<0||points>10) throw IllegalArgumentException("Cannot roll fewer than 0 points or more than 10 points.")
        if (isOver()) throw IllegalStateException("Cannot roll more than 20 times per Game.")
        this.points += points
        rounds++
        if (rounds==19)
            lastOddRoll = points
        if (rounds==20&&lastOddRoll+points==10)
            bonusRoll = true
    }

    fun isOver() = rounds >= if(bonusRoll) 21 else 20
}
