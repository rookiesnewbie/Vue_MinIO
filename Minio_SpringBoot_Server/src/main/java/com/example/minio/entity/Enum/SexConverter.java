package com.example.minio.entity.Enum;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.example.minio.excption.MyException;

/**
 * @Author LiTeng
 * @Date 2023/10/28 14:45
 * Version 1.0
 * @Description
 */
public class SexConverter implements Converter<Integer> {

    public static final String FEMALE = "女";

    public static final String MALE = "男";

    public static final Integer FEMALE_VALUE = 1;

    public static final Integer MALE_VALUE = 2;

    /**
     * 将 Excel 展示的数据 转换为 数据库中存储的数据
     *
     * @param cellData 单元格值
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        if (FEMALE.equals(cellData.getStringValue())) {
            return FEMALE_VALUE;
        } else if (MALE.equals(cellData.getStringValue())) {
            return MALE_VALUE;
        } else {
            throw new MyException(500,"性别字段值必须为男或女");
        }
    }

    /**
     * 将从数据库中查到的数据转换为 Excel 展示的数据
     *
     * @param value 枚举值
     */
    @Override
    public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (FEMALE_VALUE.equals(value)) {
            return new WriteCellData<>(FEMALE);
        } else if (MALE_VALUE.equals(value)) {
            return new WriteCellData<>(MALE);
        } else {
            throw new MyException(500,"性别字段值必须为男或女");
        }
    }

}