package com.yfh.boot.pojo;

import com.alibaba.druid.filter.AutoLoad;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("用户实体")
@TableName("t_user")
public class User {
    @ApiModelProperty("用户id")
    private Integer id;
    @ApiModelProperty("用户名")
    private String name;
}
