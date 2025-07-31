package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.ItemData;
import com.example.demo.domain.dto.ItemDataDto;

public interface ItemData_Interface {

    ItemData saveItemData(ItemData itemData);
    ItemData getItemById(long id);
    void deleteAllItemData();
    ItemData getItemByName(String name);
    ItemData requestItemToPokeApi(int number);
    boolean requestAllItems();
    ItemDataDto convertItemDataToDto(ItemData itemData);
    Set<ItemDataDto> getAllItemsAsDto();

}
