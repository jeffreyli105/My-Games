#include "ComputerPlayer4.h"
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Piece.h"
#include "Colour.h"
#include "Coordinate.h"
#include <vector>
#include <algorithm>
#include <random>

#include <iostream>

using namespace std;

// returns a vector of all legal moves in the position
vector<Move> ComputerPlayer4::getMoves(const Board& board, Colour curPlayer) const {
    vector<Coordinate> squares;
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            squares.push_back(Coordinate(i, j));
        }
    }
    vector<Move> moves;
    for (int i = 0; i < squares.size(); ++i) {
        Piece* piece = board.getPiece(squares[i]);
        if (piece == nullptr) continue;
        if (piece->getColour() != curPlayer) continue;
        for (int j = 0; j < squares.size(); ++j) {
            if (i == j) continue;
            if (!piece->canMove(board, Move(squares[i], squares[j]))) continue;
            moves.push_back(Move(squares[i], squares[j]));
        }
    }
    random_device rd;
    mt19937 gen(rd());
    shuffle(moves.begin(), moves.end(), gen);
    return moves;
}

// returns a move
// if no legal move is possible, returns a move from (0, 0) to (0, 0)
Move ComputerPlayer4::makeMove(const Board& board) const {
    vector<Move> moves = getMoves(board, colour);
    Move move = Move(Coordinate(0, 0), Coordinate(0, 0));
    if (moves.empty()) return move;

    double mx = MINSCORE;
    for (Move i : moves) {
        Board next = board;
        next.movePiece(i);
        double score = -evaluate(next, colour == Colour::WHITE ? Colour::BLACK : Colour::WHITE, DEPTH, MINSCORE, MINSCORE);
        if (mx < score) {
            mx = score;
            move = i;
        }
    }
    Board next = board;
    next.movePiece(move);
    // cout << (char)('a' + move.from.c - 1) << (9 - move.from.r) << " -> " << (char)('a' + move.to.c - 1) << (9 - move.to.r) << "\n";
    // cout << "Evaluation: " << evaluate(next, colour == Colour::WHITE ? Colour::BLACK : Colour::WHITE) << "\n";
    return move;
}

// assigns a board position with an integer
// the more desirable the position, the greater the integer
// searches depth moves into the future
// evalues based on point difference + a bonus for each square controlled
double ComputerPlayer4::evaluate(const Board& board, Colour curPlayer, int depth, double bestWhite, double bestBlack) const {
    // check if the board position is in an end state
    if (curPlayer == Colour::WHITE) {
        if (board.isCheckmate(Colour::WHITE)) return MINSCORE;
        if (board.isCheckmate(Colour::BLACK)) return MAXSCORE;
    }
    if (curPlayer == Colour::BLACK) {
        if (board.isCheckmate(Colour::WHITE)) return MAXSCORE;
        if (board.isCheckmate(Colour::BLACK)) return MINSCORE;
    }
    if (board.isStalemate(curPlayer)) return 0;

    if (--depth == 0) return evaluate(board, curPlayer);
    vector<Move> moves = getMoves(board, curPlayer);
    double score = MINSCORE;
    Move move;
    for (Move i : moves) {
        Board next = board;
        next.movePiece(i);
        if (curPlayer == Colour::WHITE) {
            score = max(score, -evaluate(next, Colour::BLACK, depth, std::max(score, bestWhite), bestBlack));
            if (-score < bestBlack) return MAXSCORE;
        }
        if (curPlayer == Colour::BLACK) {
            score = max(score, -evaluate(next, Colour::WHITE, depth, bestWhite, std::max(score, bestBlack)));
            if (-score < bestWhite) return MAXSCORE;
        }
    }
    return score;
}

double ComputerPlayer4::evaluate(const Board& board, Colour curPlayer) const {
    double score = 0;
    vector<Coordinate> enemyCoords;
    vector<Coordinate> allyCoords;
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            Coordinate c = Coordinate(i, j);
            Piece* piece = board.getPiece(c);
            if (piece == nullptr) continue;
            if (piece->getColour() == curPlayer) {
                if (piece->getType() == PieceType::King) score += MAXSCORE;
                else score += piece->getValue();
                allyCoords.push_back(c);
            }
            else {
                if (piece->getType() == PieceType::King) score -= MAXSCORE;
                else score -= piece->getValue();
                enemyCoords.push_back(c);
            }
        }
    }
    for (Coordinate allyCoord : allyCoords) {
        Piece* ally = board.getPiece(allyCoord);
        for (Coordinate enemyCoord : enemyCoords) {
            Piece* enemy = board.getPiece(enemyCoord);
            if (enemy->canMove(board, Move(enemyCoord, allyCoord))) score -= ally->getValue() * 0.2;
            if (ally->canMove(board, Move(allyCoord, enemyCoord))) score += enemy->getValue() * 0.2;
        }
    }

    vector<Coordinate> squares;
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            squares.push_back(Coordinate(i, j));
        }
    }
    for (Coordinate i : squares) {
        Piece* piece = board.getPiece(i);
        if (piece == nullptr) continue;
        double mult = piece->getColour() == curPlayer ? 1 : -1;
        if (piece->getType() == PieceType::King) score += mult * (0.5 - tilePoint[i.r * 8 + i.c - 9]) * 10;
        else score += mult * tilePoint[i.r * 8 + i.c - 9] * 0.2;
        for (Coordinate j : squares) {
            if (i.r == j.r && i.c == j.c) continue;
            if (!piece->canMove(board, Move(i, j))) score += mult * tilePoint[j.r * 8 + j.c - 9] * 0.2;
        }
    }
    return score;
}
