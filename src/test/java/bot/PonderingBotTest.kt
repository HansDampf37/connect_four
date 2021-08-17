package bot

import junit.framework.TestCase
import model.Board
import model.Token
import java.lang.Thread.sleep

class RandomRat : RatingFunction {
    override fun name() = "Bogo"
    override fun invoke(p1: Board): Int = (0..100).random()
}

class PonderingBotTest : TestCase() {

    val ponderingBot = PonderingBot(side = Token.PLAYER_1, Board(), RandomRat())

    public override fun setUp() {
        super.setUp()
    }

    fun testTree() {
        val thread = Thread(ponderingBot)
        thread.start()
        sleep(1000)
    }
}