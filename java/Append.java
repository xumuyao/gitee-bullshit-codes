/**
 * Create Time 2019/5/24
 * StringBuffer追加 如痴如醉的写法
 * @author cailong
 **/
public class Append {
    public static void main(String[] ares){

        StringBuffer sb = new StringBuffer();
        //这里都能理解
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT>");
        for (int i = 0; i < 10; i++) {
            //为什么到这里就要这样写？？？既然都用StringBuffer了    （这里省略集合遍历用i代替 意思能懂就行）
            sb.append("<NSRXX>" +
                    "<NSRSBH>"+i+"</NSRSBH>" +
                    "<NSRMC>"+i+"</NSRMC>" +
                    "<DJXH>"+i+"</DJXH>" +
                    "<ZGSWJ_DM>"+i+"</ZGSWJ_DM>" +
                    "<ZGSWJ_MC>"+i+"</ZGSWJ_MC>" +
                    "<SJLY>sjzs</SJLY>" +
                    "<YWSX_DM>"+i+"</YWSX_DM>" +
                    "</NSRXX>");
        }
        sb.append("</ROOT>");
        System.out.println(sb.toString());
    }
}
