
class Game {
    var points = 0
      private set
    var rounds = 0
      private set

    fun roll(points :Int) {
        if (points<0||points>10) throw IllegalArgumentException("Cannot roll fewer than 0 points or more than 10 points.")
        if (rounds>=20) throw IllegalStateException("Cannot roll more than 20 times per Game.")
        this.points += points
        rounds++
    }

    fun isGameOver() = rounds >= 20
}
