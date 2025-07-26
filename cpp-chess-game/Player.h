#ifndef PLAYER_H
#define PLAYER_H
#include "Move.h"
#include "Board.h"
#include "Colour.h"

class Player {
protected:
    Colour colour;
public:
    Player(Colour c): colour{c} { }
    virtual ~Player() = default;
    virtual Move makeMove(const Board& board) const = 0;
};

#endif
