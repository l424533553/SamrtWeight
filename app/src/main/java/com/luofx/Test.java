package com.luofx;


import java.util.Objects;

/**
 * 作者：罗发新
 * 时间：2018/12/21 0021    16:23
 * 邮件：424533553@qq.com
 * 说明：
 */
public class Test {

    public static void main(String[] args) {
        byte[]  by=test11(14);
        System.out.println(new String(by));


//        OrderBean y1 = new OrderBean();
//        y1.setGoodsName("测试商品一");
//        OrderBean y2 = new OrderBean();
//        y2.setGoodsName("测试商品二");
//        OrderBean y3 = new OrderBean();
//        y3.setGoodsName("测试商品三");
//
//        OrderInfo ty = new OrderInfo();
//        ty.getList().add(y1);
//        ty.getList().add(y2);
//        ty.getList().add(y3);
//        String tt = JSON.toJSONString(ty);
//        System.out.print(tt);




//        testTest();
//        ArrayList<Student> list = new ArrayList<>();
//        Student a = new Student("fafa ", 134);
//        Student b = new Student("f65464564 ", 34);
//
//        list.add(a);
//        list.add(b);
//        System.out.println(Arrays.toString(list.toArray()));
//        a.setAge(8888);
//        System.out.println(Arrays.toString(list.toArray()));
        testTest();
    }

    /**
     * 将一个字节转成3个字节
     * @param sum  校验和
     */
    private static byte[] test11(int sum){
        byte[] by={48,48,48};
        int a;
        int b;
        int c;
        if(sum>=100){
            a=sum/100+48;
            c=sum%10+48;
            b=(sum>>1)%10+48;
            by[0]=(byte) a;
            by[1]=(byte) b;
            by[2]=(byte) c;
        }else if(sum>=10){
            b=(byte) (sum/10+48);
            c=(byte) (sum%10+48);
            by[1]=(byte) b;
            by[2]=(byte) c;
        }else{
            c=(byte) (sum%10+48);
            by[2]=(byte) c;
        }
        return by;
    }

    /**
     * 使用数据功能，数据功能
     */
    private static void testTest() {
        // 使用功能
        Student stu = new Student("你大爷", 14);
        Student stu1 = new Student("你大爷", 14);
        Student stu2 = new Student("你大爷", 14);

        System.out.println(Objects.equals(stu, stu1));
        // 测试功能结果
        System.out.println(stu1.equals(stu2));
    }
}
