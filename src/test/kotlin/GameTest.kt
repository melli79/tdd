
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

object GameTest {
    var game = Game()
    @BeforeEach fun init() {
        game = Game()
    }

    @Test fun roll0Once_results0points() {
        game.roll(0)
        val result = game.points
        assertEquals(0, result)
    }

    @Test fun roll1Once_results1point() {
        game.roll(1)
        val result = game.points
        assertEquals(1, result)
    }

    @Test fun roll0Game_resultsGameOver() {
        roll0Game()
        assertTrue(game.isOver())
    }

    private fun roll0Game() {
        repeat(20) {
            game.roll(0)
        }
    }

    @Test fun roll11_fails() {
        val result = assertThrows(IllegalArgumentException::class.java) {
            game.roll(11)
        }
        assertNotNull(result)
    }

    @Test fun roll_1_fails() {
        val result = assertThrows(IllegalArgumentException::class.java) {
            game.roll(-1)
        }
        assertNotNull(result)
    }

    @Test fun roll21times_fails() {
        roll0Game()
        val result = assertThrows(IllegalStateException::class.java) {
            game.roll(0)
        }
        assertNotNull(result)
    }

    @Test fun roll20thStrike_isNotOver() {
        repeat(19) { game.roll(0) }
        game.roll(10)
        assertFalse(game.isOver())
    }
}
