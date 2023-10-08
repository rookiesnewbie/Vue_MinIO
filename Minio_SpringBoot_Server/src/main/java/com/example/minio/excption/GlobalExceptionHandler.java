package com.example.minio.excption;

import com.example.minio.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //aop
public class GlobalExceptionHandler extends Throwable {
    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody  //返回json数据
    public Result error(Exception e){
        e.printStackTrace();
//        return Result.fail(null).message("执行全局异常处理");
        return Result.fail(null).message(e.getMessage());
    }

    //特定异常处理ArithmeticException
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody  //返回json数据
    public Result error(ArithmeticException e){
        e.printStackTrace();
//        return Result.fail(null).message("执行特定异常处理ArithmeticException");
        return Result.fail(null).message(e.getMessage());
    }

    //自定义异常处理
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e){
        e.printStackTrace();
//        return Result.fail(null).code(e.getCode()).message("执行自定义异常处理");
        return Result.fail(null).code(e.getCode()).message(e.getMsg());
    }

}
