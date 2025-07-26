#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include "Bishop.h"
#include <algorithm>
#include <cmath>

bool Bishop::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;
    
    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    if (fr < 1 || fr > 8 || fc < 1 || fc > 8 || tr < 1 || tr > 8 || tc < 1 || tc > 8 ||
        (std::abs(fr - tr) != std::abs(fc - tc)) || (fr == tr && fc == tc)) {
        return false;
    }
    
    int rowStep = (tr > fr)? 1 : -1;
    int colStep = (tc > fc)? 1 : -1;
    int r = fr + rowStep;
    int c = fc + colStep;

    while (r != tr && c != tc) {
        if (board.getPiece({r, c})) {
            return false;
        }
        r += rowStep;
        c += colStep;
    }

    Piece *dest = board.getPiece(to);
    return !dest || dest->getColour() != colour;
}

void Bishop::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    const int DIRS[4][2] = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    int startR = from.r;
    int startC = from.c;
    Colour opposite = (colour == Colour::WHITE? Colour::BLACK : Colour::WHITE);
    for (auto& d : DIRS) {
        int dr = d[0];
        int dc = d[1];
        int r = startR + dr;
        int c = startC + dc;

        while (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            board.setAttackedSquare(Coordinate{r, c}, opposite, toAdd);
            if (board.getPiece(Coordinate{r, c})) {
                break;
            }
            r += dr;
            c += dc;
        }
    }
}

std::unique_ptr<Piece> Bishop::clone() const { return std::make_unique<Bishop>(*this); };
