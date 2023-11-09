package com.sparta.springresttemplateclient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class ItemDto {
    private String title;
    private int price;

    public ItemDto(JSONObject item) {
        this.title = item.getString("title");
        this.price = item.getInt("price");
    }
}