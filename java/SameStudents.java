package arrayquestion;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手程序员：为啥我已经保存了10个不同对象在list中，
 * 取出来却发现所有的对象都一样呢
 */
public class SameStudents {
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public void saveStudents(){
        List<SameStudents> studentsList = new ArrayList<>();
        SameStudents student = new SameStudents();
        for(int i = 0; i < 10; i++) {
            student.setAge((int)Math.random()*10);
            studentsList.add(student);
        }
    }
}
