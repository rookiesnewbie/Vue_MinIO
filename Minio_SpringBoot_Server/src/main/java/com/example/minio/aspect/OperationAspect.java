package com.example.minio.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.example.minio.annotation.Checking;
import com.example.minio.annotation.VerifyParameters;
import com.example.minio.excption.MyException;
import com.example.minio.util.VerifyUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author LiTeng
 * @Date 2023/10/13 10:38
 * Version 1.0
 * @Description 参数校验切面
 */

@Aspect
@Component
@Slf4j
public class OperationAspect {


    private static final  String[] BASE_TYPE_ARRAY = new String[]{"java.lang.String","java.lang.String","java.lang.Long"};

    //定义切点
    @Pointcut("@annotation(com.example.minio.annotation.Checking)")

    public void pointCut(){

    }

    //定义切面的通知类型
    @Before("pointCut()") //在切点执行之前要进行校验
    public void  beforeOperation(JoinPoint joinPoint){

        //获取参数
        Object[] args = joinPoint.getArgs();
        log.info(joinPoint.getArgs().toString());

        //通过反射获取方法名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.info("方法名：{}",method.getName());

        //判断方法是否含有自定义注解
        Checking annotation = method.getAnnotation(Checking.class);
        if (null == annotation){
            return;
        }

        //校验参数
        if (annotation.checkParams()){
            validataParams(method,args);
        }

    }

    /**
     * 参数校验
     * @param method
     * @param agrs
     */
    private void validataParams(Method method,Object[] agrs){
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object agr = agrs[i];

            //检查参数是否被注解VerifyParameters修饰
            VerifyParameters annotation = parameter.getAnnotation(VerifyParameters.class);

            if ( null == annotation){
                //没有被VerifyParameters注解修饰，跳过
                continue;
            }

            //获取校验参数的数据类型
            String typeName = parameter.getParameterizedType().getTypeName();

            //基本数据类型
            if (ArrayUtil.contains(BASE_TYPE_ARRAY,typeName)){
                checkValue(agr,annotation);
            }else {
                checkeObjValue(parameter,agr);
            }
        }
    }

    //校验参数的类型为基本数据类型
    private void checkValue(Object value,VerifyParameters annotation){
        Boolean isEmpty = value == null || StrUtil.isEmpty(value.toString());

        Integer length = value == null ? 0 : value.toString().length();

        //校验空  如果参数为必传，而没有传，则抛出异常
        if (isEmpty && annotation.required()){
            throw new MyException(500,"参数错误");
        }

        //校验长度
        if (!isEmpty && (annotation.min() != -1 && annotation.min() < length) || (annotation.max() != -1 && annotation.max() > length)){
            throw new MyException(500,"参数错误");
        }

        //校验正则
        if (!isEmpty && !StrUtil.isEmpty(annotation.regex().getRegex()) && VerifyUtils.verify(annotation.regex(),String.valueOf(value))){
            throw new MyException(500,"参数错误");
        }
    }


    //校验参数的类型为对象类型
    private void  checkeObjValue(Parameter parameter,Object value){
        try {
            //通过反射获取对象中的成员变量的数据类型
            String typeName = parameter.getParameterizedType().getTypeName();
            Class classz = Class.forName(typeName);

            //获取所有的成员变量
            Field[] fields = classz.getDeclaredFields();

            //遍历成员变量是否被VerifyParameters注解修饰
            for (Field field : fields) {
                VerifyParameters fieldParameter = field.getAnnotation(VerifyParameters.class);
                if (fieldParameter == null){
                    continue;
                }

                field.setAccessible(true);

                //获取被VerifyParameters注解修饰的成员属性的数据类型
                Object resultValue = field.get(value);
                checkValue(resultValue,fieldParameter);
            }
        } catch (Exception e) {
            log.error("参数出错");
            throw new MyException(500,"参数错误");
        }
    }


}
