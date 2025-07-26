#include "Board.h"
#include "Coordinate.h"
#include "Piece.h"
#include "King.h"
#include <cmath>
#include <iostream>
using namespace std;

bool King::canMove(const Board& board, const Move& move) const {
    Coordinate from = move.from;
    Coordinate to = move.to;
    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    Colour colour = getColour();
    Attacked atk = board.getAttackingSquare(to);
    Colour opposite = (colour == Colour::WHITE? Colour::BLACK : Colour::WHITE);
    bool destUnderAttack = false;
    if (opposite == Colour::WHITE) {
        destUnderAttack = atk.isAttackedByWhite();
    } else {
        destUnderAttack = atk.isAttackedByBlack();
    }

    if (fr < 1 || fr > 8 || fc < 1 || fc > 8 || tr < 1 || tr > 8 || tc < 1 || tc > 8 ||
        (fr == tr && fc == tc) || destUnderAttack) {
        return false;
    }

    int dr = std::abs(fr-tr), dc = std::abs(fc-tc);
    if (dr <= 1 && dc <= 1) {
        Piece *dest = board.getPiece(to);
        return !dest || dest->getColour() != colour;
    }
    if (dr == 0 && dc == 2) {
        return isValidCastle(board, Move(from, to));
    }
    return false;
}

bool King::isValidCastle(const Board& board, const Move& move) const {
    Coordinate from = move.from, to = move.to;
    int fr = from.r;
    int fc = from.c;
    int tr = to.r;
    int tc = to.c;
    int r = (colour == Colour::WHITE ? 8 : 1);
    if (fr != r || fc != 5 || tr != r) {
        return false;
    }
    const int targetFile[2] = {3, 7};
    const int betweenFiles[2][3] = {{2, 3, 4}, {6, 7}};
    const int betweenCount[2] = {3, 2}; // 2 means to ignore the 0 in the array betweenFiles

    for (int side = 0; side < 2; side++) {
        if (tc != targetFile[side]) {
            continue;
        }
        bool canCastle = false;
        if (colour == Colour::WHITE) {
            if (side == 0) {
                canCastle = board.canWhiteCastleQueenSide();
            } else {
                canCastle = board.canWhiteCastleKingSide();
            }
        } else {
            if (side == 0) {
                canCastle = board.canBlackCastleQueenSide();
            } else {
                canCastle = board.canBlackCastleKingSide();
            }
        }
        if (!canCastle) {
            break;
        }

        bool clear = true;
        for (int i = 0; i < betweenCount[side]; i++) {
            int file = betweenFiles[side][i];
            if (board.getPiece({r, file})) {
                clear = false;
                break;
            }
        }
        if (!clear) {
            break;
        }
        return !board.isInCheck(colour);
    }
    return false;
}

void King::setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const {
    const int DIRS[8][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    int startR = from.r;
    int startC = from.c;
    Colour opposite = (colour == Colour::WHITE? Colour::BLACK : Colour::WHITE);
    for (auto& d : DIRS) {
        int dr = d[0];
        int dc = d[1];
        int r = startR + dr;
        int c = startC + dc;
        if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
            board.setAttackedSquare(Coordinate(r, c), opposite, toAdd);
        }
    }
}

std::unique_ptr<Piece> King::clone() const { return std::make_unique<King>(*this); };
