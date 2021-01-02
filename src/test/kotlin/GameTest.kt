
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

    @Test fun halfStrike_getsDoubleNextRollsPoints() {
        game.roll(5);  game.roll(5)
        val before = game.points
        game.roll(3)
        val after1 = game.points
        game.roll(2)
        val after2 = game.points
        assertEquals(2*3, after1-before) // doubled
        assertEquals(1*2, after2-after1) // not doubled
    }

    @Test fun fullStrike_getNext2RollsDoubledPoints() {
        game.roll(10)
        val before = game.points
        game.roll(5)
        game.roll(4)
        val after = game.points
        game.roll(1)
        val later = game.points
        assertEquals(2*(5+4), after-before)
        assertEquals(1*1, later-after)
    }

    @Test fun fullStrikeIntheEnd_endsAfter22Rolls() {
        rollMany0s(18)
        game.roll(10)
        val before = game.points
        game.roll(10)
        game.roll(10)
        val after = game.points
        assertTrue(game.isOver())
        assertEquals(10+10, after-before) // these are bonus rolls
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

    @Test fun roll11inFrame_fails() {
        game.roll(5)
        assertThrows(IllegalStateException::class.java) {
            game.roll(6)
        }
    }

    @Test fun roll0Game_resultsGameOver() {
        rollMany0s(20)
        assertTrue(game.isOver())
    }

    private fun rollMany0s(rounds :Int) {
        repeat(rounds) {
            game.roll(0)
        }
    }

    @Test fun roll21times_fails() {
        rollMany0s(20)
        val result = assertThrows(IllegalStateException::class.java) {
            game.roll(0)
        }
        assertNotNull(result)
    }

    @Test fun roll20thStrike_isNotOver() {
        rollMany0s(19)
        game.roll(10)
        assertFalse(game.isOver())
    }

    @Test fun rollLastStrikeIn2_isNotOver() {
        rollMany0s(18)
        game.roll(5)
        game.roll(5)
        assertFalse(game.isOver())
    }
}
