package peng.stream.quotes;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import peng.stream.quotes.module.QuotePriceResult;
import peng.stream.quotes.module.SecPrice;
import peng.stream.quotes.module.SecPriceWithTime;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Create by peng on 2022/2/22.
 * <p>
 * 模拟流计算
 * 模拟场景：证券行情价格计算：当前的最高价、最低价、平均价格
 * 输入：证券代码+实时价格
 * 输出：过去一段时间拥有行情数据的证券列表以及相应的证券最高价、最低价、平均价格
 */
public class quotes {

    private static final Logger logger = LoggerFactory.getLogger(quotes.class);

    //时间窗口长度(秒)
    private static final long TIMELINES = 5L;
    //缓存过去一段时间的证券价格
    private static final Map<String, List<SecPriceWithTime>> secPriceTimeWindow = new HashMap<>();
    //计算结果
    private static final Map<String, QuotePriceResult> secQuotedResult = new HashMap<>();


    public static void main(String[] args) {

        logger.info("开始模拟生成行情信息：");
        for (int i = 0; i < 5; i++) {
            quotesCalculated(randomSecPrice());
        }
        getAllQuotedPrice().forEach((quotePriceResult -> logger.info(quotePriceResult.toString())));

        logger.info("开始模拟生成重复行情信息：");
        secQuotedResult.keySet().forEach(secCode -> {
            quotesCalculated(randomSecPriceBySecCode(secCode));
        });

        getAllQuotedPrice().forEach((quotePriceResult -> logger.info(quotePriceResult.toString())));

    }

    private static void quotesCalculated(SecPrice secPrice) {

        SecPriceWithTime secPriceWithTime = new SecPriceWithTime();
        secPriceWithTime.setSecCode(secPrice.getSecCode());
        secPriceWithTime.setSecPrice(secPrice.getSecPrice());
        secPriceWithTime.setTime(Instant.now());

        List<SecPriceWithTime> secPriceWithTimeList = new ArrayList<>();
        secPriceWithTimeList.add(secPriceWithTime);

        //合并新的数据
        secPriceTimeWindow.merge(secPrice.getSecCode(), secPriceWithTimeList, (secPrices, secPriceWithTimes) -> {
            secPrices.add(secPriceWithTime);
            return secPrices;
        });

        //过滤超时的券
        secPriceTimeWindow.put(secPrice.getSecCode(), secPriceTimeWindow.get(secPrice.getSecCode()).stream().filter((time) -> time.getTime().isAfter(Instant.now().minusSeconds(TIMELINES))).collect(Collectors.toList()));
        //获取统计平均值
        DoubleSummaryStatistics statistics = secPriceTimeWindow.get(secPrice.getSecCode()).stream().mapToDouble((sec) -> Double.parseDouble(sec.getSecPrice())).summaryStatistics();

        //缓存结构信息
        QuotePriceResult quotePriceResult = new QuotePriceResult();
        quotePriceResult.setAvgPrice(String.valueOf(statistics.getAverage()));
        quotePriceResult.setMaxPrice(String.valueOf(statistics.getMax()));
        quotePriceResult.setMinPrice(String.valueOf(statistics.getMin()));
        quotePriceResult.setSecCode(secPrice.getSecCode());
        quotePriceResult.setUpdateTime(Instant.now());

        secQuotedResult.put(secPrice.getSecCode(),quotePriceResult);
    }

    public static QuotePriceResult getQuotedPrice(String secCode) {
        return secQuotedResult.getOrDefault(secCode, new QuotePriceResult("no exist"));
    }

    public static Collection<QuotePriceResult> getAllQuotedPrice() {
        return secQuotedResult.values();
    }

    private static SecPrice randomSecPrice() {
        return new SecPrice(RandomUtil.randomNumbers(6), String.valueOf(RandomUtil.randomDouble(2, RoundingMode.CEILING)));
    }

    private static SecPrice randomSecPriceBySecCode(String secCode) {
        return new SecPrice(secCode, String.valueOf(RandomUtil.randomDouble(2, RoundingMode.CEILING)));
    }
}
