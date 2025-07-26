#ifndef HUMANPLAYER_H
#define HUMANPLAYER_H
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Colour.h"
#include <string>

using namespace std;

class HumanPlayer: public Player {
    Coordinate parse_coord(const string& coord) const;
    bool isValidCoord(const string& coord) const;
public:
    HumanPlayer(Colour c): Player(c) { }
    Move makeMove(const Board& board) const override;
};

#endif
