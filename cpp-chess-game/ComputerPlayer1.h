#ifndef COMPUTERPLAYER1_H
#define COMPUTERPLAYER1_H
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Colour.h"

class ComputerPlayer1: public Player {
public:
    ComputerPlayer1(Colour c): Player(c) { }
    Move makeMove(const Board& board) const override;
};

#endif
