#ifndef SUBJECT_H
#define SUBJECT_H

#include "Observer.h"
#include <vector>
#include "Piece.h"

using namespace std;

class Observer;
enum class PieceType;

class Subject {
protected: 
    vector<Observer*> observers;
public:
    virtual void attach(Observer* observer) { observers.push_back(observer); }
    virtual void notifyObservers() = 0; 
    virtual void setLastMove(const Move& move) = 0;
    virtual void init() = 0; 
    virtual void clearSquare(Coordinate c) = 0;
    virtual void specialMove(const Move &move) = 0;
    virtual void promotion(PieceType type, Coordinate c) = 0; 
    virtual ~Subject() {}
}; 



#endif
