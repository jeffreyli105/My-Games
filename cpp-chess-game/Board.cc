#include "Board.h"
#include "Piece.h"
#include "Coordinate.h"
#include "Colour.h"
#include "Move.h"
#include <algorithm>
#include "Pawn.h"
#include "Bishop.h"
#include "Knight.h"
#include "Rook.h"
#include "King.h"
#include "Queen.h"
#include "Observer.h"
#include <iostream>

using namespace std;

Board::Board() {
    for (int i = 0; i < SIZE; ++i) {
        for (int j = 0; j < SIZE; ++j) {
            squares[i][j] = nullptr;
            attackedSquares[i][j] = Attacked(0, 0);
        }
    }
    for (int i = 1; i <= 8; ++i) {
        placePiece(PieceType::Pawn, Coordinate(2, i), Colour::BLACK);
        placePiece(PieceType::Pawn, Coordinate(7, i), Colour::WHITE);
    }
    for (int i : {1, 8}) {
        placePiece(PieceType::Rook, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Rook, Coordinate(8, i), Colour::WHITE);
    }
    for (int i : {2, 7}) {
        placePiece(PieceType::Knight, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Knight, Coordinate(8, i), Colour::WHITE);
    }
    for (int i : {3, 6}) {
        placePiece(PieceType::Bishop, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Bishop, Coordinate(8, i), Colour::WHITE);
    }
    placePiece(PieceType::Queen, Coordinate(1, 4), Colour::BLACK);
    placePiece(PieceType::Queen, Coordinate(8, 4), Colour::WHITE);
    placePiece(PieceType::King, Coordinate(1, 5), Colour::BLACK);
    placePiece(PieceType::King, Coordinate(8, 5), Colour::WHITE);

    whiteCanCastleKingSide = true;
    whiteCanCastleQueenSide = true;
    blackCanCastleKingSide = true;
    blackCanCastleQueenSide = true;

    notifyObservers();
}
Board::Board(const Board& board) {
    for (int i = 0; i < SIZE; ++i) {
        for (int j = 0; j < SIZE; ++j) {
            if (board.getPiece(Coordinate(i, j)) == nullptr) squares[i][j] = nullptr;
            else squares[i][j] = board.getPiece(Coordinate(i, j))->clone();
            attackedSquares[i][j] = board.getAttackingSquare(Coordinate(i, j));
        }
    }
    whiteCanCastleKingSide = board.canWhiteCastleKingSide();
    whiteCanCastleQueenSide = board.canWhiteCastleQueenSide();
    blackCanCastleKingSide = board.canBlackCastleKingSide();
    blackCanCastleQueenSide = board.canBlackCastleQueenSide();

}
Piece* Board::getPiece(Coordinate c) const {
    if (c.r < 1 || c.r >= SIZE || c.c < 1 || c.c >= SIZE) {
        return nullptr; // Out of bounds
    }
    return squares[c.r][c.c].get();
}

