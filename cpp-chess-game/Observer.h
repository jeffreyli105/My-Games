#ifndef OBSERVER_H
#define OBSERVER_H

#include "Board.h"
#include "Piece.h"

class Board;
enum class PieceType;

class Observer {
public:  
    virtual void notify(Board &board) = 0; 
    virtual void setLastMove(const Move& move) = 0; 
    virtual void init(Board &board) = 0; 
    virtual void clearSquare(Coordinate c) = 0;
    virtual void specialMove(Board &board, const Move &move) = 0;
    virtual void promotion(Board &board, PieceType type, Coordinate c) = 0; 
};

#endif
