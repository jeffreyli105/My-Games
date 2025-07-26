#include "ComputerPlayer3.h"
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Piece.h"
#include "Coordinate.h"
#include "Colour.h"
#include <vector>
#include <algorithm>
#include <random>

using namespace std;

// returns a vector of all legal moves in the position
vector<Move> ComputerPlayer3::getMoves(const Board& board) const {
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
        if (piece->getColour() != colour) continue;
        for (int j = 0; j < squares.size(); ++j) {
            if (i == j) continue;
            Board copy = board;
            if (!copy.movePiece(Move(squares[i], squares[j]))) continue;
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
Move ComputerPlayer3::makeMove(const Board& board) const {
    vector<Move> moves = getMoves(board);
    Move move = Move(Coordinate(0, 0), Coordinate(0, 0));
    if (moves.empty()) return move;

    int mx = -99999;
    for (Move i : moves) {
        Board next = board;
        next.movePiece(i);
        int score = evaluate(next);
        if (mx < score) {
            mx = score;
            move = i;
        }
    }
    return move;
}

// assigns a board position with an integer
// the more desirable the position, the greater the integer
// same as computer player 2 but
// subtract the value of pieces that are under attack
int ComputerPlayer3::evaluate(const Board& board) const {
    int score = 0;
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            Coordinate c = Coordinate(i, j);
            Piece* piece = board.getPiece(c);
            if (piece == nullptr) continue;
            if (piece->getColour() == colour) score += piece->getValue();
            else score -= piece->getValue();
        }
    }

    vector<Piece*> enemyPieces;
    vector<Coordinate> coords;
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            if (board.getPiece(Coordinate(i, j)) == nullptr) continue;
            if (board.getPiece(Coordinate(i, j))->getColour() == colour) continue;
            enemyPieces.push_back(board.getPiece(Coordinate(i, j)));
            coords.push_back(Coordinate(i, j));
        }
    }
    for (int i = 1; i <= 8; ++i) {
        for (int j = 1; j <= 8; ++j) {
            Coordinate c = Coordinate(i, j);
            Piece* piece = board.getPiece(c);
            if (piece == nullptr) continue;
            if (piece->getColour() != colour) continue;
            for (int k = 0; k < enemyPieces.size(); ++k) {
                Piece* p = enemyPieces[k];
                Coordinate from = coords[k];
                if (p->canMove(board, Move(from, c))) {
                    score -= piece->getValue();
                    break;
                }
            }
        }
    }
    return score;
}
