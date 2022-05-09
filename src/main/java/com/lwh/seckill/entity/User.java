package com.lwh.seckill.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lwh
 * @since 2021-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号做主键id
     */
      @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private Long id;
    private long phone;
    private String nickname;
    private String password;
    private String slat;

    /**
     * 头像
     */
    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;


}
