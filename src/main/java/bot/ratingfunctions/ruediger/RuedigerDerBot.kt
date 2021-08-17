package bot.ratingfunctions.ruediger

import model.Token

class RuedigerDerBot(side: Token) : IRuediger(side) {
    override fun enhanceMaps() {
        val beginner = side
        // only works when board has even height
        if (height % 2 == 1) return
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (beginner == side) {
                    //this bot should aim for lines 0, 2, 4, 6
                    if (ownThreatMap[x][y] == 3 && y % 2 == 0) ownThreatMap[x][y] = 6
                    if (opponentThreatMap[x][y] == 3 && y % 2 == 1) opponentThreatMap[x][y] = 6
                } else {
                    //this bot should aim for lines 1, 3, 5
                    if (opponentThreatMap[x][y] == 3 && y % 2 == 0) opponentThreatMap[x][y] = 6
                    if (ownThreatMap[x][y] == 3 && y % 2 == 1) ownThreatMap[x][y] = 6
                }
            }
        }
    }
}