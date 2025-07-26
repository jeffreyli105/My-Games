#ifndef TEXTDISPLAY_H
#define TEXTDISPLAY_H

#include <iostream>
#include "Observer.h"
#include "Piece.h"
#include "Board.h" 

const char KING = 'K';
const char QUEEN = 'Q';
const char BISHOP = 'B';
const char ROOK = 'R';
const char KNIGHT = 'N';
const char PAWN = 'P'; 
const int WHITEBUFFER = 0; 
const int BLACKBUFFER = 'a' - 'A'; 

class TextDisplay : public Observer {
    char display [SIZE][SIZE]; 
public: 
    TextDisplay(Board &board); 
    void notify(Board &board) override;  
    void setLastMove(const Move& move) override {}
    void init(Board &board) override {notify(board); }
    void clearSquare(Coordinate c) override {}
    void specialMove(Board &board, const Move &move) override {}
    void promotion(Board &board, PieceType type, Coordinate c) override {}
};

#endif
