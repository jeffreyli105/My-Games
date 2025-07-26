#ifndef COMPUTERPLAYER3_H
#define COMPUTERPLAYER3_H
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Colour.h"

using namespace std;

class ComputerPlayer3: public Player {
    vector<Move> getMoves(const Board& board) const;
    int evaluate(const Board& board) const;
public:
    ComputerPlayer3(Colour c): Player(c) { }
    Move makeMove(const Board& board) const override;
};

#endif
