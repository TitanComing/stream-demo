package peng.stream.quotes.module;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Create by peng on 2022/2/22.
 *
 * 计算结果:当前时段的最低价格，最高价格，平均价格
 */
@Data
@NoArgsConstructor
public class QuotePriceResult {

   private String secCode;

   private String maxPrice;

   private String minPrice;

   private String avgPrice;

   private Instant updateTime;

   public QuotePriceResult(String secCode) {
      this.secCode = secCode;
   }
}
