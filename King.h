#include "Piece.h"
#include "Coordinate.h"
#include "Board.h"
#include "Colour.h"
#include <memory>

class King: public Piece {
    bool isValidCastle(const Board& board, const Move& move) const;
public:
    King(Colour colour): Piece(colour) { }
    bool canMove(const Board& board, const Move& move) const override;
    void setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const override;
    int getValue() const override { return 0; }
    PieceType getType() const override { return PieceType::King; }
    std::unique_ptr<Piece> clone() const override;
};
