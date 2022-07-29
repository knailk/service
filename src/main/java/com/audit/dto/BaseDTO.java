package com.audit.dto;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
@MappedSuperclass
public class BaseDTO {
    private Integer id;
}
