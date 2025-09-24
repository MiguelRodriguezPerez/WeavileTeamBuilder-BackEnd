package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.example.demo.dto.MoveDto;

public interface MoveDataInterface {

    MoveData saveMove(MoveData moveData);
    void deleteAllMoves();
    MoveData getMoveByName(String name);
    MoveData requestMoveToPokeApi(int number);
    boolean requestAllMovesToApi();
    MoveDto convertMoveDataToDto(MoveData moveData);

}
