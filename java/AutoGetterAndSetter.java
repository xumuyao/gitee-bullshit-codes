/**
 *  boolean类型的字段 如果定义为is*** 自动代码出来的getter and setter  会去掉is
 *
 * @version 1.0
 * @date 2019/5/31
 */
public class AutoGetterAndSetter {

    private String isMan;

    private Boolean isPerson;


    /***以下是自动代码出来的getter and setter ***/
    public String getIsMan() {
        return isMan;
    }

    public void setIsMan(String isMan) {
        this.isMan = isMan;
    }

    public Boolean getPerson() {
        return isPerson;
    }

    public void setPerson(Boolean person) {
        isPerson = person;
    }
}
