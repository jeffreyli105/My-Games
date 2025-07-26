#ifndef GRAPHICDISPLAY_H
#define GRAPHICDISPLAY_H

#include "Observer.h"
#include "Board.h" 
#include "window.h"
#include <iostream>
#include "Move.h"
#include <string>
#include "Coordinate.h"
#include "Piece.h"

const int CELL_SIZE = 60;

class GraphicDisplay : public Observer {
    Xwindow window;
    std::string pieceToString(Piece *p); 
    Move lastMove;
public: 
    GraphicDisplay(Board &board); 
    void notify(Board &board) override;  
    void setLastMove(const Move& move) { lastMove = move; }
    void init(Board &board) override;
    void clearSquare(Coordinate c); 
    void specialMove(Board &board, const Move &move) override; 
    void promotion(Board &board, PieceType type, Coordinate c) override;
};

#endif
