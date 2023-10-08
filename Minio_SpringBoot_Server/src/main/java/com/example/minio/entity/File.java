package com.example.minio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.minio.constant.ProjectConstant;
import com.example.minio.entity.Enum.FileShareTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file")
@ApiModel(value="File对象", description="文件")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "大小")
    private String size;

    @ApiModelProperty(value = "员工id")
    private Long empId;

    @ApiModelProperty(value = "父级id 0为根目录")
    private Long parentId;

    @ApiModelProperty(value = "是否公共")
    private Integer isShared;

    @ApiModelProperty(value = "content-type")
    private String contentType;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updater;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "删除批次号")
    private String deletedBatchNum;

    @ApiModelProperty(value = "挂载的共享目录")
    private Long mountShareFolderId;

    /**
     * 是否是文件夹
     */
    public Boolean isFolder() {
        return ProjectConstant.FOLDER.equals(this.type);
    }

    /**
     * 是否是共享
     */
    public Boolean isShare() {
        return FileShareTypeEnum.IS_SHARED.getValue().equals(this.isShared);
    }

}
