#include "TextDisplay.h"

using namespace std;

const int WHITESQUARE = 1;
const int BLACKSQUARE = -1; 
const char BLANKWHITE = ' ';
const char BLANKBLACK = '_';

TextDisplay::TextDisplay(Board &board) {
    int squareColour = 1;
    int rowStartingColour = 1; 

    for (int i = 0; i < SIZE; i++) {
        squareColour = rowStartingColour; 
        for (int j = 0; j < SIZE; j++) {
            if (squareColour == WHITESQUARE) {
                display[i][j] = BLANKWHITE; 
            } else {
                display[i][j] = BLANKBLACK; 
            }
            squareColour *= -1; 
        }
        rowStartingColour *= -1; 
    }
}

void TextDisplay::notify(Board &board){
    int colourBuffer; 
    int squareColour = 1;
    int rowStartingColour = 1; 
    for (int i = 1; i < SIZE; i++) {
        squareColour = rowStartingColour;
        cout << (SIZE - i) << " ";
        for (int j = 1; j < SIZE; j++) {
            if (board.getPiece({i, j})) { // the square is not empty
                if (board.getPiece({i, j})->getColour() == Colour::WHITE) {
                    colourBuffer = WHITEBUFFER; 
                } else {
                    colourBuffer = BLACKBUFFER; 
                }

                PieceType type = board.getPiece({i, j})->getType(); 
                if (type == PieceType::King) {
                    display[i][j] = KING + colourBuffer; 
                } else if (type == PieceType::Queen) {
                    display[i][j] = QUEEN + colourBuffer;
                } else if (type == PieceType::Bishop) {
                    display[i][j] = BISHOP + colourBuffer; 
                } else if (type == PieceType::Rook) {
                    display[i][j] = ROOK + colourBuffer; 
                } else if (type == PieceType::Knight) {
                    display[i][j] = KNIGHT + colourBuffer; 
                } else { //Pawn
                    display[i][j] = PAWN + colourBuffer; 
                }
            }
            else {
                if (squareColour == WHITESQUARE) {
                    display[i][j] = BLANKWHITE; 
                } else {
                    display[i][j] = BLANKBLACK; 
                }
            }
            squareColour *= -1; 
            cout << display[i][j]; 
        }
        rowStartingColour *= -1; 
        cout << endl; 
    }
    cout << "  ";
    for (int i = 1; i < SIZE; ++i) {
        cout << (char)('a' + i - 1);
    } cout << "\n";
}
