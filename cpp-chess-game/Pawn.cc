#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include "Colour.h"
#include "Pawn.h"
#include <cmath>
#include <iostream>
using namespace std;

bool Pawn::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;
    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    if (fr < 1 || fr > 8 || fc < 1 || fc > 8 || tr < 1 || tr > 8 || tc < 1 || tc > 8) {
        return false;
    }

    int dir = (colour == Colour::WHITE? -1 : 1);
    int startRank = (colour == Colour::WHITE? 7 : 2);
    Piece *dest = board.getPiece(to);

    // Single-square move
    if (fc == tc && fr + dir == tr && !dest) {
        return true;
    }
    // Two-square initial move
    if (fr == startRank && fr + 2 * dir == tr && fc == tc && !board.getPiece({fr + dir, fc}) && !dest) {
        return true;
    }
    // Normal capture
    if (fr + dir == tr && std::abs(fc - tc) == 1 && dest && colour != dest->getColour()) {
        return true;
    }
    // En passant
    Coordinate ep = board.getEnPassantTarget();
    if (std::abs(fc - tc) == 1 && fr + dir == tr && !dest && ep.r == tr && ep.c == tc) {
        return true;
    }
    return false;
}

void Pawn::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    int fr = from.r;
    int fc = from.c;
    int rowStep = (colour == Colour::WHITE ? -1 : 1);
    int attackRow = fr + rowStep;
    Colour opposite = (colour == Colour::WHITE? Colour::BLACK : Colour::WHITE);
    
    for (int dc : {-1, 1}) {
        int attackCol = fc + dc;
        if (attackRow >= 1 && attackRow <= 8 && attackCol >= 1 && attackCol <= 8) {
            board.setAttackedSquare(Coordinate(attackRow, attackCol), opposite, toAdd);
        }
    }
}

std::unique_ptr<Piece> Pawn::clone() const { return std::make_unique<Pawn>(*this); };
