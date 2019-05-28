import java.util.List;

public class Remove {

    /**
     * 移除特定元素
     */
    public static void remove(List<String> items){
        for (String item : items) {
            if (item.equals("abc")) {
                items.remove(item);
            }
        }
    }
}
