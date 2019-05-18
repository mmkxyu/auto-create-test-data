/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: DataEntity
 * Author:   yml
 * Date:     2019/5/17 15:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 俞美玲          2019.5.7          1.0.01            数据实体
 */
package com.test.csv.tool;

import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @创建人 yumeiling
 * @创建时间 2019/5/17
 * @描述
 */
@Data
public class DataEntity implements Serializable {
    /**
     * 注册用户名
     */
    private String username;

    /**
     * 注册密码
     */
    private String password;

    /**
     * 登录用户名
     */
    private String loginUsername;

    /**
     * 登录密码
     */
    private String loginPassword;



    /**
     * 记住我
     */
    private String remeberme;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 分类
     */
    private String catagory;

    /**
     * 文章标题
     */
    private String title;

    /**

     * 文章自定义路径
     */
    private String slug;
    /**
     * 文章标签
     */
    private String tag;
    /**
     * 文章内容
     */
    private String content;



    /**
     * 分类
     */
    private String comment;



    /**
     * 期望结果
     */
    private String expectResult;

    public DataEntity(DataEntity entity) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(entity,this);
    }

    public DataEntity(){}
}
