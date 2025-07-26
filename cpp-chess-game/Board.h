#ifndef BOARD_H
#define BOARD_H

#include <vector>
#include <memory> 
#include "Coordinate.h"
#include "Piece.h"
#include "Colour.h"
#include "Observer.h"
#include "Subject.h"
#include "Move.h"

class Piece;
class Observer;
enum class PieceType;

const int SIZE = 9; // 8x8 board with an extra row and column for easier indexing

class Attacked {
public:
    int white; //how many white pieces is attacking this
    int black; //how many black pieces is attacking this
    Attacked(int white, int black): white{white}, black{black} {}
    Attacked(const Attacked& a): Attacked(a.white, a.black) {}
    Attacked(): white{0}, black{0} { }
    void whiteAttacked() { white++; }
    void blackAttacked() { black++; }
    void removeWhiteAttacker() { white--; }
    void removeBlackAttacker() { black--; }
    bool isAttackedByWhite() const { return white > 0; }
    bool isAttackedByBlack() const { return black > 0; }
};
 
class Board : public Subject {
    std::unique_ptr<Piece> squares[SIZE][SIZE];
    Attacked attackedSquares[SIZE][SIZE];
    bool whiteCanCastleKingSide;
    bool whiteCanCastleQueenSide;
    bool blackCanCastleKingSide;
    bool blackCanCastleQueenSide;
    bool whiteInCheck = false;
    bool blackInCheck = false;
    Coordinate enPassantTarget = {-1, -1};
    
public:

    Board();
    Board(const Board& board);

    Piece* getPiece(Coordinate c) const; 
    bool movePiece(Move move, bool freePass = false);
 
    bool isInCheck(Colour colour) const;
    bool isCheckmate(Colour colour) const;
    bool isStalemate(Colour colour) const;
    bool hasLegalMoves(Colour colour) const;
    bool canWhiteCastleQueenSide() const;
    bool canWhiteCastleKingSide() const;
    bool canBlackCastleQueenSide() const;
    bool canBlackCastleKingSide() const;
    void toggleWhiteCastleQueenSide();
    void toggleWhiteCastleKingSide();
    void toggleBlackCastleQueenSide();
    void toggleBlackCastleKingSide();
    void setAttackedSquare(Coordinate c, Colour underAttack, bool attack);
    void setEnPassantTarget(const Coordinate& coord) { enPassantTarget = coord; }
    void clearEnPassantTarget() { enPassantTarget = {-1, -1}; }
    Attacked getAttackingSquare(Coordinate c) const {return attackedSquares[c.r][c.c]; }
    Coordinate getEnPassantTarget() const { return enPassantTarget; };
    void placePiece(PieceType piece, Coordinate coord, Colour colour);
    void removePiece(Coordinate coord);
    bool hasOneWhiteKing(); 
    bool hasOneBlackKing();
    bool hasPawnOnEdges();
    Coordinate findKingPosition(Colour colour);
    void notifyObservers() override;
    void setLastMove(const Move& move) override; 
    void init() override; 
    void clearSquare(Coordinate c) override;
    void specialMove(const Move &move) override;
    void promotion(PieceType type, Coordinate c) override;
    void clearBoard(); 
    void refresh(); 
};

#endif

