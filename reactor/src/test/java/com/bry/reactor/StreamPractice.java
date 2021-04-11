package com.bry.reactor;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamPractice {

    /**
     * 由于Reactor模式要用到很多Lambda和Stream的方式，这儿先练习一下。
     */
    @Test
    public void testlambda() {

        //这两种操作方式是等价的，下面是双冒号的方式
        Arrays.asList("a", "b", "c").forEach(s -> log.info(s));
        // 其实传过去的是方法的引用，里面会执行方法的引用，这是函数式编程的用法， Python里面就经常用。
        Arrays.asList("a", "b", "c").forEach(log::info);

        //Stream 的构造方式
        /**
         * Collection.steam
         * Stream.of()
         * IntStream.range(1, 3) //对于三种基本的数值类型（int, long, double）有对应的Stream
         */
        log.info("使用collection产生流");
        List<Integer> a = Arrays.asList(1, 2, 3);
        a.forEach(e -> log.info("{}", e));
        log.info("使用IntStream产生流");
        IntStream.range(1, 3).forEach(e -> log.info("{}", e));
        IntStream.rangeClosed(1, 3).forEach(e -> log.info("{}", e));
        log.info("使用Stream.of() 产生流");
        Stream.of("1", "2", "3").forEach(log::info);

        //terminal 操作转换成其他流
        List<Integer> l = Stream.of(1, 2, 3).collect(Collectors.toList());
        log.info("将Stream转成 List {}", l);
        Integer[] la = Stream.of(1, 2, 3).toArray(Integer[]::new);
        log.info("将Stream转成数组，长度 {}", la.length);
        String s = Stream.of(1, 2, 3).map(integer -> Integer.toString(integer)).collect(Collectors.joining(" "));
        log.info("通过转换将整形Stream 连接成字符串 {}", s);

        //转换操作
        //Map
        log.info("从1-5，将每个数变成平方");
        List<Integer> convertList = Arrays.asList(1, 2, 3).stream().map(n -> n * n).collect(Collectors.toList());
        log.info("{}", convertList);
        //IntStream 要想获得Collection，需要boxed 一下
        List<Integer> convertList2 = IntStream.rangeClosed(1, 3)
                .map(n -> n * n)
                .boxed()
                .collect(Collectors.toList());
        log.info("{}", convertList2);

        //flatMap
        //可以用于多维的数据打平
        List<Integer> fm = Stream.of(
                Arrays.asList(1, 2),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8)
        ).flatMap(c -> c.stream()).collect(Collectors.toList());
        log.info(fm.toString());
        assertEquals(Arrays.asList(1, 2, 4, 5, 6, 7, 8), fm);

        //filter
        List<Integer> filter1 = Arrays.asList(1, 2, 4, 5).stream()
                .filter(e -> e < 4)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(1, 2), filter1);

        //filter and map
        //找出长度大于3的字符串，并且转换成大写
        List<String> fmu = Stream.of("one", "two", "three", "four")
                .filter(s1 -> s1.length() > 3)
                .peek(e -> log.info("Filtered value {}", e))
                .map(String::toUpperCase)
                .peek(e -> log.info("Mapped value {}", e))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("THREE", "FOUR"), fmu);

        //limit, skip
        List<String> limitskip1 = Stream.of("one", "two", "three", "four")
                .limit(3)
                .skip(1)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("two", "three"), limitskip1);

        //Sort
        List<String> sort1 = Stream.of("one", "two", "three", "four")
                //.sorted() // 按字母排序，这样就够了
                .sorted(comparing(String::length).reversed()) //使用comparing 对象，就可以自定义比较方式了，比如长度
                .collect(Collectors.toList());
        log.info(sort1.toString());
        assertEquals(Arrays.asList("three", "four", "one", "two"), sort1);

        //获得最长的长度
        int len1 = Stream.of("one", "two", "three", "four").mapToInt(String::length).max().getAsInt();
        assertEquals(5, len1);

        //reduce
        //连接字符串
        String str1 = Stream.of("one", "two").reduce("", String::concat);
        assertEquals("onetwo", str1);
        //数组和，没有初始值，返回optional
        Optional<Integer> res = Stream.of(1, 2, 3).reduce(Integer::sum);
        // assertTrue(res.isPresent());
        assertEquals(6, res.get());

        // 有初始值，返回int
        int res2 = Stream.of(1, 2, 3).reduce(0, Integer::sum);
        assertEquals(6, res.get());

        //distinct - 去重
        List<String> res3 = Stream.of("one", "two", "two", "two").distinct().collect(Collectors.toList());
        assertEquals(Arrays.asList("one", "two"), res3);

        //match, allmatch, anymatch, nonmatch, 检测是否符合条件
        boolean allmatch = Stream.of(1, 2, 3).allMatch(p -> p > 0);
        assertTrue(allmatch);
        boolean anymatch = Stream.of(1, 2, 3).anyMatch(p -> p > 4);
        assertFalse(anymatch);
        boolean nonmatch = Stream.of(1, 2, 3).noneMatch(p -> p > 10);
        assertTrue(nonmatch);

    }


    @Test
    @Disabled
    void testParallar() {
        /**
         * 如果执行时间很短的话，并行计算时间不一定缩短，因为线程切换需要时间
         * 下面实例sleep 200 ms, 效果就出来了。
         * 所以多线程使用也要分场景
         * 并行2s，串行20s
         */
        int res = IntStream.rangeClosed(1, 100)
                .parallel()
                .peek(e -> {
                    //log.info("{}", e);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                })
                .map(n -> n*n)
                .max().getAsInt();
        log.info("{}", res);
    }
}
