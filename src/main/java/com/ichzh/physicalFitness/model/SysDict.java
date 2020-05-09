package com.ichzh.physicalFitness.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sys_dict")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysDict implements Serializable {

    @Id @GeneratedValue
    @Column(name = "dict_id")
    private Integer dictId;

    @Column(name = "dict_type")
    private Integer dictType;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "dict_name")
    private String dictName;

    @Column(name = "dict_code")
    private String dictCode;
}
