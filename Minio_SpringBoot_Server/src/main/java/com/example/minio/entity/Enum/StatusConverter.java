package com.example.minio.entity.Enum;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.example.minio.excption.MyException;

import static com.example.minio.entity.Enum.AccountStatusEnum.DISABLE;
import static com.example.minio.entity.Enum.AccountStatusEnum.ENABLE;

/**
 * @Author LiTeng
 * @Date 2023/10/28 14:47
 * Version 1.0
 * @Description
 */
public class StatusConverter implements Converter<Integer> {

    /**
     * 将 Excel 展示的数据 转换为 数据库中存储的数据
     *
     * @param cellData 单元格值
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        if (ENABLE.getName().equals(cellData.getStringValue())) {
            return ENABLE.getStatus();
        } else if (DISABLE.getName().equals(cellData.getStringValue())) {
            return DISABLE.getStatus();
        } else {
            throw new MyException(500,"状态字段值必须为开启或关闭");
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
        if (ENABLE.getStatus().equals(value)) {
            return new WriteCellData<>(ENABLE.getName());
        } else if (DISABLE.getStatus().equals(value)) {
            return new WriteCellData<>(DISABLE.getName());
        } else {
            throw new MyException(500,"状态字段必须为0（开启）或1（关闭）");
        }
    }

}

