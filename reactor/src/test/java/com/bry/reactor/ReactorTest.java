package com.bry.reactor;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ReactorTest {

    @Test
    void testReactor() {

        //简单的序列产生
        Flux.just("Hello", "world").subscribe(log::info); //打印出 两个元素
        Flux.range(1, 10).subscribe(i -> log.info("received {}", i)); //打印出1 ~ 10
        //Flux.empty().subscribe(System.out::println);

        //Generate
        Flux.generate(sink -> {
            sink.next("hello");
            sink.complete(); //每次产生一个，调用complete 结束，如果不调用，则无限循环了。
        }).subscribe(System.out::println);

        Flux.create(fluxSink -> {//create 会一下子生成 n 个元素
            for (int i = 0; i < 10; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
        }).subscribe(System.out::println);

        //buffer - 进行分组，如果参数是2，那就按照两个一组进行分组，如果没参数，分成一组
        log.info("Buffer, group with buffer");
        Flux.range(1, 10).log("Range")
                .buffer(3)
                .take(2) // 取几个
                .log("take")
                .subscribe(System.out::println);
        //Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        //        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).take(2)
        //
        // window, group as UnicastProcessor
        log.info("window, group as UnicastProcessor");
        Flux.range(1, 100).window(20).subscribe(System.out::println);

    }

    @Test
    void anotherFluxTestWithThread() {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request number {}", n))
                .doOnComplete(() -> log.info("Request completed"))
                .publishOn(Schedulers.parallel())//影响后面的代码，表示后面的代码执行模式

                .map(integer -> {
                    log.info("Map 1 {}, Thread {}", integer, Thread.currentThread());
                    //return 10/(integer-3);
                    return integer;
                })
                //.doOnRequest(n -> log.info("Request Map 1 number {}", n))
                //.doOnNext(n -> log.info("Request Map 1 number {}", n))
                .doOnComplete(() -> log.info("Request 2 completed"))
                //.publishOn(Schedulers.parallel())
                .map(integer -> {
                    log.info("Map 2 {}, Thread {}", integer, Thread.currentThread());
                    //return 10/(integer-3);
                    return integer;
                })
                .onErrorReturn(-1)
                //.onErrorResume(e -> Mono.just(-1))
                .doOnComplete(() -> log.info("Request 3 completed"))
                //.publishOn(Schedulers.boundedElastic())
                .subscribeOn(Schedulers.single())
                .subscribe(consumer -> {
                            log.info("Subscribe {} Thread {}", consumer, Thread.currentThread());
                        },
                        error -> {
                            log.info("Error happened {}", error);
                        },
                        () -> {//on complete
                            log.info("Subscribe complete");
                        });

    }

}
