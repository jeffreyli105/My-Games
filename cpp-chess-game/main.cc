#include "Board.h"
#include "Piece.h"
#include "Knight.h"
#include "Bishop.h"
#include "King.h"
#include "Queen.h"
#include "Pawn.h"
#include "Player.h"
#include "HumanPlayer.h"
#include "ComputerPlayer1.h"
#include "ComputerPlayer2.h"
#include "ComputerPlayer3.h"
#include "ComputerPlayer4.h"
#include "Game.h"
#include "TextDisplay.h"
#include "GraphicDisplay.h"
#include "Colour.h"
#include <iostream>
#include <string>
#include <memory>
using namespace std;

void initialize_player(unique_ptr<Player> &p, const string& player, Colour c) {
    if (player == "human") {
        p = make_unique<HumanPlayer>(c);
    } else if (player == "computer1") {
        p = make_unique<ComputerPlayer1>(c);
    } else if (player == "computer2") {
        p = make_unique<ComputerPlayer2>(c);
    } else if (player == "computer3") {
        p = make_unique<ComputerPlayer3>(c);
    } else if (player == "computer4") {
        p = make_unique<ComputerPlayer4>(c);
    }
}

Coordinate parse_coord(const string& coord) {
    int file = coord[0] - 'a' + 1;
    int rank = SIZE - (coord[1] - '0');
    return {rank, file};
}

PieceType parse_piece(const string& piece) {
    if (piece == "King") {
        return PieceType::King;
    } else if (piece == "Queen") {
        return PieceType::Queen;
    } else if (piece == "Rook") {
        return PieceType::Rook;
    } else if (piece == "Knight") {
        return PieceType::Knight;
    } else if (piece == "Bishop") {
        return PieceType::Bishop;
    }
    return PieceType::Pawn;
}

bool isValidPlayer(const string& player) {
    if (player != "human" && player != "computer1" && player != "computer2" &&
        player != "computer3" && player != "computer4") {
        return false;
    }
    return true;
}

bool isValidCoord(const string& coord) {
    if (coord.length() != 2 || coord[0] < 'a' || coord[0] > 'h' ||
        coord[1] < '1' || coord[1] > '8') {
        return false;
    }
    return true;
}

bool isValidPiece(const string& piece) {
    if (piece != "King" && piece != "Queen" && piece != "Rook" &&
        piece != "Knight" && piece != "Bishop" && piece != "Pawn") {
        return false;
    }
    return true;
}

bool isValidPromotionPiece(const string& piece) {
    if (piece == "Queen" || piece == "Rook" || piece == "Knight" ||
        piece == "Bishop") {
        return true;
    }
    return false;
}

