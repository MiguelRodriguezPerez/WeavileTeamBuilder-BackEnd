package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.movements.MoveData;

public interface MoveData_Interface {

    MoveData saveMove(MoveData moveData);
    void deleteAllMoves();
    MoveData getMoveByName(String name);
    MoveData requestMoveToPokeApi(int number);
    boolean requestAllMovesToApi();
    Set<MoveData> getAllMoveData();
}
