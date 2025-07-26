#ifndef MOVE_H
#define MOVE_H
#include "Coordinate.h"
 
class Move {
public:
    Coordinate from;
    Coordinate to;
 
    Move(): from{Coordinate(0, 0)}, to{Coordinate(0, 0)} {}
    Move(const Coordinate& from, const Coordinate& to):
    from {from}, to {to} {}
};

#endif
