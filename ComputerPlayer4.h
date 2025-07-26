#ifndef COMPUTERPLAYER4_H
#define COMPUTERPLAYER4_H
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Colour.h"
#include <vector>

using namespace std;

class ComputerPlayer4: public Player {
    const int MINSCORE = -99999, MAXSCORE = 99999, DEPTH = 3;
    vector<Move> getMoves(const Board& board, Colour curPlayer) const;
    double evaluate(const Board& board, Colour curPlayer, int depth, double bestWhite, double bestBlack) const;
    double evaluate(const Board& board, Colour curPlayer) const;
    const double tilePoint[64] = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2,
                                  0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2,
                                  0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2,
                                  0.2, 0.3, 0.4, 0.5, 0.5, 0.4, 0.3, 0.2,
                                  0.2, 0.3, 0.4, 0.5, 0.5, 0.4, 0.3, 0.2,
                                  0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2,
                                  0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2,
                                  0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2};
public:
    ComputerPlayer4(Colour c): Player(c) { }
    Move makeMove(const Board& board) const override;
};

#endif
