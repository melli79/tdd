
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

object GameTest {
    @Test fun createGame() {
        val game = Game()
        assertNotNull(game)
    }
}
