package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy DiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인 적용")
    void vip_o() {
        //given
        Member memberVIP = new Member(1L, "memberVIP", Grade.VIP);

        //when
        int discount = DiscountPolicy.discount(memberVIP, 10000);

        //then

        assertThat(discount).isEqualTo(1000);

    }

    @Test
    @DisplayName("VIP가 아니면 할인적용이 안됀다")
    void vip_x() {
        //given
        Member memberNoneVIP = new Member(2L, "memberNoneVIP", Grade.BASIC);

        //when
        int discount = DiscountPolicy.discount(memberNoneVIP, 10000);

        //then

        assertThat(discount).isEqualTo(0);

    }


}