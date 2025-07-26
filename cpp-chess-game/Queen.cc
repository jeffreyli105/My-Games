#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include "Bishop.h"
#include "Rook.h"
#include "Queen.h"

bool Queen::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;
    
    Bishop tempBishop = Bishop(colour);
    Rook tempRook = Rook(colour);
    return tempBishop.canMove(board, Move{from, to}) || tempRook.canMove(board, Move{from, to});
}

void Queen::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    Bishop tempBishop = Bishop(colour);
    Rook tempRook = Rook(colour);
    tempBishop.setAttackedSquares(board, from, toAdd);
    tempRook.setAttackedSquares(board, from, toAdd);
}

std::unique_ptr<Piece> Queen::clone() const { return std::make_unique<Queen>(*this); };
