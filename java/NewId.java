
import Java.util.Random;

class SomeBizOperation {

    /*疯狂到令人发指*/
    public String getNewId() {

        while (true) {
            String id = newId(10);
            var obj = SomeTableQuery.getById(id);
            if (obj == null) {
                return obj;
            }
        }
    }

    private String newId(int length) {
        String str = "0123456789";
        var random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i =0; i<length; i++){
            int number = random.next(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}