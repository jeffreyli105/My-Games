#ifndef GAME_H
#define GAME_H

#include "Board.h"
#include "Colour.h"
#include "Move.h"

class Game {
public:
    Game();
    void start(Board& board);
    bool move(Board& board, const Move& move);
    void resign(Colour c);
    bool hasWhiteWon(const Board& b);
    bool hasBlackWon(const Board& b);
    bool hasTied(const Board& b);
};

#endif
