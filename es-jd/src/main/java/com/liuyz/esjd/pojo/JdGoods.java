package com.liuyz.esjd.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/7/16
 * @description 京东商品对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JdGoods {

    private String name;
    private String price;
    private String src;

}
