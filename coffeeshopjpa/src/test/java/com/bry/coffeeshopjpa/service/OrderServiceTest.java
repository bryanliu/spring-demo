package com.bry.coffeeshopjpa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.bry.coffeeshopjpa.model.Coffee;
import com.bry.coffeeshopjpa.model.CoffeeOrder;
import com.bry.coffeeshopjpa.model.OrderState;
import com.bry.coffeeshopjpa.repository.CoffeeOrderRepository;

@SpringBootTest
public class OrderServiceTest {

    /**
     * 可以将CoffeeOrderService都Mock，然后用InjectMocks 注入进去。这样CoffeeOrderService 就依赖了Mock的对象
     * 可以看下面两个@Mock
     */
    @InjectMocks
    CoffeeOrderService coffeeOrderService;

    @Mock
    CoffeeOrderRepository coffeeOrderRepository;

    @Mock
    CoffeeService coffeeService;

    @Test
    public void testGetALlOrders() {
        //arrange mock
        CoffeeOrder co1 = CoffeeOrder.builder().customer("Someone").state(OrderState.INIT)
                .items(Arrays.asList(
                        Coffee.builder().name("coffee1").price(20).build(),
                        Coffee.builder().name("coffee2").price(30).build()
                )).build();

        // 设置findAll() 的时候返回的数据
        when(coffeeOrderRepository.findAll()).thenReturn(Arrays.asList(co1));
        //invoke 执行调用方法
        List<CoffeeOrder> res = coffeeOrderService.getAllOrders();

        //verify 验证方法被调用一次
        verify(coffeeOrderRepository).findAll(); // 这种写法和下面等价，默认就是1次。
        // verify(coffeeOrderRepository,times(1)).findAll();
        assertEquals(1, res.size());
        assertEquals(2, res.get(0).getItems().size());
        assertEquals("Someone", res.get(0).getCustomer());

    }

    @Test
    void testSaveCoffeeOrder() {
        //arrange mock
        List<Coffee> coffeeList = Arrays.asList(
                Coffee.builder().name("coffee1").price(20).build(),
                Coffee.builder().name("coffee2").price(30).build()
        );
        when(coffeeService.findCoffeeByNames(anyList())).thenReturn(coffeeList);

        //invoke
        coffeeOrderService.saveCoffeeOrder("Someone", Arrays.asList("coffee1", "coffee2"));

        //Verify
        verify(coffeeService).findCoffeeByNames(anyList());

        // 获取依赖方法被调用的参数，这样我们可以验证调用目标方法之后，参数是不是期望的。
        ArgumentCaptor<CoffeeOrder> capture = ArgumentCaptor.forClass(CoffeeOrder.class);
        verify(coffeeOrderRepository).save(capture.capture());
        CoffeeOrder savedOrder = capture.getValue();

        assertEquals(2, savedOrder.getItems().size());
        assertEquals("Someone", savedOrder.getCustomer());
        assertEquals(OrderState.INIT, savedOrder.getState());

    }

    @Test
    public void testDelete() {

        coffeeOrderService.deleteOrder(12);
        verify(coffeeOrderRepository).deleteById(12);

    }

}
