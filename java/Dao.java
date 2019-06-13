/**
 * Dao层的方法，Map进，Map出
 * 神啊，求你告诉我这个箱子是干什么的吧
 */
public Map<String, Object> selRedeemInfo(Map<String, Object> param) {
    return Dao.selectOne(basepath + "selRedeemInfo", param);
}