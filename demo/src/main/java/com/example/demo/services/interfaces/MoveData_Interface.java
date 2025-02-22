package com.example.demo.services.interfaces;

import com.example.demo.domain.movements.MoveData;

public interface MoveData_Interface {
    
    MoveData saveMove(MoveData moveData);
    void deleteAllMoves();
    MoveData findMoveByName(String name);
    MoveData requestMoveToPokeApi(int number);
    boolean requestAllMoves();
    
}
