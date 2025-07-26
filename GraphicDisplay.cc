#include "GraphicDisplay.h"
#include "Board.h"
#include "window.h"
#include "TextDisplay.h"

GraphicDisplay::GraphicDisplay(Board &board) : window(CELL_SIZE * 8, CELL_SIZE * 8) {}

void GraphicDisplay::init(Board &board) {
    int colour; 
    for (int r = 1; r < SIZE; r++) {
        for (int c = 1; c < SIZE; c++) {
            if ((r + c) % 2 == 0) {
                colour = 0; // White
            } else {
                colour = 3; // Green
            }

            window.fillRectangle((c - 1) * CELL_SIZE, (r - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE, colour);
            Piece* p = board.getPiece({r, c});
            if (p) {
                std::string symbol = pieceToString(p); 
                window.drawString((c - 1) * CELL_SIZE + CELL_SIZE / 2, (r - 1) * CELL_SIZE + CELL_SIZE / 2, symbol);
            }
        
        }
    }
}

void GraphicDisplay::notify(Board &board) {
    int fr = lastMove.from.r;
    int fc = lastMove.from.c;
    int tr = lastMove.to.r;
    int tc = lastMove.to.c;
    // Cover the squares
    clearSquare(lastMove.from);
    clearSquare(lastMove.to);
    // displaying piece at new location
    Piece *p = board.getPiece(lastMove.to); 
    if (p) {
        std::string symbol = pieceToString(p); 
        window.drawString((tc - 1) * CELL_SIZE + CELL_SIZE / 2, (tr - 1) * CELL_SIZE + CELL_SIZE / 2, symbol);
    }
}


std::string GraphicDisplay::pieceToString(Piece* p) {
    int colourBuffer; 
    if (p->getColour() == Colour::WHITE) {
        colourBuffer = WHITEBUFFER; 
    } else { // Black
        colourBuffer = BLACKBUFFER;
    }

    char symbol; 
    if (p->getType() == PieceType::King) {
        symbol = KING + colourBuffer; 
    } else if (p->getType() == PieceType::Queen) {
        symbol = QUEEN + colourBuffer;
    } else if (p->getType() == PieceType::Bishop) {
        symbol = BISHOP + colourBuffer;
    } else if (p->getType() == PieceType::Rook) {
        symbol = ROOK + colourBuffer;
    } else if (p->getType() == PieceType::Knight) {
        symbol = KNIGHT + colourBuffer;
    } else { // Pawn
        symbol = PAWN + colourBuffer;
    }

    return std::string(1, symbol);
} 

void GraphicDisplay::clearSquare(Coordinate c) {
    int row = c.r;
    int col = c.c;
    if ((row + col) % 2 == 0) {
        window.fillRectangle((col - 1) * CELL_SIZE, (row - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE, 0); // Fill with white
    } else {
        window.fillRectangle((col - 1) * CELL_SIZE, (row - 1) * CELL_SIZE, CELL_SIZE, CELL_SIZE, 3); // Fill with green
    }
}

void GraphicDisplay::specialMove(Board &board, const Move &move) {
    clearSquare(move.from);
    Piece *p = board.getPiece(move.to); 
    int tc = move.to.c;
    int tr = move.to.r;
    if (p) {
        std::string symbol = pieceToString(p); 
        window.drawString((tc - 1) * CELL_SIZE + CELL_SIZE / 2, (tr - 1) * CELL_SIZE + CELL_SIZE / 2, symbol);
    }
}

void GraphicDisplay::promotion(Board &board, PieceType type, Coordinate c) {
    clearSquare(c);
    int row = c.r;
    int col = c.c;
    char symbol; 
    Colour colour = board.getPiece(c)->getColour();
    int colourBuffer = (colour == Colour::WHITE) ? WHITEBUFFER : BLACKBUFFER;
    if (type == PieceType::Queen) {
        symbol = QUEEN + colourBuffer;
    } else if (type == PieceType::Bishop) {
        symbol = BISHOP + colourBuffer;
    } else if (type == PieceType::Rook) {
        symbol = ROOK + colourBuffer;
    } else { // knight
        symbol = KNIGHT + colourBuffer;
    }

    window.drawString((col - 1) * CELL_SIZE + CELL_SIZE / 2, (row - 1) * CELL_SIZE + CELL_SIZE / 2, std::string(1, symbol));
}

