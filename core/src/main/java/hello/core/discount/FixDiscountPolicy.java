package hello.core.discount;

import hello.core.membar.Grade;
import hello.core.membar.Member;

public class FixDiscountPolicy implements DiscountPolicy {


    private  int dicountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP){
            return dicountFixAmount;
        } else {
            return 0;
        }
    }
}
