package com.example.minio.common;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class Result<T> {
    private Integer code; //状态码
    private String msg;  // 返回信息（成功、失败）
    private T data;  //返回的数据

    public Result(){} //无参的构造器

    //返回成功的方法,没有data数据
    public static<T>  Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    //返回成功的方法,有data数据
    public static<T>  Result<T> success(T data){
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    //返回失败的方法,没有data数据
    public static<T>  Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMsg("操作失败");
        return result;
    }

    //返回失败的方法,有data数据
    public static<T>  Result<T> fail(T data){
        Result<T> result = new Result<>();
        if (data != null){
            result.setData(data);
        }
        result.setCode(400);
        result.setMsg("操作失败");
        return result;
    }

    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = new Result<T>();
        if (body != null) {
            result.setData(body);
        }
        result.setCode(code);
        result.setMsg(message);
        return result;
    }
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
    public Result<T> message(String msg){
        this.setMsg(msg);
        return this;
    }

}
