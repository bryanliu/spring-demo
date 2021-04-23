# 服务链路跟踪
本Demo演示了如何使用Zip来进行服务链路的跟踪
本Demo 有三个服务，customer, waiter, barista
customer 会下单给 waiter， waiter 会下单并且给barista发送neworder的消息，barista收到消息后，做好咖啡后会发送消息给waiter告知咖啡已经做好
