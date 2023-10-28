package com.example.minio;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author LiTeng
 * @Date 2023/10/15 17:36
 * Version 1.0
 * @Description 测试反射
 */
public class TestReflection {


    @Test
    public void test() throws NoSuchMethodException, NoSuchFieldException, ClassNotFoundException {


        Class c1 = Class.forName("com.example.minio.admin.User");

        //获取类的名字
        System.out.println("getName:"+c1.getName());//获取包名+类名
        System.out.println("类名："+c1.getSimpleName());//获取类名

        //获取类的属性
        System.out.println("==================================");
        Field[] fields = c1.getFields();//只能找到public属性
        Field[] declaredFields = c1.getDeclaredFields();//可以找到全部属性
        for (Field field:fields) {
            System.out.println("获取被public修饰的属性：" + field.getName());
        }
        for (Field declaredField : declaredFields) {
            System.out.println("获取全部属性："+declaredField.getName());
        }
        //获取指定属性的值
        Field name = c1.getDeclaredField("name");
        System.out.println(name.getName());

        //获取类的方法
        System.out.println("==================================");
        Method[] methods = c1.getMethods();//获取本类及其父类的全部public方法
        for (Method method:methods
        ) {
            System.out.println(method);
        }
        System.out.println("==================================");
        methods = c1.getDeclaredMethods();//获取本类的所有方法
        for (Method method:methods
        ) {
            System.out.println(method);
        }

        //获取指定的方法
        Method getName = c1.getMethod("getName",null);//要有参数，因为Java有重载的方法，以便于获取到指定的方法
        Method setName = c1.getMethod("setName", String.class);

        System.out.println(getName);
        System.out.println(setName);

        //获取类的所有构造器
        Constructor[] constructor = c1.getConstructors();//只能获取public权限的所有构造器
        for (Constructor constructor1 : constructor) {
            System.out.println(constructor1);
        }
        constructor = c1.getDeclaredConstructors();//获取所有的构造器
        for (Constructor constructor1 : constructor) {
            System.out.println("#"+constructor1);
        }

        //获取指定的构造器
        Constructor declaredMethod = c1.getDeclaredConstructor(String.class, int.class, int.class);
        System.out.println("指定的构造器："+declaredMethod);
    }


    @Test
    public void test1(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入一个数字: ");

        // 读取输入的数字
        int number = scanner.nextInt();

        // 使用正则表达式匹配数字的位数
        String numberString = String.valueOf(number);
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(numberString);
        int numberOfDigits = 0;
        while (matcher.find()) {
            numberOfDigits++;
        }

        System.out.println("输入的数字有 " + numberOfDigits + " 位。");
    }


}
