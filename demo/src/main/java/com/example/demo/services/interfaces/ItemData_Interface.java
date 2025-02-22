package com.example.demo.services.interfaces;

import com.example.demo.domain.ItemData;

public interface ItemData_Interface {

    ItemData saveItemData(ItemData itemData);
    void deleteAllItemData();
    ItemData findItemByName(String name);
    ItemData requestItemToPokeApi(int number);
    boolean requestAllItems();
    
}
