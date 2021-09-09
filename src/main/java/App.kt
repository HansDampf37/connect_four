import bot.bots.PonderingBot
import bot.ratingfunctions.ruediger.RuedigerDerBot
import gui.GUIHumanPlayer
import model.Token
import model.procedure.ConsoleOutput
import model.procedure.Game

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        ConsoleOutput.setAll(false, false, false, false, false, false, false, false, false)
        val bot = PonderingBot(Token.PLAYER_1, RuedigerDerBot(Token.PLAYER_1))
        val human = GUIHumanPlayer(Token.PLAYER_2)
        val human2 = GUIHumanPlayer(Token.PLAYER_1)
        val game = Game(human, bot, 9, 10)
        game.play()
    }
}