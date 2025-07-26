#include "ComputerPlayer2.h"
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
vector<Move> ComputerPlayer2::getMoves(const Board& board) const {
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
Move ComputerPlayer2::makeMove(const Board& board) const {
    vector<Move> moves = getMoves(board);
    Move move;
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
// for this computer player,
// the score is equal to the material advantage
// +1 if the opponent is in check
// e.g. if the player is up a bishop (3 pts) and a pawn (1 pt) and the opponent is in check, it returns 3 + 1 + 1 = 5
int ComputerPlayer2::evaluate(const Board& board) const {
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
    return score;
}
