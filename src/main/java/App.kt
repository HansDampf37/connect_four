import bot.PonderingBot
import bot.ratingfunctions.ruediger.RuedigerDerBot
import model.Board
import model.HumanPlayer
import model.Token
import model.procedure.Game

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val b = Board()
        val bot = PonderingBot(Token.PLAYER_2, b, RuedigerDerBot(Token.PLAYER_2))
        Thread(bot).start()
        val game = Game(HumanPlayer(Token.PLAYER_1, b), bot)
        game.play()
    }
}