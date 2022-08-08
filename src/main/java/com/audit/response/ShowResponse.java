package com.audit.response;

import java.util.ArrayList;
import java.util.List;

public class ShowResponse {
    private Object data;
    private int currentPage;
    private int size;
    private int maxPage;

    public ShowResponse(){}

    public ShowResponse(Object data, int currentPage, int size, int maxPage) {
        this.data = data;
        this.currentPage = currentPage;
        this.size = size;
        this.maxPage = maxPage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
