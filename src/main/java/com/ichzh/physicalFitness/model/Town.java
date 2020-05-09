package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "town")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Town implements Serializable {

    // 区县id（行政字典码）
    @Id
    @Column(name = "town_id")
    private Integer townId;

    // 自定义字典码
    @Column(name = "town_code")
    private String townCode;

    // 曲线名称
    @Column(name = "town_name")
    private String townName;
}