bool Board::movePiece(Move move, bool freePass) {
    Coordinate f = move.from, t = move.to;
    if (f.r < 1 || f.r >= SIZE || f.c < 1 || f.c >= SIZE ||
        t.r < 1 || t.r >= SIZE || t.c < 1 || t.c >= SIZE) { //check bounds
        return false;
    }
    if (squares[f.r][f.c] == nullptr) { //No piece at f
        return false;
    }
    if (!freePass && !squares[f.r][f.c]->canMove(*this, Move(f, t))) { //invalid move
        return false; 
    }

    if (!freePass) {
        Board next = *this;
        next.movePiece(move, true);
        if (next.isInCheck(getPiece(f)->getColour())) return false;
    }
   

    // Rook move logic for castling
    Piece* piece = getPiece({f.r, f.c});
    piece->setAttackedSquares(*this, f, false);
    Colour colour = piece->getColour();
    if (piece->getType() == PieceType::King && abs(f.c - t.c) == 2) {
        // castling
        if (colour == Colour::WHITE) {
            if (t.c == 3) {
                // White queen side
                squares[8][4] = std::move(squares[8][1]);
                specialMove(Move(Coordinate(8, 1), Coordinate(8, 4)));
            } else if (t.c == 7) {
                // White king side
                squares[8][6] = std::move(squares[8][8]);
                specialMove(Move(Coordinate(8, 8), Coordinate(8, 6)));
            } else {
                return false;
            }
        } else if (colour == Colour::BLACK) {
            if (t.c == 3) {
                // Black queen side
                squares[1][4] = std::move(squares[1][1]);
                specialMove(Move(Coordinate(1, 1), Coordinate(1, 4)));
            } else if (t.c == 7) {
                // Black king side
                squares[1][6] = std::move(squares[1][8]);
                specialMove(Move(Coordinate(1, 8), Coordinate(1, 6)));
            } else {
                return false;
            }
        }
    }
    squares[t.r][t.c] = std::move(squares[f.r][f.c]);
    squares[f.r][f.c] = nullptr;
    // update attacked squares
    // 1) Reset the whole attack grid
    for (int r = 1; r <= 8; ++r) {
        for (int c = 1; c <= 8; ++c) {
            attackedSquares[r][c] = Attacked{0,0};
        }
    }
    // 2) For every piece still on the board, mark its attacked squares
    for (int r = 1; r <= 8; ++r) {
        for (int c = 1; c <= 8; ++c) {
            if (squares[r][c]) {
                squares[r][c]->setAttackedSquares(*this, {r,c}, true);
            }
        }
    }
    // 3) Find both kings’ coordinates
    auto whiteK = findKingPosition(Colour::WHITE);
    auto blackK = findKingPosition(Colour::BLACK);

    // 4) Update the flags just once
    whiteInCheck = attackedSquares[whiteK.r][whiteK.c].isAttackedByBlack();
    blackInCheck = attackedSquares[blackK.r][blackK.c].isAttackedByWhite();

    if (squares[t.r][t.c]->getType() == PieceType::Rook) {
        if (squares[t.r][t.c]->getColour() == Colour::WHITE) {
            if (t.c == 1) {
                whiteCanCastleQueenSide = false;
            } else if (t.c == SIZE - 1) {
                whiteCanCastleKingSide = false;
            }
        } else if (squares[t.r][t.c]->getColour() == Colour::BLACK) {
            if (t.c == 1) {
                blackCanCastleQueenSide = false;
            } else if (t.c == SIZE - 1) {
                blackCanCastleKingSide = false;
            }
        }
    } else if (squares[t.r][t.c]->getType() == PieceType::King) {
        if (squares[t.r][t.c]->getColour() == Colour::WHITE) {
            whiteCanCastleKingSide = false;
            whiteCanCastleQueenSide = false;
        } else if (squares[t.r][t.c]->getColour() == Colour::BLACK) {
            blackCanCastleKingSide = false;
            blackCanCastleQueenSide = false;
        }
    } else if (squares[t.r][t.c]->getType() == PieceType::Pawn) {
        if (std::abs(f.r - t.r) == 2) {
            // the vulnerable square is the one the pawn jumped over:
            int dir = (squares[t.r][t.c]->getColour() == Colour::WHITE? 1 : -1);
            setEnPassantTarget({ t.r + dir, t.c });
        } else {
            if (t.r == enPassantTarget.r && t.c == enPassantTarget.c) {
                removePiece({f.r, t.c});
            }
            // any other pawn move (including captures) clears the target
            clearEnPassantTarget();
        }
    } else {
        clearEnPassantTarget();
    }
    setLastMove(move);
    notifyObservers();
    return true;
}

bool Board::hasLegalMoves(Colour colour) const {
    for (int i = 1; i < SIZE; ++i) {
        for (int j = 1; j < SIZE; ++j) {
            if (squares[i][j] && squares[i][j]->getColour() == colour) {
                for (int x = 1; x < SIZE; ++x) {
                    for (int y = 1; y < SIZE; ++y) {
                        Board copy = *this;
                        if (copy.movePiece(Move(Coordinate(i, j), Coordinate(x, y)))) {
                            return true;
                        }
                    }
                }
            }
        }
    }
    return false;
}

