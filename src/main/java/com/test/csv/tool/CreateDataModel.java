/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CreateDataModel
 * Author:   yml
 * Date:     2019/5/17 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 俞美玲           2019.5.17           1.0.0              自动化生成测试数据
 */
package com.test.csv.tool;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @创建人 yumeiling
 * @创建时间 2019/5/17
 * @描述
 */
public class CreateDataModel {


    enum randomData{equal, more, less}
    /**
     * 生成区间内，区间外随机字母和数字数据
     * @param mixLength
     * @param maxLength
     * @return
     */
    //生成mixLength,maxLength闭闭区间的随机数字和字母
    //如mixLength为6，maxLength为14则rd为equal时生成6到14位的随机数字和字母，
    // rd为more时生成超过14位的随机数字和字母，rd为less时生成小于6位的随机数字和字母
    public static String getStringRandom(int mixLength,int maxLength,randomData rd) {
        int length;
        String val = "";
        switch (rd){
            case equal:
                length = (int) (Math.random() * (maxLength - mixLength + 1) + mixLength);
                break;
            case less:
                length = (int) (Math.random() * (mixLength-1) + 1);
                break;
            case more:
                length = (int) (Math.random() + (maxLength+1));
                break;
            default:
                length = 0;
                val =  "flag错误";
                break;
        }
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 从数组中随机选取一个值
     * @param data
     * @return
     */
    public static String getStringFromArray(String[] data){
//        String[] data = {"on", "off"};
        int randomData = (int)( Math.random () * data.length );
        return data[randomData];
    }

    /**
     * 随机生成一个邮箱数据
     * @return
     */
    public static String getEmailRandom(){
        String[] email_suffix={"@gmail.com","@yahoo.com","@msn.com","@hotmail.com","@qq.com","@163.com","@163.net","@yeah.net","@googlemail.com","@126.com","@sina.com","@sohu.com","@yahoo.com.cn"};
        StringBuilder sb = new StringBuilder();
        sb.append(getStringRandom(6,18,randomData.equal))
        .append(getStringFromArray(email_suffix));
        return sb.toString();
    }

    /**
     * 随机生成汉字
     */
    public static String getChinese(int chineseLength) throws Exception {
        String chineseResult="";
        for(int i=0;i<chineseLength;i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39)));//获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93)));//获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            str = new String(b, "GBk");//转成中文
            chineseResult+=str;
        }
        return chineseResult;
    }

    /**
     * 创建数据公共方法
     */
    public static List<DataEntity> dataCreate(List<DataEntity> autoData,String ...params){
        DataEntity dataEntity  = new DataEntity();
        dataEntity.setUsername(params[0]);
        dataEntity.setPassword(params[1]);
        dataEntity.setEmail(params[2]);
        dataEntity.setRemeberme(params[3]);
        dataEntity.setLoginUsername(params[4]);
        dataEntity.setLoginPassword(params[5]);
        dataEntity.setCatagory(params[6]);
        dataEntity.setTitle(params[7]);
        dataEntity.setSlug(params[8]);
        dataEntity.setTag(params[9]);
        dataEntity.setContent(params[10]);
        dataEntity.setComment(params[11]);
        dataEntity.setExpectResult(params[12]);
        autoData.add(dataEntity);
        return autoData;
    }


    /**
     * 写csv方法
     */
    public static <T> void writeCSV(Collection<T> dataset, String csvFilePath, String[] csvHeaders) {

        try {
            //集合长度，和循环次数，当循环到最后一条记录时不在末尾插入换行符
            int datasetLength = dataset.size();
            int loop=1;
            // 定义路径，分隔符，编码d，第一个表头名称
            //如果是写入bom则bom后面不追加逗号，即在bom后面和第一个表头前面，即两者之间不追加逗号,sheetFirstName为第一个表头的名字根据需求传入
            CsvWriterExtend csvWriter = new CsvWriterExtend(csvFilePath, ',', Charset.forName("UTF-8"),"username"); // 写表头
            //如果是写入bom解决文件乱码，则不在bom后面追加,号分隔符
            csvWriter.write("\ufeff");
            csvWriter.writeRecord(csvHeaders); // 写内容
            // 遍历集合
            Iterator<T> it = dataset.iterator();
            while (it.hasNext()) {
                T t = (T) it.next();
                //获取类属性
                Field[] fields = t.getClass().getDeclaredFields();
                String[] csvContent=new String[fields.length];
                for (short i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    try {
                        Class tCls = t.getClass();
                        Method getMethod = tCls.getMethod(getMethodName,new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        if (value == null) {
                            continue;
                        }
                        //取值并赋给数组
                        String textvalue=value.toString();
                        csvContent[i]=textvalue;
                    }catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                if(loop!=datasetLength){
                    //迭代插入记录
                    csvWriter.writeRecord(csvContent);
                }else{
                    //插入最后一条记录
                    csvWriter.writeLastRecord(csvContent);
                }

                loop=loop+1;
                for(String csvs:csvContent) {
                    System.out.println("记录数据：" + csvs);
                }
            } csvWriter.close();
            System.out.println("<--------CSV文件写入成功-------->");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建测试数据
     * @return
     */
    public static List<DataEntity> createUserData() throws Exception{
        List<DataEntity> autoData =new ArrayList<DataEntity>();
        //正常数据
        String usernameEqual = getStringRandom(1,15,randomData.equal);
        String passwordEqual = getStringRandom(6,14,randomData.equal);
        String emailEqual = getEmailRandom();
        String[] data = {"on", "off"};
        String catagoryEqual = getStringRandom(1,15,randomData.equal);
        String titleEqual = "测试文章"+getChinese(10);
        String slugEqual = getStringRandom(5,10,randomData.equal);
        String tagEqual = getStringRandom(1,10,randomData.equal);
        String contentEqual = "测试文章内容"+getChinese(100);


        //异常数据
        String usernameMore = getStringRandom(1,15,randomData.more);
        String usernameFormat = getStringRandom(1,14,randomData.equal);
        String passwordMore = getStringRandom(6,14,randomData.more);
        String passwordLess = getStringRandom(6,14,randomData.less);
        String catagoryMore = getStringRandom(1,15,randomData.more);
        String slugMore = getStringRandom(5,10,randomData.more);
        String slugLess = getStringRandom(5,10,randomData.less);
        String slugFormat = "."+getStringRandom(5,9,randomData.equal);
        String tagMore = getStringRandom(1,10,randomData.more);

        //异常数据数组
        String[] usernameFails = {usernameMore,usernameFormat+"_"," "};
        String[] passwordFails = {passwordMore,passwordLess," "};
        String[] emailFails = {passwordEqual," "};
        String[] loginUserFails = {usernameEqual+"1",""};
        String[] loginPassFails = {passwordEqual+"1",""};
        String[] catagoryFails = {"",catagoryMore};
        String[] slugFails = {slugLess,slugMore,slugFormat};

        //异常判断输出--注册
        String[] expectRegisterUser = {"用户名不能超过15位，由数字字母组成！","用户名不能超过15位，由数字字母组成！","用户名不能为空！"};
        String[] expectRegisterPass = {"请输入6-14位密码！","请输入6-14位密码！","密码不能为空！"};
        String[] expectRegisterEmail = {"请输入正确的邮箱格式","邮箱不能为空！"};
        //异常判断输出--登录
        String[] expectLoginUser = {"不存在该用户","用户名和密码不能为空"};
//        String[] expectLoginPass = {"用户名或密码错误！","用户名和密码不能为空"};
        String[] expectCatagory = {"分类名称不能为空！","请输入不超过15位分类名称！"};
        String[] expectArticle = {"文章标题不能为空","文章内容不能为空","文章标签过长"};
        String[] expectSlug = {"自定义路径限制5到10位","自定义路径限制5到10位","您输入的路径不合法"};
        String[] expectComment = {"请输入评论内容"};

        //用户名异常数据
        for(int i=0;i<usernameFails.length;i++){
            autoData = dataCreate(autoData,usernameFails[i],passwordEqual,emailEqual,"","","","","","","","","",expectRegisterUser[i]);
        }
        //密码异常数据
        for(int j=0;j<passwordFails.length;j++){
            autoData = dataCreate(autoData,usernameEqual,passwordFails[j],emailEqual,"","","","","","","","","",expectRegisterPass[j]);
        }
        //邮箱异常数据
        for(int k=0;k<emailFails.length;k++){
            autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailFails[k],"","","","","","","","","",expectRegisterEmail[k]);
        }
        //登录用户异常数据
        for(int i=0;i<loginUserFails.length;i++){
            autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),loginUserFails[i],passwordEqual,"","","","","","",expectLoginUser[i]);
        }
//        for(int i=0;i<loginPassFails.length;i++){
//            autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,loginPassFails[i],"","","","","","",expectLoginPass[i]);
//        }
        //分类异常数据
        for(int i=0;i<catagoryFails.length;i++){
            autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,passwordEqual,catagoryFails[i],"","","","","",expectCatagory[i]);
        }
        //文章标题为空
        autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,passwordEqual,catagoryEqual," ",slugEqual,tagEqual,contentEqual,"",expectArticle[0]);
        //文章自定义路径异常数据
        for(int i=0;i<slugFails.length;i++) {
            autoData = dataCreate(autoData, usernameEqual, passwordEqual, emailEqual, getStringFromArray(data), usernameEqual, passwordEqual, catagoryEqual, titleEqual, slugFails[i], tagEqual, contentEqual, "",expectSlug[i]);
        }
        //文章内容为空
        autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,passwordEqual,catagoryEqual,titleEqual,slugEqual,tagEqual," ","",expectArticle[1]);
        //文章标签过长
        autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,passwordEqual,catagoryEqual,titleEqual,slugEqual,tagMore,contentEqual,"",expectArticle[2]);
        //评论内容为空
        autoData = dataCreate(autoData,usernameEqual,passwordEqual,emailEqual,getStringFromArray(data),usernameEqual,passwordEqual,catagoryEqual,titleEqual,slugEqual,tagEqual,contentEqual," ",expectComment[0]);

        return autoData;
    }


}
