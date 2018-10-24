package com.axecom.smartweight.my.entity.scalescategory;

import java.io.Serializable;
import java.util.List;

/**
 * author: luofaxin
 * date： 2018/9/26 0026.
 * email:424533553@qq.com
 * describe:
 */
public class CategoryGoods implements Serializable {

    public int id;
    public String name;
    public List<child> child;

    public class child implements Serializable {
        public int id;
        public String name;
        public int cid;
        public int traceable_code;
        public String price;
        public int is_default;
    }
}
