package com.example.minio.constant;

/**
 * @Author LiTeng
 * @Date 2023/10/5 22:58
 * Version 1.0
 * @Description 项目常量
 */
public class ProjectConstant {
    /**
     * 开发环境
     */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

    /**
     * 生产环境
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    /**
     * 测试环境
     */
    public static final String SPRING_PROFILE_TEST = "test";

    /**
     * 项目基础包名称
     */
    public static final String BASE_PACKAGE = "com.example.minio";

    /**
     * Entity 所在包
     */
    public static final String ENTITY_PACKAGE = BASE_PACKAGE + ".entity";

    /**
     * Mapper 所在包
     */
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";

    /**
     * Filter 所在包
     */
    public static final String FILTER_PACKAGE = BASE_PACKAGE + ".filter";

    /**
     * Service 所在包
     */
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";

    /**
     * ServiceImpl 所在包
     */
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";

    /**
     * Controller 所在包
     */
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";

    /**
     * Mapper 插件基础接口的完全限定名
     */
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.mapper.MyCommonMapper";

    /**
     * 时间转换 年-月-日 时:分:秒
     */
    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";


    /**
     * 时间转换 年-月-日
     */
    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static final String FORMAT_YEAR_MONTH_DAY_WITH_FILE_SEPARATOR = "/yyyy/MM/dd/";

    public static final String FORMAT_YEAR_MONTH_DAY_WITH_FILE_SEPARATOR_INNER = "yyyy/MM/dd";

    public static final String TWENTY_FOUR_CLOCK = "24:00";

    /**
     * 文件夹
     */
    public static final String FOLDER = "文件夹";

    /**
     * 文件服务器部署在Linux，文件分割符只识别 “/”
     */
    public static final String FILE_SEPARATOR = "/";

    /**
     * 个人文件存放的根路径
     */
    public static final String PERSONAL_FILE_ROOT_PATH = "personal" + FILE_SEPARATOR;

    /**
     * 共享文件存放的根路径
     */
    public static final String SHARE_FILE_ROOT_PATH = "share" + FILE_SEPARATOR;

}
