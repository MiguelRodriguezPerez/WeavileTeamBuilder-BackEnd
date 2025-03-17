package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.team.NatureData;

public interface NatureData_Interface {

    NatureData saveNatureData(NatureData natureData);
    NatureData getNatureDataById(long id);
    void deleteAllNatureData();
    NatureData requestNatureToPokeApi(int number);
    boolean requestAllNatures();
    Set<NatureData> getAllNatures();

}
