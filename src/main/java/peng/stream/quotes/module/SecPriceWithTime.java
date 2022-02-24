package peng.stream.quotes.module;

import lombok.Data;

import java.time.Instant;

/**
 * Create by peng on 2022/2/24.
 */
@Data
public class SecPriceWithTime {
    //证券代码
    private String secCode;

    //证券价格
    private String secPrice;

    //获取时间
    private Instant time;

}
