import model.procedure.ConsoleOutput

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        ConsoleOutput.setAll(false, false, false, false, false, false, false, false, false)
        Controller().startNewGame()
    }
}