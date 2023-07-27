package com.example.affablebeanui.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data
@NoArgsConstructor
public class Categories {

    private List<Category> categories = new ArrayList<>();

    public Categories(Spliterator<Category> spliterator){
        categories = StreamSupport.stream(spliterator,true).collect(Collectors.toList());
    }

}
