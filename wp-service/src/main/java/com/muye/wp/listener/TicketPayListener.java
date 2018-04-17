package com.muye.wp.listener;

import com.muye.wp.common.cons.ParkingTicketStatus;
import com.muye.wp.common.cons.SysConfig;
import com.muye.wp.common.utils.DateUtil;
import com.muye.wp.dao.domain.ParkingTicket;
import com.muye.wp.dao.domain.query.Sort;
import com.muye.wp.service.ParkingTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by muye on 18/4/11.
 *
 * 停车单指定时间是否完成支付监听器
 */
@Component
public class TicketPayListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TicketPayListener.class);

    private final ConcurrentLinkedQueue<Ticket> unpaidQuery = new ConcurrentLinkedQueue<>();    //单付款停车单队列
    private final ConcurrentHashMap<String, Ticket> paidMap = new ConcurrentHashMap<>();        //已支付停车单
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    @Autowired
    private ParkingTicketService parkingTicketService;


    @Override
    public void afterPropertiesSet() {
        //加载所有未支付的停车单  并监听
        ParkingTicket query = new ParkingTicket();
        query.setStatus(ParkingTicketStatus.UNPAID.getStatus());
        query.addSort("pay_deadline_time", Sort.ASC);
        List<ParkingTicket> ticketList = parkingTicketService.queryListByCondition(query, null);
        ticketList.stream().forEach(this::listen);

        for (int i = 0; i < 5; i++){
            executor.execute(() -> deadlineDispose());
        }

    }

    public void listen(ParkingTicket ticket){
        unpaidQuery.offer(new Ticket(ticket));
    }

    public void unListen(ParkingTicket ticket){
        paidMap.put(ticket.getTicketNum(), new Ticket(ticket));
    }

    //支付超时处理
    private void deadlineDispose(){
        while (true){

            Ticket ticket = unpaidQuery.poll();

            //为空等待10秒  再次尝试  防止cpu消耗太高
            if (ticket == null) {
                try {
                    Thread.sleep(10 * 1000);
                }catch (InterruptedException e){
                }
                continue;
            }

            int deadline = (DateUtil.betweenMin(ticket.payDeadlineTime, new Date()) - 3) * -1;     //截止时间往后延3分钟

            logger.info("dispose {}, deadline:{}", ticket.ticketNum, deadline);

            if (deadline > 0) {
                try {
                    Thread.sleep(deadline * 60 * 1000);
                }catch (InterruptedException e){
                }
            }

            //已经支付过了
            if (paidMap.get(ticket.ticketNum) != null) continue;

            //截止时间没有支付自动取消
            try {
                parkingTicketService.cancel(ticket.ticketNum);
            }catch (Exception e){
            }
        }
    }

    private static class Ticket {

        public Ticket (ParkingTicket ticket){
            this.ticketNum = ticket.getTicketNum();
            this.payDeadlineTime = ticket.getPayDeadlineTime();
        }

        String ticketNum;
        Date payDeadlineTime;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Ticket ticket = (Ticket) o;
            return Objects.equals(ticketNum, ticket.ticketNum);
        }
    }
}
