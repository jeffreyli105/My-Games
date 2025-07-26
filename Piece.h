#ifndef PIECE_H
#define PIECE_H

#include "Coordinate.h"
#include "Move.h"
#include "Board.h"
#include "Colour.h"
#include <memory>

class Board;

enum class PieceType { King, Queen, Rook, Bishop, Knight, Pawn };
class Piece {
public:
  Piece(Colour c):
  colour {c} {}
  virtual ~Piece() = default;
  virtual PieceType getType() const = 0;
  virtual bool canMove(const Board& board, const Move& move) const = 0;
  Colour getColour() const { return colour; }
  virtual void setAttackedSquares(Board& board, const Coordinate& from, bool toAdd) const = 0;
  virtual int getValue() const = 0;
  virtual std::unique_ptr<Piece> clone() const = 0;

protected:
  const Colour colour;
  bool hasMoved = false;
};

#endif
