package top.kkuily.xingbackend.utils;

//    SILENT = 0,
//    WARN_MESSAGE = 1,
//    ERROR_MESSAGE = 2,
//    NOTIFICATION = 3,
//    REDIRECT = 9,

/**
 * 错误类型
 */
public enum ErrorType {
    SILENT(0),
    WARN_MESSAGE(1),
    ERROR_MESSAGE(2),
    NOTIFICATION(3),
    REDIRECT(9);

    ErrorType(int i) {
    }
}
