package peng.stream.quotes.module;

import lombok.Data;

/**
 * Create by peng on 2022/2/22.
 *
 * 证券价格对象：证券代码+实时价格
 */
@Data
public class SecPrice {

    //证券代码
    private String secCode;

    //证券价格
    private String secPrice;

    public SecPrice(String secCode, String secPrice){
        this.secCode = secCode;
        this.secPrice = secPrice;
    }

}
