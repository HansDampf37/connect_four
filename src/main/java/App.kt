import bot.bots.PonderingBot
import bot.ratingfunctions.ruediger.RuedigerDerBot
import model.Board
import model.HumanPlayer
import model.Token
import model.procedure.Game

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val b = Board()
        val bot = PonderingBot(Token.PLAYER_1, b, RuedigerDerBot(Token.PLAYER_1))
        val game = Game(HumanPlayer(Token.PLAYER_2, b), bot)
        game.play()
    }
}