package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.ItemData;
import com.example.demo.dto.ItemDto;

public interface ItemDataInterface {

    ItemData saveItemData(ItemData itemData);

    ItemData getItemById(long id);

    void deleteAllItemData();

    ItemData getItemByName(String name);

    ItemData requestItemToPokeApi(int number);

    boolean requestAllItems();

    ItemDto convertItemDataToDto(ItemData itemData);

    Set<ItemDto> getAllItemsAsDto();

}
