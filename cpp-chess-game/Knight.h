#include "Piece.h"
#include "Coordinate.h"
#include "Board.h"
#include "Colour.h"
#include <memory>

class Knight: public Piece {
public:
    Knight(Colour colour): Piece(colour) { }
    bool canMove(const Board& board, const Move& move) const override;
    void setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const override;
    int getValue() const override { return 3; };
    PieceType getType() const override { return PieceType::Knight; }
    std::unique_ptr<Piece> clone() const override;
};
