public class WhichOne
{
    public static void Guess()
    {
        var isMe = isMe(5);
    }

    public static int isMe(int nums)
    {
        return nums != 0 ? nums > 0 && nums < 10 ? 1 : nums >= 10 ? 2 : 3 : 0;
    }
}