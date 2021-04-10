package com.example.moduletest.Data;

import java.io.Serializable;
import java.util.List;

public class PhotoList implements Serializable {

    private List<String> list;
    private int position;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
