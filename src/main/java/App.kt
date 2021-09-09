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
        Controller().startNewGame()
    }
}