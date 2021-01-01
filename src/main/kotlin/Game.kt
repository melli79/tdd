
class Game {
    var points = 0
      private set
    var rounds = 0
      private set

    fun roll(points :Int) {
        this.points += points
        rounds++
    }

    fun isGameOver() = rounds >= 20
}
