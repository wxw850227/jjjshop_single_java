package net.jjjshop.job.scheduled;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.job.service.user.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CouponScheduled {

    @Autowired
    private UserCouponService userCouponService;


    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ? ")
    public void execute() throws Exception {
        // 优惠券
        this.setExpired();
    }

    private void setExpired(){
        List<Integer> couponIds = userCouponService.getExpiredCouponIds();
        userCouponService.setIsExpire(couponIds);
        //记录日志
        if(couponIds.size() > 0){
            log.info(String.format(" userCoupon scheduled setExpired couponIds:%s", couponIds));
        }
    }

}
