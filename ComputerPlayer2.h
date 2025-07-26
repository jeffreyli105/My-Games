#ifndef COMPUTERPLAYER2_H
#define COMPUTERPLAYER2_H
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Colour.h"

using namespace std;

class ComputerPlayer2: public Player {
    vector<Move> getMoves(const Board& board) const;
    int evaluate(const Board& board) const;
public:
    ComputerPlayer2(Colour c): Player(c) { }
    Move makeMove(const Board& board) const override;
};

#endif
