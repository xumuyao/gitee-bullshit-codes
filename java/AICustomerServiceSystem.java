import java.util.Scanner;

/**
 * @Description 智能AI客服服务系统
 * @Author saltfish
 * @Date 15:35 2019/6/22
 */
public class AICustomerServiceSystem {


  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String str;
    // 待优化
    while (true) {
      str = sc.next();
      // 测试环境，正式环境从数据库中获取
      str = str.replace("吗", "");
      str = str.replace("？", "！");
      str = str.replace("？", "！");
      str = str.replace("不", "很");
      str = str.replace("你们", "我们");
      str = str.replace("有", "没有");

      System.out.println(str);
    }

    // test
//    在吗？
//    在！
//    你好!
//    你好!
//    产品有问题啊
//    产品没有问题啊
//    你们的服务态度不好
//    我们的服务态度很好
  }
}