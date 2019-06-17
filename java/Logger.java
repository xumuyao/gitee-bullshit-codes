import org.slf4j.Marker;
// 当时完全不懂包装slf4j什么意思,还以为自己针对市面上的logger进行了封装处理，上次开会的时候这么说,我为了logger的时候像andriod能写个tag，WTF，难道你logger.info('tag:{},xxxxx',tag)这样不行么？为了六个字符创建一个logger和一个loggerFactory
public class Logger {

    /** 日志处理 */
    private org.slf4j.Logger logger;

    public static String separator = " ";

    public Logger(Class clazz) {
        logger = org.slf4j.LoggerFactory.getLogger(clazz);
    }

    public Logger(String name) {
        logger = org.slf4j.LoggerFactory.getLogger(name);
    }

    public String getName() {
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String format, Object arg) {
        logger.trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, arg1, arg2);
    }

    public void trace(String format, Object... arguments) {
        logger.trace(format, arguments);
    }

    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String msg) {
        logger.trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, format, argArray);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(String format, Object arg) {
        logger.debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, arg1, arg2);
    }

    public void debug(String format, Object... arguments) {
        logger.debug(format, arguments);
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, format, arguments);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, msg, t);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String format, Object arg) {
        logger.info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, arg1, arg2);
    }

    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {
        logger.info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, format, arguments);
    }

    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, msg, t);
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(String format, Object arg) {
        logger.warn(format, arg);
    }

    public void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, arg1, arg2);
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, format, arguments);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, msg, t);
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String format, Object arg) {
        logger.error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, arg1, arg2);
    }

    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    public void error(Marker marker, String msg) {
        logger.error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, format, arguments);
    }

    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, msg, t);
    }

    /**
     * trace等级日志，小于debug
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param args 变量对应的参数
     */
    public void trace(Tag tag, String format, Object... args) {
        logger.trace(merge(tag, format), args);
    }

    /**
     * trace等级日志，小于debug
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param e 需在日志中堆栈打印的异常
     */
    public void trace(Tag tag, String format, Throwable e) {
        logger.trace(merge(tag, format), e);
    }

    /**
     * debug等级日志，小于info
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param args 变量对应的参数
     */
    public void debug(Tag tag, String format, Object... args) {
        logger.debug(merge(tag, format), args);
    }

    /**
     * debug等级日志，小于info
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param e 需在日志中堆栈打印的异常
     */
    public void debug(Tag tag, String format, Throwable e) {
        logger.debug(merge(tag, format), e);
    }

    /**
     * info等级日志，小于warn
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param args 变量对应的参数
     */
    public void info(Tag tag, String format, Object... args) {
        logger.info(merge(tag, format), args);
    }

    /**
     * info等级日志，小于warn
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param e 需在日志中堆栈打印的异常
     */
    public void info(Tag tag, String format, Throwable e) {
        logger.info(merge(tag, format), e);
    }

    /**
     * warn等级日志，小于error
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param args 变量对应的参数
     */
    public void warn(Tag tag, String format, Object... args) {
        logger.warn(merge(tag, format), args);
    }

    /**
     * warn等级日志，小于error
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param e 需在日志中堆栈打印的异常
     */
    public void warn(Tag tag, String format, Throwable e) {
        logger.warn(merge(tag, format), e);
    }

    /**
     * error等级日志
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param args 变量对应的参数
     */
    public void error(Tag tag, String format, Object... args) {
        logger.error(merge(tag, format), args);
    }

    /**
     * error等级日志
     *
     * @param tag 日志标签
     * @param format 格式文本，{} 代表变量
     * @param e 需在日志中堆栈打印的异常
     */
    public void error(Tag tag, String format, Throwable e) {
        logger.error(merge(tag, format), e);
    }

    // 合并
    private String merge(Tag tag, String format) {
        if (tag == null) {
            return format;
        }

        return tag.toString() + separator + format;
    }

}