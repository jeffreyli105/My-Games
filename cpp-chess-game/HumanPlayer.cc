#include "HumanPlayer.h"
#include "Move.h"
#include "Board.h"
#include "Player.h"
#include "Piece.h"
#include "Coordinate.h"
#include <vector>
#include <algorithm>
#include <iostream>
#include <string>

using namespace std;

Coordinate HumanPlayer::parse_coord(const string& coord) const {
    int file = coord[0] - 'a' + 1;
    int rank = SIZE - (coord[1] - '0');
    return {rank, file};
}

bool HumanPlayer::isValidCoord(const string& coord) const {
    if (coord.length() != 2 || coord[0] < 'a' || coord[0] > 'h' ||
        coord[1] < '1' || coord[1] > '8') {
        return false;
    }
    return true;
}

Move HumanPlayer::makeMove(const Board& board) const {
    string coord1;
    string coord2;
    while (true) {
        cin >> coord1 >> coord2;
        bool valid1 = isValidCoord(coord1);
        bool valid2 = isValidCoord(coord2);
        if (!valid1) {
            cerr << "First coordinate is invalid" << endl;
        }
        if (!valid2) {
            cerr << "Second coordinate is invalid" << endl;
        }
        if (!valid1 || !valid2) {
            continue;
        }
        if (board.getPiece(parse_coord(coord1))->getColour() != colour) {
            cerr << "You cannot move your opponent's piece" << endl;
            continue;
        }
        break;
    }

    return Move(parse_coord(coord1), parse_coord(coord2));
}
