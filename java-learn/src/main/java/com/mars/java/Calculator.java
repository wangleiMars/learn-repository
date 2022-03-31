package com.mars.java;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Calculator {

    static NumberFormat FORMAT = NumberFormat.getInstance();
    static {
        FORMAT.setGroupingUsed(false);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(calculateEqualPrincipalAndInterestApart(1108000,360,200000,40,5.975)));
    }
    /**
     * 计算等额本息还款
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param rate      贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalAndInterest(double principal, int months, double rate) {

        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);//每月还款金额
        double totalMoney = preLoan * months;//还款总额
        double interest = totalMoney - principal;//还款总利息
        data.add(FORMAT.format(totalMoney));//还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//还款总利息
        data.add(FORMAT.format(preLoan));//每月还款金额
        data.add(String.valueOf(months));//还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 计算等额本金还款
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param rate      贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipal(double principal, int months, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double prePrincipal = principal / months;//每月还款本金
        double firstMonth = prePrincipal + principal * monthRate;//第一个月还款金额
        double decreaseMonth = prePrincipal * monthRate;//每月利息递减
        double interest = (months + 1) * principal * monthRate / 2;//还款总利息
        double totalMoney = principal + interest;//还款总额
        data.add(FORMAT.format(totalMoney));//还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//还款总利息
        data.add(FORMAT.format(firstMonth));//首月还款金额
        data.add(FORMAT.format(decreaseMonth));//每月递减利息
        data.add(String.valueOf(months));//还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 一次性提前还款计算（等额本息）
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param payTimes  已还次数
     * @param rate      贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalAndInterest(double principal, int months, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);//每月还款金额
        double totalMoney = preLoan * months;//还款总额
        double interest = totalMoney - principal;//还款总利息
        double leftLoan = principal * Math.pow(1 + monthRate, payTimes) - preLoan * (Math.pow(1 + monthRate, payTimes) - 1) / monthRate;//n个月后欠银行的钱
        double payLoan = principal - leftLoan;//已还本金
        double payTotal = preLoan * payTimes;//已还总金额
        double payInterest = payTotal - payLoan;//已还利息
        double totalPayAhead = leftLoan * (1 + monthRate);//剩余一次还清
        double saveInterest = totalMoney - payTotal - totalPayAhead;
        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(preLoan));//原还每月还款金额
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(totalPayAhead));//一次还清支付金额
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(0));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 一次性提前还款计算(等额本金)
     *
     * @param principal 贷款总额
     * @param months    贷款期限
     * @param payTimes  已还次数
     * @param rate      贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipal(double principal, int months, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double prePrincipal = principal / months;//每月还款本金
        double firstMonth = prePrincipal + principal * monthRate;//第一个月还款金额
        double decreaseMonth = prePrincipal * monthRate;//每月利息递减
        double interest = (months + 1) * principal * monthRate / 2;//还款总利息
        double totalMoney = principal + interest;//还款总额
        double payLoan = prePrincipal * payTimes;//已还本金
        double payInterest = (principal * payTimes - prePrincipal * (payTimes - 1) * payTimes / 2) * monthRate;//已还利息
        double payTotal = payLoan + payInterest;//已还总额
        double totalPayAhead = (principal - payLoan) * (1 + monthRate);//提前还款金额（剩余本金加上剩余本金当月利息）
        double saveInterest = totalMoney - payTotal - totalPayAhead;
        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(firstMonth));//原首月还款金额
        data.add(FORMAT.format(decreaseMonth));//原每月递减利息
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(totalPayAhead));//一次还清支付金额
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(0));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 部分提前还款计算（等额本息、月供不变）
     *
     * @param principal      贷款总额
     * @param months         贷款期限
     * @param aheadPrincipal 提前还款金额
     * @param payTimes       已还次数
     * @param rate           贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalAndInterestApart(double principal, int months, double aheadPrincipal, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);//每月还款金额
        double totalMoney = preLoan * months;//还款总额
        double interest = totalMoney - principal;//还款总利息
        double leftLoanBefore = principal * Math.pow(1 + monthRate, payTimes) - preLoan * (Math.pow(1 + monthRate, payTimes) - 1) / monthRate;//提前还款前欠银行的钱
        double leftLoan = principal * Math.pow(1 + monthRate, payTimes + 1) - preLoan * (Math.pow(1 + monthRate, payTimes + 1) - 1) / monthRate - aheadPrincipal;//提前还款后欠银行的钱
        double payLoan = principal - leftLoanBefore;//已还本金
        double payTotal = preLoan * payTimes;//已还总金额
        double payInterest = payTotal - payLoan;//已还利息
        double aheadTotalMoney = aheadPrincipal + preLoan;//提前还款总额
        //计算剩余还款期限
        int leftMonth = (int) Math.floor(Math.log(preLoan / (preLoan - leftLoan * monthRate)) / Math.log(1 + monthRate));
        double newPreLoan = (leftLoan * monthRate * Math.pow((1 + monthRate), leftMonth)) / (Math.pow(  (1 + monthRate), leftMonth) - 1);//剩余贷款每月还款金额
        double leftTotalMoney = newPreLoan * leftMonth;//剩余还款总额
        double leftInterest = leftTotalMoney - (leftLoan - aheadPrincipal);
        double saveInterest = totalMoney - aheadTotalMoney - leftTotalMoney - payTotal;

        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(preLoan));//原还每月还款金额
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(aheadTotalMoney));//提前还款总额
        data.add(FORMAT.format(leftTotalMoney));//剩余还款总额
        data.add(FORMAT.format(leftInterest));//剩余还款总利息
        data.add(FORMAT.format(newPreLoan));//剩余每月还款金额
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(leftMonth));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 部分提前还款计算(等额本金、月供不变)
     *
     * @param principal      贷款总额
     * @param months         贷款期限
     * @param aheadPrincipal 提前还款金额
     * @param payTimes       已还次数
     * @param rate           贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalApart(double principal, int months, double aheadPrincipal, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double prePrincipal = principal / months;//每月还款本金
        double firstMonth = prePrincipal + principal * monthRate;//第一个月还款金额
        double decreaseMonth = prePrincipal * monthRate;//每月利息递减
        double interest = (months + 1) * principal * monthRate / 2;//还款总利息
        double totalMoney = principal + interest;//还款总额
        double payLoan = prePrincipal * payTimes;//已还本金
        double payInterest = (principal * payTimes - prePrincipal * (payTimes - 1) * payTimes / 2) * monthRate;//已还利息
        double payTotal = payLoan + payInterest;//已还总额
        double aheadTotalMoney = (principal - payLoan) * monthRate + aheadPrincipal + prePrincipal;//提前还款金额
        double leftLoan = principal - aheadPrincipal - payLoan - prePrincipal;//剩余金额
        int leftMonth = (int) Math.floor(leftLoan / prePrincipal);
        double newPrePrincipal = leftLoan / leftMonth;//新的每月还款本金
        double newFirstMonth = newPrePrincipal + leftLoan * monthRate;//新的第一个月还款金额
        double newDecreaseMonth = newPrePrincipal * monthRate;//新的每月利息递减
        double leftInterest = (leftMonth + 1) * leftLoan * monthRate / 2;//还款总利息
        double leftTotalMoney = leftLoan + leftInterest;//还款总额
        double saveInterest = totalMoney - payTotal - aheadTotalMoney - leftTotalMoney;
        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(firstMonth));//原还首月还款金额
        data.add(FORMAT.format(decreaseMonth));//原每月递减利息
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(aheadTotalMoney));//提前还款总额
        data.add(FORMAT.format(leftTotalMoney));//剩余还款总额
        data.add(FORMAT.format(leftInterest));//剩余还款总利息
        data.add(FORMAT.format(newFirstMonth));//剩余首月还款金额
        data.add(FORMAT.format(newDecreaseMonth));//剩余月递减利息
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(leftMonth));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 部分提前还款计算（等额本息、期限不变）
     *
     * @param principal      贷款总额
     * @param months         贷款期限
     * @param aheadPrincipal 提前还款金额
     * @param payTimes       已还次数
     * @param rate           贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalAndInterestApart2(double principal, int months, double aheadPrincipal, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);//每月还款金额
        double totalMoney = preLoan * months;//还款总额
        double interest = totalMoney - principal;//还款总利息
        double leftLoanBefore = principal * Math.pow(1 + monthRate, payTimes) - preLoan * (Math.pow(1 + monthRate, payTimes) - 1) / monthRate;//提前还款前欠银行的钱
        double leftLoan = principal * Math.pow(1 + monthRate, payTimes + 1) - preLoan * (Math.pow(1 + monthRate, payTimes + 1) - 1) / monthRate;//提前还款后银行的钱
        double payLoan = principal - leftLoanBefore;//已还本金
        double payTotal = preLoan * payTimes;//已还总金额
        double payInterest = payTotal - payLoan;//已还利息
        double aheadTotalMoney = preLoan + aheadPrincipal;//下个月还款金额
        double newPreLoan = ((leftLoan - aheadPrincipal) * monthRate * Math.pow((1 + monthRate), months - payTimes - 1)) / (Math.pow((1 + monthRate), months - payTimes - 1) - 1);//下个月起每月还款金额
        double leftTotalMoney = newPreLoan * (months - payTimes);
        double leftInterest = leftTotalMoney - (leftLoan - aheadPrincipal);
        double saveInterest = totalMoney - payTotal - aheadTotalMoney - leftTotalMoney;
        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(preLoan));//原还每月还款金额
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(aheadTotalMoney));//提前还款总额
        data.add(FORMAT.format(leftTotalMoney));//剩余还款总额
        data.add(FORMAT.format(leftInterest));//剩余还款总利息
        data.add(FORMAT.format(newPreLoan));//剩余每月还款金额
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(months));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }

    /**
     * 部分提前还款计算(等额本金、期限不变)
     *
     * @param principal      贷款总额
     * @param months         贷款期限
     * @param aheadPrincipal 提前还款金额
     * @param payTimes       已还次数
     * @param rate           贷款利率
     * @return
     */
    public static String[] calculateEqualPrincipalApart2(double principal, int months, double aheadPrincipal, int payTimes, double rate) {
        ArrayList<String> data = new ArrayList<String>();
        double monthRate = rate / (100 * 12);//月利率
        double prePrincipal = principal / months;//每月还款本金
        double firstMonth = prePrincipal + principal * monthRate;//第一个月还款金额
        double decreaseMonth = prePrincipal * monthRate;//每月利息递减
        double interest = (months + 1) * principal * monthRate / 2;//还款总利息
        double totalMoney = principal + interest;//还款总额
        double payLoan = prePrincipal * payTimes;//已还本金
        double payInterest = (principal * payTimes - prePrincipal * (payTimes - 1) * payTimes / 2) * monthRate;//已还利息
        double payTotal = payLoan + payInterest;//已还总额
        double aheadTotalMoney = (principal - payLoan) * monthRate + aheadPrincipal + prePrincipal;//提前还款金额
        int leftMonth = months - payTimes - 1;
        double leftLoan = principal - aheadPrincipal - payLoan - prePrincipal;
        double newPrePrincipal = leftLoan / leftMonth;//新的每月还款本金
        double newFirstMonth = newPrePrincipal + leftLoan * monthRate;//新的第一个月还款金额
        double newDecreaseMonth = newPrePrincipal * monthRate;//新的每月利息递减
        double leftInterest = (leftMonth + 1) * leftLoan * monthRate / 2;//还款总利息
        double leftTotalMoney = leftLoan + leftInterest;//还款总额
        double saveInterest = totalMoney - payTotal - aheadTotalMoney - leftTotalMoney;
        data.add(FORMAT.format(totalMoney));//原还款总额
        data.add(FORMAT.format(principal));//贷款总额
        data.add(FORMAT.format(interest));//原还款总利息
        data.add(FORMAT.format(firstMonth));//原还首月还款金额
        data.add(FORMAT.format(decreaseMonth));//原每月递减利息
        data.add(FORMAT.format(payTotal));//已还总金额
        data.add(FORMAT.format(payLoan));//已还本金
        data.add(FORMAT.format(payInterest));//已还利息
        data.add(FORMAT.format(aheadTotalMoney));//提前还款总额
        data.add(FORMAT.format(leftTotalMoney));//剩余还款总额
        data.add(FORMAT.format(leftInterest));//剩余还款总利息
        data.add(FORMAT.format(newFirstMonth));//剩余首月还款金额
        data.add(FORMAT.format(newDecreaseMonth));//剩余月递减利息
        data.add(FORMAT.format(saveInterest));//节省利息
        data.add(String.valueOf(months));//剩余还款期限
        return data.toArray(new String[data.size()]);
    }
}
