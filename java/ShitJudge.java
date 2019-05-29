/**
 * 判断表单数据是否被发起流程.
 * 
 * @param _formDef 表单对象
 * @param id 表单数据id
 * @return
 */
public boolean isFormDataAssocationProcess(FormDef _formDef, Long id) {
    String sql = "select count(*) from t_auto_" + _formDef.getId() + " where db_id=:id and processinstanceid_ is not null";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    int count = this.genericJdbcDAO.queryForInt(sql, params);
    // 这个判断，一口老血喷出来，一个项目经理写的
    if (count > 0) {
        return true;
    } else {
        return false;
    }
}