int main() {
    Game game;
    string command;
    Board b;
    unique_ptr<Observer> td = make_unique<TextDisplay>(b);
    unique_ptr<Observer> gd = make_unique<GraphicDisplay>(b);
    b.attach(td.get());
    b.attach(gd.get());
    unique_ptr<Player> p1;
    unique_ptr<Player> p2;
    Colour turn = Colour::WHITE;
    bool game_started = false;
    double whiteScore = 0;
    double blackScore = 0;

    while (cin >> command) {
        if (command == "done") {
            cout << "Final Score:" << endl;
            cout << "White: " << whiteScore << endl;
            cout << "Black: " << blackScore << endl;
            break;
        }
        if (command == "game") {
            string white_player;
            string black_player;
            turn = Colour::WHITE;
            cin >> white_player >> black_player;
            bool white_valid = isValidPlayer(white_player);
            bool black_valid = isValidPlayer(black_player);
            if (!white_valid) {
                cerr << "Invalid player" << endl;
            }
            if (!black_valid) {
                cerr << "Invalid player" << endl;
            }
            if (!white_valid || !black_valid) {
                continue;
            }
            initialize_player(p1, white_player, Colour::WHITE);
            initialize_player(p2, black_player, Colour::BLACK);
            b.refresh(); 
            game.start(b);
            game_started = true;
        } else if (command == "resign") {
            game.resign(turn);
            game_started = false;
            if (turn == Colour::WHITE) {
                blackScore++;
            } else {
                whiteScore++;
            }
            continue;
        } else if (command == "move") {
            Move move;
            if (turn == Colour::WHITE) move = p1->makeMove(b);
            if (turn == Colour::BLACK) move = p2->makeMove(b);
            // cout << "(" << move.from.r << " " << move.from.c << ") (" << move.to.r << " " << move.to.c << ")" << endl;
            if (game.move(b, move)) {
                // cout << "valid move" << endl;
                if (b.hasPawnOnEdges()) {
                    // Promotion
                    cout << "Input piece to promote pawn to:" << endl;
                    string promote;
                    while (cin >> promote && !isValidPromotionPiece(promote)) {
                        cout << "Not a valid promotion piece" << endl;
                    }
                    b.placePiece(parse_piece(promote), move.to, turn);
                    b.notifyObservers();
                    b.promotion(parse_piece(promote), move.to);
                }
                if (game.hasWhiteWon(b)) {
                    cout << "White wins!" << endl;
                    whiteScore++;
                    game = Game();
                    continue;
                } else if (game.hasBlackWon(b)) {
                    cout << "Black wins!" << endl;
                    blackScore++;
                    game = Game();
                    continue;
                } else if (game.hasTied(b)) {
                    cout << "Game tied!" << endl;
                    whiteScore += 0.5;
                    blackScore += 0.5;
                    game = Game();
                    continue;
                }
                if (b.isInCheck(Colour::WHITE)) {
                    cout << "White is in check!" << endl;
                } else if (b.isInCheck(Colour::BLACK)) {
                    cout << "Black is in check!" << endl;
                }
                turn = (turn == Colour::WHITE? Colour::BLACK : Colour::WHITE);
            } else {
                cout << "invalid move" << endl;
                continue;
            }
        } else if (command == "setup") {
            if (game_started) {
                cout << "Game has already started" << endl;
                continue;
            }
            b.clearBoard();
            bool in_setup = true;
            while (in_setup) {
                string op;
                string arg1;
                string arg2;
                cin >> op;
                if (op == "+") {
                    cin >> arg1 >> arg2;
                    bool valid1 = isValidPiece(arg1);
                    bool valid2 = isValidCoord(arg2);
                    if (!valid1) {
                        cerr << "Piece is not valid" << endl;
                    }
                    if (!valid2) {
                        cerr << "Coordinate is not valid" << endl;
                    }
                    if (!valid1 || !valid2) {
                        continue;
                    }
                    cout << parse_coord(arg2).r << " " << parse_coord(arg2).c << endl;
                    b.placePiece(parse_piece(arg1), parse_coord(arg2), turn);
                    b.notifyObservers();
                } else if (op == "-") {
                    cin >> arg1;
                    if (!isValidCoord(arg1)) {
                        cerr << "Coordinate is not valid" << endl;
                        continue;
                    }
                    b.removePiece(parse_coord(arg1));
                } else if (op == "=") {
                    cin >> arg1;
                    if (arg1 == "White") {
                        turn = Colour::WHITE;
                    } else if (arg1 == "Black") {
                        turn = Colour::BLACK;
                    } else {
                        cerr << "Invalid colour" << endl;
                        continue;
                    }
                } else if (op == "done") {
                    if (!b.hasOneWhiteKing()) {
                        cerr << "Board must have exactly 1 white king" << endl;
                        continue;
                    }
                    if (!b.hasOneBlackKing()) {
                        cerr << "Board must have exactly 1 black king" << endl;
                        continue;
                    }
                    if (b.hasPawnOnEdges()) {
                        cerr << "Board can't have pawns on first or last rows" << endl;
                        continue;
                    }
                    if (b.isInCheck(Colour::WHITE)) {
                        cerr << "White king must not be in check" << endl;
                        continue;
                    }
                    if (b.isInCheck(Colour::BLACK)) {
                        cerr << "Black king must not be in check" << endl;
                        continue;
                    }
                    in_setup = false;
                    b.notifyObservers(); 
                } else {
                    cerr << "Invalid setup command" << endl;
                    continue;
                }
            }
        } else {
            cerr << "Unknown command" << endl;
        }
    }
}