bool Board::isInCheck(Colour colour) const {
    if (colour == Colour::WHITE) {
        return whiteInCheck;
    } else if (colour == Colour::BLACK) {
        return blackInCheck;
    }
    return false; // Invalid colour
}
bool Board::isCheckmate(Colour colour) const {
    return isInCheck(colour) && !hasLegalMoves(colour);
}
bool Board::isStalemate(Colour colour) const {
    return !isInCheck(colour) && !hasLegalMoves(colour);
}
bool Board::canWhiteCastleQueenSide() const {
    return whiteCanCastleQueenSide;
}
bool Board::canWhiteCastleKingSide() const {
    return whiteCanCastleKingSide;
}
bool Board::canBlackCastleQueenSide() const {
    return blackCanCastleQueenSide;
}
bool Board::canBlackCastleKingSide() const {
    return blackCanCastleKingSide;
}
void Board::toggleWhiteCastleQueenSide() {
    whiteCanCastleQueenSide = false;
}
void Board::toggleWhiteCastleKingSide() {
    whiteCanCastleKingSide = false;
}
void Board::toggleBlackCastleQueenSide() {
    blackCanCastleQueenSide = false;
}
void Board::toggleBlackCastleKingSide() {
    blackCanCastleKingSide = false;
}

void Board::setAttackedSquare(Coordinate c, Colour underAttack, bool attack) {
    if (c.r < 0 || c.r >= SIZE || c.c < 0 || c.c >= SIZE) {
        return; // just making sure out of bounds won't crash
    }
    
    if (underAttack == Colour::WHITE) {
        if (attack) {
            attackedSquares[c.r][c.c].blackAttacked();
        } else {
            attackedSquares[c.r][c.c].removeBlackAttacker();
        }
    } else if (underAttack == Colour::BLACK) {
        if (attack) {
            attackedSquares[c.r][c.c].whiteAttacked();
        } else {
            attackedSquares[c.r][c.c].removeWhiteAttacker();
        }
    } 
}

void Board::placePiece(PieceType piece, Coordinate coord, Colour colour) {
    int r = coord.r, c = coord.c;
    if (piece == PieceType::Pawn) squares[r][c] = make_unique<Pawn>(Pawn(colour));
    if (piece == PieceType::Knight) squares[r][c] = make_unique<Knight>(Knight(colour));
    if (piece == PieceType::Bishop) squares[r][c] = make_unique<Bishop>(Bishop(colour));
    if (piece == PieceType::Rook) squares[r][c] = make_unique<Rook>(Rook(colour));
    if (piece == PieceType::Queen) squares[r][c] = make_unique<Queen>(Queen(colour));
    if (piece == PieceType::King) squares[r][c] = make_unique<King>(King(colour));
    squares[r][c]->setAttackedSquares(*this, coord, true);
    auto whiteK = findKingPosition(Colour::WHITE);
    auto blackK = findKingPosition(Colour::BLACK);

    // 4) Update the flags just once
    whiteInCheck = attackedSquares[whiteK.r][whiteK.c].isAttackedByBlack();
    blackInCheck = attackedSquares[blackK.r][blackK.c].isAttackedByWhite();
}

void Board::removePiece(Coordinate coord) {
    int r = coord.r, c = coord.c;
    squares[r][c]->setAttackedSquares(*this, coord, false);
    squares[r][c] = nullptr;
    clearSquare(coord); 
}

bool Board::hasOneWhiteKing() {
    int count = 0;
    for (int i = 1; i < SIZE; ++i) {
        for (int j = 1; j < SIZE; ++j) {
            if (squares[i][j] && squares[i][j]->getType() == PieceType::King && squares[i][j]->getColour() == Colour::WHITE) {
                count++;
            }
        }
    }
    return count == 1;
}

