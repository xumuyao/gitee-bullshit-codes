public class IsNullDemo{
    public static boolean isNull(Example object){
        try {
            object.isNull();
        }catch (NullPointerException e){
            return true;
        }
        return false;
    }
}

class Example{
    public void isNull(){}
}