
/**
 * 17年我刚入职接手前人的的代码，记得当时android5.0刚普及而已
 * 在他的手机上应该运行正常吧
 */
public class LoopView extends View {

    public LoopView(Context context) {
        this(context, null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);//这骚操作我给满分...
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)//这骚操作我给满分...
    public LoopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
//        ...
    }
}