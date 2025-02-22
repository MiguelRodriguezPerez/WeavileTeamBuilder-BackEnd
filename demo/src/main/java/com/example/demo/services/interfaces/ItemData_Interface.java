package com.example.demo.services.interfaces;

import com.example.demo.domain.ItemData;

public interface ItemData_Interface {

    ItemData saveItemData(ItemData itemData);
    ItemData getItemById(long id);
    void deleteAllItemData();
    ItemData findItemByName(String name);
    ItemData requestItemToPokeApi(int number);
    boolean requestAllItems();
    
}