bool Board::hasOneBlackKing() {
    int count = 0;
    for (int i = 1; i < SIZE; ++i) {
        for (int j = 1; j < SIZE; ++j) {
            if (squares[i][j] && squares[i][j]->getType() == PieceType::King && squares[i][j]->getColour() == Colour::BLACK) {
                count++;
            }
        }
    }
    return count == 1;
}

bool Board::hasPawnOnEdges() {
    for (int j = 1; j < SIZE; ++j) {
        if (squares[1][j] && squares[1][j]->getType() == PieceType::Pawn) return true; 
        if (squares[SIZE - 1][j] && squares[SIZE - 1][j]->getType() == PieceType::Pawn) return true;
    }
    return false;
}

Coordinate Board::findKingPosition(Colour colour) {
    for (int i = 1; i < SIZE; i++) {
        for (int j = 1; j < SIZE; j++) {
            Piece *p = getPiece({i, j});
            if (!p) {
                continue;
            }
            if (p->getType() == PieceType::King && p->getColour() == colour) {
                return {i, j};
            }
        }
    }
    return {-1, -1};
}

void Board::notifyObservers() {
    for (Observer* observer : observers) {
        observer->notify(*this);
    }
}

void Board::setLastMove(const Move& move) {
    for (Observer* observer : observers) {
        observer->setLastMove(move);
    }
}

void Board::init() {
    for (Observer* observer : observers) {
        observer->init(*this);
    }
}

void Board::clearSquare(Coordinate c) {
    for (Observer* observer : observers) {
        observer->clearSquare(c);
    }
}

void Board::specialMove(const Move &move) {
    for (Observer* observer : observers) {
        observer->specialMove(*this, move);
    }
}

void Board::promotion(PieceType type, Coordinate c) {
    for (Observer* observer : observers) {
        observer->promotion(*this, type, c);
    }
}

void Board::clearBoard() {
    for (int i = 1; i < SIZE; ++i) {
        for (int j = 1; j < SIZE; ++j) {
            squares[i][j] = nullptr;
            attackedSquares[i][j] = Attacked(0, 0);
        }
    }
    whiteCanCastleKingSide = true;
    whiteCanCastleQueenSide = true;
    blackCanCastleKingSide = true;
    blackCanCastleQueenSide = true;
    whiteInCheck = false;
    blackInCheck = false;
    enPassantTarget = {-1, -1};
}

void Board::refresh() {
    for (int i = 0; i < SIZE; ++i) {
        for (int j = 0; j < SIZE; ++j) {
            squares[i][j] = nullptr;
            attackedSquares[i][j] = Attacked(0, 0);
        }
    }
    for (int i = 1; i <= 8; ++i) {
        placePiece(PieceType::Pawn, Coordinate(2, i), Colour::BLACK);
        placePiece(PieceType::Pawn, Coordinate(7, i), Colour::WHITE);
    }
    for (int i : {1, 8}) {
        placePiece(PieceType::Rook, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Rook, Coordinate(8, i), Colour::WHITE);
    }
    for (int i : {2, 7}) {
        placePiece(PieceType::Knight, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Knight, Coordinate(8, i), Colour::WHITE);
    }
    for (int i : {3, 6}) {
        placePiece(PieceType::Bishop, Coordinate(1, i), Colour::BLACK);
        placePiece(PieceType::Bishop, Coordinate(8, i), Colour::WHITE);
    }
    placePiece(PieceType::Queen, Coordinate(1, 4), Colour::BLACK);
    placePiece(PieceType::Queen, Coordinate(8, 4), Colour::WHITE);
    placePiece(PieceType::King, Coordinate(1, 5), Colour::BLACK);
    placePiece(PieceType::King, Coordinate(8, 5), Colour::WHITE);

    whiteCanCastleKingSide = true;
    whiteCanCastleQueenSide = true;
    blackCanCastleKingSide = true;
    blackCanCastleQueenSide = true;
}
