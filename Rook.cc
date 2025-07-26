#include "Rook.h"
#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include <algorithm>

bool Rook::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;
    
    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    if (fr < 1 || fr > 8 || fc < 1 || fc > 8 || tr < 1 || tr > 8 || tc < 1 || tc > 8 ||
        (fr != tr && fc != tc) || (fr == tr && fc == tc)) {
        return false;
    }
    
    if (fr == tr) {
        int start = std::min(fc, tc) + 1;
        int end = std::max(fc, tc) - 1;
        for (int c = start; c <= end; c++) {
            if (board.getPiece({fr, c})) {
                return false;
            }
        }
    } else {
        int start = std::min(fr, tr) + 1;
        int end = std::max(fr, tr) - 1;
        for (int r = start; r <= end; r++) {
            if (board.getPiece({r, fc})) {
                return false;
            }
        }
    }
    
    return true;
}

void Rook::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    const int DIRS[4][2] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
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

std::unique_ptr<Piece> Rook::clone() const { return std::make_unique<Rook>(*this); };
