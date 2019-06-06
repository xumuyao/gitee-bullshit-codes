import java.math.BigDecimal;

public class CreateIncome {
    /**
     * 返回支付金额
     */
    public static BigDecimal getPayFee(BigDecimal payFee) {
        return payFee.multiply(BigDecimal.TEN);
    }
}
