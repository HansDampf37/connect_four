package model

enum class Token {
    PLAYER_1 {
        override fun other() = PLAYER_2
    },
    PLAYER_2 {
        override fun other() = PLAYER_1
    },
    EMPTY {
        override fun other() = EMPTY
    };

    abstract fun other(): Token
}