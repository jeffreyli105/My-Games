#include "Board.h"
#include "Colour.h"
#include "Move.h"
#include <iostream>
#include "Game.h"
#include "GraphicDisplay.h"
using namespace std;

Game::Game() { }

void Game::start(Board& board) {
    board.init(); 
}

bool Game::move(Board& b, const Move& move) {
    return b.movePiece(move);
}

void Game::resign(Colour c) {
    if (c == Colour::WHITE) {
        cout << "Black Wins!" << endl;
    } else {
        cout << "White Wins!" << endl;
    }
}

bool Game::hasWhiteWon(const Board& b) {
    return b.isCheckmate(Colour::BLACK);
}

bool Game::hasBlackWon(const Board& b) {
    return b.isCheckmate(Colour::WHITE);
}

bool Game::hasTied(const Board& b) {
    return b.isStalemate(Colour::WHITE) && b.isStalemate(Colour::BLACK);
}
