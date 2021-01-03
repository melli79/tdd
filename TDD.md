# Test-driven Development

# 0 Result of DDD
Remember that the result of [Domain-driven Development](https://www.Domian-Driven-Design.org/) is a *Core Domain* together with Actors, *Domain objects*, *Domain Events*, actions and a *Domain Boundary*.

## Example: A Bowling algorithm

So from a DDD perspective the actors in bowling are the **Player**s that *roll* the bowling ball and thus kick down some pins (out of 10).  The game consists of 10 **Frame**s.  In every Frame you have at 2 **Rolls**, in the first roll you throw into a *full* **Setting**, in the second roll you continue with what was left after the first roll.  In the Frame you get the *point*s for the number of pins you have kicked down.  A *half-strike* is when you have kicked down all pins in the 2 rolls (marked '/').  In that case you count the points from the next roll into the current Frame (marked 'X').  A *full strike* is when you kick down all pins in the first roll.  In this case there is no second Roll in the Frame, but the points from the next 2 rolls are counted towards the current Frame.  In the last Frame when you roll a half-strike or full strike, there are additional rolls that extend the Frame and count towars its total Points.  The whole **Game** is a competition between 1 or more players which roll in turns, every one a Frame and the winner of the Game is who got the most Points.

So some side domains are the Food-and-beverage-Service around the bowling hall, the Machine-repair-*Service* (if e.g. a bowling ball gets stuck in the machine) or the Payment-Service.

## Literature

[1] R.C. Martin: [Clean Code](https://cleanCoder.com/), the Lost Years **2008**; [Test-Driven Development](http://cleancoder.com/files/tdd.md) **201x**; .

## Copyright (c) 2021  CC BY-SA
As stated above, the main idea for this TDD game was taken from Uncle Bob.  Therefore I do not claim copyright for the ideas.  If you want the lecture to be read in a charming way, please consult Uncle Bob.

The translation to Kotlin as well as the detailed formulations here were made by me, use under the APL 2.0.


# 1 Starting Development

Depending on the expected (initial) size of your application, you can write a hull of it, say from few business logic classes or an API and then implement down from there (top down sketch). This keeps your focus on where to go.

However, before your app goes life, you will probably have to test it. The more automated tests you have, the more reliably you can discover bugs. The earlier you start testing, the faster you can discover bugs. So *why not start with testing even before writing* lots of *code*?

Once you are clear what is a central component, you start writing a test for it.

## in the Example
Let us just write a small bowling machine that keeps track of the current state of the **Game** and allows the clients to *roll* the ball with argument the *numberOfKickedDownPins*.

# 2 Development Cycle: Red, Green, Refactoring

So you have decided that you need a public function *doXxx* with an initial argument *foo* and a result type *Baz*. Before you implement its business logic, think about how you would test that? How can you automate that? What would be an appropriate test case? What is the simple most test case?

Write this test case in a test class parallel to your business logic. If you start with a function write a test class solely for testing this function. Write a method within that `@Test doXxx1() throws Exception`.  A Test acts in 3 steps,

1. prepare,
2. invoke,
3. verify.

![TDD Hat](tddHat.jpg)

**Red.** Write only as much test code that is logically correct but that fails. Failure can mean 3 things:

1. Fails compilation due to lacking code, signature mismatch;
2. Fails execution due to missing method body, missing abort criteria or unhandled exceptional state;
3. Fails verification due to missing/incorrect details of the solution.

**Green.** No matter what the cause of failure is, you can now go to fix the problem, but only with as little code as possible.  Write the simple most solution (remember the KISS principle).  Once your test passes you can

**Refactor.** Now think about whether there is a better way to integrate the new code with the surroundings. Whether your function has become too big, whether you have too many arguments that require too many special cases, all kinds of code smells that tell you that your solution is not well suited towards the agile development.

One thing you can do here is that after each refactoring (and still all tests passing), you can check in the code (make a commit, but don't push yet).  With a little practice, you get these Red-Gree-Refactor cycles down to few minutes.

# 3 Example: So what is the simplest test case?

If you are reading the documentation in a git repository, copy the TDD.md to some temporary place for reading and check out the "initial setting" and work through the description below as described with your favorit Kotlin IDE.  Whenever you get stuck at a point, you man checkout the corresponding solution from the repository.  Remember that I do not want to destroy the sample solution, so pull requests to it will rarely be granted (say in a case of a typo or much better solution).

## 1. Red.
Is it rolling a whole Frame?  Or is it rolling once and counting the Points? or is it Initializing the Game?  It turns out that if you have not written any application code, as you should, then you start with instantiating the Game class.  So a test would look as follows:

```kotlin
class GameTest {
  @Test fun createGame() {
    val game = Game()
    assertNotNull(game)
  }
}
```

And surprise, it does not compile (cause 1).

***1. Green.*** So we are now free to write some code. How much?  Remember, just as much code as to make the test pass.

```kotlin
class Game {}
```

That's it, no fancy initialization, no inner states or similar.  Now the test passes.

***1. Refactoring.***  Now, do we need refactoring?  Probably there is not too much code (given that we wanted a class for the Game).  What about the name?  Yes, bad naming is a code smell and remember that the name of a public class is easier to change in the beginning than after having written 1000 access points to it (even though automated refactoring still helps in such a case).

What alternatives do we have?  We could call it *BowlingGame* or just *G* or [org.grutzmann.bowling.Game](http://org.grutzmann.bowling.Game) or ... .  There are many options (if you only see one, you may not yet have thought about it thoroughly yet).  There is no unique universally best option, but there is usually a good choice for the goal at which you are aiming.  If you were to publish the class as a package (public domain or commercially) that is to be integrated into an e.g. bowling business, then putting it into an appropriately named package is certainly a good idea.  On the other hand, we want to implement the logica just for educational purposes and see how far we get, how much we need, so we will just stick with the name Game and put it into a project called bowling.  Usually it is a bad idea to name a public class *G*, because what is G? Remember that names may stick around much longer than you have thought about them initially, so better think twice.

Where do we go from here?  We can decide to implement the Game logic, so we would probably continue with a method *roll*.

## 2. Red.

```kotlin
@Test fun roll() {
   val game = Game()
	 game.roll(0)
   val result = game.points
   assertEquals(0, result)
}
```

So it turned out that beside a method roll, we also need a method points, because we do not only invoke a state change, but also want to observe it.  That is dictated by the test logic.

***2. Green.*** What code do we need?

```kotlin
class Game {
  val points = 0
  fun roll(rollPoints :Int) {} 
}
```

Wait, don't we want the passed *rollPoints* to be stored in the *`var points`*?  Have a look at our test.  Does it pass?  Yes, so we don't currently want any additional code.

***2. Refactoring.*** So is there any duplicate code?

Only 2 methods (meaning an explicitly declared one and a property getter), any duplicates?  Don't see any.  What about the test class?  Yes, test code is first class code.  Even though you may not deliver it to the commercial customer, but still you want fellow developers to read clean test code.  Remember that even yourself in half a year may be this fellow developer.  So what is duplicated there? Look at the line `val game = Game()` it is there twice.  Sure, for multiple test cases we would need to instantiate multiple Games (remember that tests should be decoupled and independent), but does the first test really operate independently from the second test?  No, then why not just remove the first test.

It may first sound strange that we just remove the code we wrote earlier, but remember that software development should be agile.  We don't write monolithic once and forever.  We see that we don't need the first test independently (because the second test will fail anyway if the class does not exist or does not instantiate without failure), so just remove it.  Our test code could now look as follows:

```kotlin
class GameTest {
  @Test fun roll() {
    val game = Game()
		game.roll(0)
    val result = game.points
    assertEquals(0, result)
  }
}
```

Can we do better?  Yes, there is one thing we can do better.  We will have to instantiate the test class in every test.  So why rewrite the test code every time?  Instead we can declare a member variable and instantiate it `@BeforeEach`.  So actually our test class will look as follows:

```kotlin
object GameTest {
  var game = Game()
  @BeforeEach fun init() {
     game = Game()
  }

	@Test fun roll() {
    game.roll(0)
    ...
  }
}
```

## 3. Red.
Let us take care of the points property to sum up the rolledPoints.

```kotlin
@Test fun roll1Once() {
  val game = Game()
  game.roll(1)
  val result = game.points()
  assertEquals(1, result)
}
```

***3. Green.*** What needs to be implemented?

```kotlin
class Game {
  var points :Int =0
  fun roll(points :Int) {
		this.points += points
  }
}
```

***3. Refactoring.*** Anything we can improve?

Again what about the test class?  We have 2 methods that test rolling the ball, but the names do not clearly tell which is which. Also it is common for the methods not only to have different names, but also for the name to tell what the method does (not how). So let us refactor the first method, rename it to *roll0Once_results0points*() and the second method to *roll1Once_results1point()*.

There is another code smell, the property points is publicly accessible, even writeable.  That is bad design.  We need to change that:

```kotlin
class Game {
   var points :Int = 0
		 private set
}
```

A quick rerun of our automated tests shows that we did not break anything from the features implemented so far.

## 4. Red.
What about a whole game.  How can we know when the game is over?

```kotlin
@Test fun roll0Game() {
  val game = Game()
  repeat(20)  {
    game.roll(0)
  }
  assertTrue(game.isGameOver())
}
```

***4. Green.***

```kotlin
class Game {
  var points = 0
		private set
  var round = 0
    private set

  fun roll(0 :points) {
    this.points += points
    round++
  }

  fun isGameOver() = round>=20
}
```

***4. Refactor.***

There is an apparent problem.  Is it really good to keep the round separate from the total points?  Shouldn't it rather consist of keeping track of the individual rolls?  We could refactor that here, but the KISS principle dictates that we should not introduce this burden unless it is required by a spec.  So instead we continue with the specifications — phase

## 5. Red.
Remember that there are only 10 pins, so it does not make sense to roll 11 or more points.

```kotlin
@Test fun roll11() {
  val game = Game()
  val result = AssertThrows(IllegalArgumentException::class.java) {
     game.roll(11)
  }
  assertNotNull(result)
}
```

***5. Green.***

```kotlin
fun roll(points :Int) {
  if (points>10)  throw IllegalArgumentException("Cannot roll more than 10.")
  ...
}
```

And see, it passes.

***5. Refactor.***  At the moment, there is not much code smell. So we leave it as is.

## 6. Red.
What else should we test?  Well, it is illegal to roll a negative number too.

```kotlin
@Test fun roll_1() {
  val game = Game()
  val result = assertThrows<IllegalArgumentException>{ game.roll(-1) }
  assertNotNull(result)
}
```

***6. Green.***

```kotlin
fun roll(points :Int) {
  if(points<0||points>10)  throw IllegalArgumentException("Cannot roll fewer than 0 or more than 10.")
  ...
}
```

***6. Refactor.*** Still no refactoring apparent, but please take a moment to get an overview if really nothing is necessary.

## 7. Red.
We have also corner cases that someone tries to roll more than 20 times.

```kotlin
@Test fun roll21times_fails() {
  val game = Game()
  for (n in 1..20) { game.roll(0) }
  val result = assertThrows<IllegalStateException>{ game.roll(0) }
  assertNotNull(result)
}
```

***7. Green.***

```kotlin
fun roll(points :Int) {
  if (points<0||points>10) ...
  if (round>=20) throw IllegalStateException("Cannot roll more than 20 times.")
  ...
}
```

***7. Refactoring.*** There is one pice of code duplication, namely rolling an empty game.  You may think that this is not much and moreover only in the test code, but remember that test code is first class code and that problems always begin with small code smells, but soon get worse.  Therefore we extract the common part into a auxiliary method as follows:

```kotlin
class GameTest {
  @Test fun roll20times_finishesGame() {
    ...
    roll0Game()
    assertTrue(game.isFinished())
  }

  private fun roll0Game() {
    for(n in 1..20) {
      game.roll(0)
    }
  }

  @Test fun roll21times_fails() {
    ...
    roll0Game()
    val result = assertThrows<IllegalStateException>{ game.roll(0) }
  }
}
```

There is also a second code smell.  When you look at our code, you see that there are 2 places in which we test whether the game is over.  That contradicts the DRY principle, don't repeat yourself.  At this point it may seem superstitious, but what when we later change the rule for gameOver.  Are we certain we will find all places we have to change?  Better make both places refer to the same code.  Therefore we change the method roll(...) as follows:

```kotlin
fun roll(points :Int) {
  ...
  if(isOver())  throw IllegalStateException(...)
  ...
}

fun isOver() = round >= 20 // renaming of the isGameOver() method
```

## 8. Red.
Wait, there is a problem.  We cannot say that it is wrong to roll a 21st time.  Namely if your 20th roll was a strike, then you are allowed to roll a 21st time.  Fortunately the `@Test fun roll21times_fails()` is correct, i.e. in that case it is not ok to roll again.  How can we test the last half-strike?

```kotlin
@Test fun roll20thStrike_isNotFinished() {
  val game = Game()
  for (n in 1..19) { game.roll(0) }
  game.roll(10)
  assertFalse(game.isFinished())
}
```

***8. Green.***

```kotlin
class Game {
  ...
  val bonusRoll = false

  fun roll(points :Game) {
    ...
    if (round==20&&points==10)  bonusRoll=1
  }

  fun isOver() = round >= if(bonusRoll) 21 else 20
}
```

Now it passes.

***8. Refactor.***  Where can we improve?  Maybe in the test code, we have the repeats for rolling many rolls distributed over multiple places, but all they do is fill the game with 0-rolls.  Let us factor out that.

```kotlin
class GameTest {
  @Test fun roll20times_finishesGame() {
    ...
    rollMany0s(20)
    assertTrue(game.isFinished())
  }

  private fun rollMany0s(game :Game, rounds :Int) {
    repeat(rounds) {
      game.roll(0)
    }
  }

  @Test fun roll21times_fails() {
    ...
    rollMany0s(20)
    val result = assertThrows<IllegalStateException>{ game.roll(0) }
  }
  ...
  @Test fun roll20thStrike_isNotFinished() {
    val game = Game()
    rollMany0s(game, 19)
    game.roll(10)
    ...
  }
}
```

And hooray, all tests still pass.

## 9. Red.
Rolling a 10 in the 20th roll is not the only way to get a strike in the last roll.  Instead we should keep the points from the 19th roll and see whether it adds up to 10 with the 20th roll.  But let us test for that.

```kotlin
@Test fun roll20thStrikein2steps_isNotFinished() {
  val game = Game()
  rollMany0s(18)
   game.roll(5)
   game.roll(5)
   assertFalse(game.isFinished())
}
```

***9. Green.***

```kotlin
class Game {
  ...
  private var lastOddRoll = 0
  
  fun roll(points :Int) {
    ...
    if (round==19)
      oddRoll = points
    if (round==20&&lastOddRoll+points==10)
      bonusRound = true
  }
}
```

***9. Refactor.***  The Game.roll() method is getting longer.  Maybe we can split off the part `updateBonusRoll()`.  While we do that, we will also write the assignment not as an `= true`, but as `= lastOddRoll+points==10`.  This leads to

```kotlin
fun roll(points :Int) {
  ...
  checkForBonusRoll()
}

private fun checkForBonusRoll(points :Int) {
  if (round==20)
    bonusRoll = lastOddRoll+points == 10
}
```

## 10. Red.
What about not throwing more than 10 points in a frame?

```kotlin
@Test fun roll11inOneFrame_fails() {
  game.roll(5)
  val result = assertThrows(IllegalStateException::class.java) {
    game.roll(6)
  }
  assertNotNull(result)
}
```

***10. Green.***

```kotlin
fun roll(points :Int) {
  ...
  rounds++
  if (round%2==0&&lastOddRoll+points>10)  throw IllegalStateException("Cannot roll more than 10 points in a Frame.")
  if (round%2==1)
    lastOddRoll = points
  this.points += points
  checkForBonusRoll()
}
```

***10. Refactoring.*** When we look at the Game.roll method, we see many if statements.  The question is if we can group them better to separate concerns.  One possibility is to axtract `checkValidPoints`.  This would look as follows:

```kotlin
fun roll(points :Int) {
  if (isOver())  throw IllegalStateException("Game is already over.")
  round++
  checkPointRange(points)
  this.points += points
  if (round%2==1)
    lastOddRoll = points
  checkForBonusRoll(points)
}

fun checkPointRange(points) {
  if (points<0||points>10)  throw IllegalArgumentException("Cannot roll fewer than 0 or more than 10 points.")
  if (round%2==0 && lastOddRoll+points>10) throw IllegalStateException("Cannot roll more than 10 points in a Frame.")"
}
```

Since we now have more than 5 tests, it may be a good idea to group the tests.  Currently I only sorted them by adding points, rolling illegally, ending the game.

## 11. Red.
What about doubling the points for the next roll after a strike?

```kotlin
@Test fun halfStrike_getsDoubledNextRollsPoints() {
  game.roll(5);  game.roll(5)
  val before = game.points
  game.roll(2)
  val after = game.points
  assertEquals(2*2, after-before)
}
```

***11. Green.***

```kotlin
fun roll(points :Int) {
  ...
  if(round%2==01 && bonusRoll)
    this.points += points *2
  else 
    this.points += points
  checkForBonusRoll(points)
}

fun checkForBonusRoll(points :Int) {
  if (rounds%2==0)
    bonusRoll = lastOddRoll+points == 10
}
```

***11. Refactor.*** The `Game.roll` method is getting longer again.  Maybe we should split away the `roundHandling`, e.g. as follows:

```kotlin
fun roll(points :Int) {
  handleRound()
  checkPointRange(points)
  ...
}

fun handleRound() {
  if (isOver) throw IllegalStateException("Cannot roll after the Game isOver.")
  rounds++
}
```

## 12. Red.
What about full-strikes?

```kotlin
@Test fun fullStrike_next2rollsGetDoubledPoints() {
  game.roll(10)
  val before = game.points
  game.roll(5)
  game.roll(4)
  val after = game.points
  assertEquals(2*(5+4), after-before)
}
```

***12. Green.***

```kotlin
class Game {
  ...
  private var bonusRolls = 0
  ...
  fun roll(points :Int) {
    ...
    if (round%2==1 && points<10)
      lastOddRoll = points
    if (bonusRolls>0)
      this.points += points*2
    else
      this.points += points
    checkForBonusRolls(points)
  }
  ...
  fun checkForBonusRolls(points :Int) {
    if (rounds%2==0)
        bonusRolls = if (lastOddRoll+points == 10) 1 else 0
    else if (points==10) {
        bonusRolls = 2
        if (rounds<20)
            rounds++
    } else if (bonusRolls>0)
        bonusRolls--
    }
}
```

***12. Refactor.***  Don't see much.  But it now becomes aparent why we split away the responsibility from the `Game.roll` method (producing a couple of additional methods).

## 13. Red.
What about a full strike in the end?

```kotlin
@Test fun fullStrikeIntheEnd_endsAfter22Rolls() {
  rollMany0s(18)
  game.roll(10)
  val before = game.points
  game.roll(10)
  game.roll(10)
  val after = game.points
  assertTrue(game.isOver())
  assertEquals(10+10, after-before)
}
```

***13. Green.***

```kotlin
fun roll(points :Int) {
  ...
  if (bonusRounds>0 && rounds<=20)
    this.points += points*2
  else
    this.points += points
  ...
}
```

***13. Refactor.***  Almost done, not much to reshape.

## 14. Red.
What about triple counts?

```kotlin
@Test fun tripleStrike_lastCountsTriple() {
  game.roll(10)
  game.roll(10)
  val before = game.points
  game.roll(10)
  val after = game.points
  assertEquals(3*10, after-before)
}
```

***14. Green.***

```kotlin
class Game {
  ...
  var tripleRound = false
  ...
  fun roll(points :Int) {
    ...
    if (tripleRoll)
      this.points += points*3
    else if (bonusRolls>0 && rounds<20)
      this.points += points*2
    else
      this.points += points
    ...
  }
  ...
  fun checkBonusRolls(points :Int) = when {
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
...
}
```

***14. Refactor.*** We can do one thing.  The function `Game.roll` is not homogeneous, i.e. it has abstractions, via function calls, as well as explicit statements.  We can lift the remaining statements into `saveOddRollPoints` and `addPoints`.

```kotlin
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
```

## 99. MVP
When you look at the function and the test suite, you will see that all cases we can think of are covered.  That is the point when you can publish or go to production.
