#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include "Knight.h"
#include <cmath>

bool Knight::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;

    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    if (fr < 1 || fr > 8 || fc < 1 || fc > 8 || tr < 1 || tr > 8 || tc < 1 || tc > 8) {
        return false;
    }
    
    int dr = std::abs(fr - tr);
    int dc = std::abs(fc - tc);
    Piece *dest = board.getPiece(to);
    if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2)) {
        return !dest || dest->getColour() != colour;
    }
    return false;
}

void Knight::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    const int DIRS[8][2] = {{-2, -1}, {-2, 1}, {-1, -2}, {1, -2}, {-1, 2}, {1, 2}, {2, -1}, {2, 1}};
    int startR = from.r;
    int startC = from.c;
    Colour opposite = (colour == Colour::WHITE? Colour::BLACK : Colour::WHITE);
    for (auto& d : DIRS) {
        int dr = d[0];
        int dc = d[1];
        int r = startR + dr;
        int c = startC + dc;
        if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            board.setAttackedSquare({r, c}, opposite, toAdd);
        }
    }
}

std::unique_ptr<Piece> Knight::clone() const { return std::make_unique<Knight>(*this); };
