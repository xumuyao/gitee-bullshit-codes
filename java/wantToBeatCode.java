/**
 * 需求是这样的：
 * 从某中心仓库中，获取产品列表，并增量更新本地产品数据。
 * 结果：
 * 因商品更新不频繁，系统运行了好几个月，发现无新增数据...
 */
public class WantToBeatCode {

    /**
     * 简单举个例子
     * <xml><pid>1000</pid></xml>
     * @param args
     */
    public static void main(String[] args) {
        // 假设拿好了一个XML
        Document doc = new SAXReader().read(new File("./src/contact.xml"));
        Node node = doc.selectSingleNode("pid");
        // 知道用equals，老子很欣慰，但是你拿个node类型和integer做equals，就很难受啊...
        if(node.equals(1000)){
            // 添加数据...
            insert(product);
        }
    }
}
