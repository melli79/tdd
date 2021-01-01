
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

object GameTest {
    var game = Game()
    @BeforeEach fun init() {
        game = Game()
    }

    @Test fun roll() {
        game.roll(0)
        val result = game.points
        assertEquals(0, result)
    }

    @Test fun roll1Once() {
        game.roll(1)
        val result = game.points
        assertEquals(1, result)
    }
}
