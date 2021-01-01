
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

object GameTest {
    @Test fun createGame() {
        val game = Game()
        assertNotNull(game)
    }

    @Test fun roll() {
        val game = Game()
        game.roll(0)
        val result = game.points
        assertEquals(0, result)
    }
}
