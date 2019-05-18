/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: ExecutionMain
 * Author:   yml
 * Date:     2019/5/17 17:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 俞美玲          2019.5.17          1.0.0              工具执行类
 */
package com.test.csv.tool;

import java.util.List;

/**
 * @创建人 yumeiling
 * @创建时间 2019/5/17
 * @描述
 */
public class ExecutionMain {
    public static void main(String[] args) throws Exception{
        //造测试数据
        List<DataEntity> data = CreateDataModel.createUserData();
        String csvFilePath = "E://data.csv";
        //表头名称
        //注册用户名、注册密码、登录用户名、登录密码、记住我、邮箱、分类名称、文章标题、文章路径、文章标签、文章内容、评论文章、期望结果
        String[] csvHeaders = { "username", "password", "loginusername","loginpassword","remeber","email","cname","title","slug","tags","content","comment","expectresult" };
        CreateDataModel.writeCSV(data,csvFilePath,csvHeaders);

    }
}
