package com.audit.dto;

public class ModuleDTO {
    Integer id;
    String name;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ModuleDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
}
