package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.team.NatureData;
import com.example.demo.dto.NatureDto;

public interface NatureDataInterface {

    NatureData getNatureDataById(long id);
    void deleteAllNatureData();
    NatureData requestNatureToPokeApi(int number);
    boolean requestAllNatures();
    Set<NatureData> getAllNatures();
    Set<NatureDto> getAllNaturesAsDto();
    NatureData getNatureByName(String name);
    NatureDto convertNatureDataToDto(NatureData data);

}
