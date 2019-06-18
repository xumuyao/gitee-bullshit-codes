
/**
 * 分页对象
 * @author youao.du@gmail.com
 * @time 13:33
 * @params
 */
@Data
class PageDto {
    private Integer pageNum = 1;

    private Integer pageSize = 1;

    // 计算起始页
    public void setPageNum(Integer pageNum) {
        if (pageNum == null || pageNum <= 0) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public void calcPageNum() {
        // 设置第一个limit参数
        setPageNum((getPageNum() - 1); * getPageSize())
    }
}


/**
 * 这个问题想了很久。这样就导致了。
 * 第一页永远显示不了数据库中的第一行数据。
 *
 * 至于原因。应该很清晰
 * 计算起始坐标   当前页 - 1 * 页面容量
 * 所以第一页 1 - 1 = 0  这个时候调用了set方法。
 * set方法刚进入时进行了判断。<= 0 的话。 直接变1
 *
 * 这是我写的代码。当时调这个错误调了一下午。白白浪费了一下午
 */