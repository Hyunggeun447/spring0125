package study.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.study.domain.entity.order.Order;
import study.study.repository.memberRepository.MemberRepository;
import study.study.repository.orderRepository.OrderRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    /*public Long order(Long memberId, Long itemId, int count) {

    }*/
}
