#include "ComputerPlayer1.h"
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Piece.h"
#include "Coordinate.h"
#include "Colour.h"
#include <vector>
#include <random>

#include <iostream>

using namespace std;

// returns a random legal move
// if no legal move is possible, returns a move from (0, 0) to (0, 0)
Move ComputerPlayer1::makeMove(const Board& board) const {
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
    if (!moves.empty()) {
        srand((unsigned)time(NULL));
        return moves[rand() % moves.size()];
    }
    return Move();
}
