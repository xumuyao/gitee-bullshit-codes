import java.util.ArrayList;
import java.util.List;

public class ProccessingResponseResult {

    private List<String> dataSouce = new ArrayList<>();

    private void processingResponseResult(List<String> result){

        for (int i = 0; i < result.size(); i++) {
            dataSouce.add(result.get(i));
        }

        /*处理网络请求后解析的数据，存在于N个文件，😂*/

    }
}