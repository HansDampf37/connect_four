import bot.Ruediger.RuedigerDerBot
import model.Board
import model.HumanPlayer
import model.Token
import model.procedure.Game

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val b = Board()
        val game = Game(HumanPlayer(Token.PLAYER_1, b), RuedigerDerBot(4, Token.PLAYER_2, b))
        game.play()
    }
}