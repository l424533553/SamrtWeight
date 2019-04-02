//package com.axecom.smartweight.my.activity;
//
//import java.io.Console;
//
///**
// * 作者：罗发新
// * 时间：2019/2/13 0013    9:06
// * 邮件：424533553@qq.com
// * 说明：
// */
//public class THHH {
//
//
//    String authenCode = "fpms_vender_axb";//签到接口参数1
//    String password = "h79OpV3MtCfiZHcu";//签到接口参数2
//    String mainKey = "F7AD4703F4520AFDB0216339";
//    String appCode = "FPMSWS";
//
//    /*4.调用接口的例子：http://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=xxxxxxxxxxxxx*/
//    /*data数据整体采用3des加密传输（主密钥），其中cmd接口名也采用3des加密传输（数据密钥），加密方式及密钥见附表。
//data数据加密前的格式为：
//service=服务名称&cmd=接口名&参数=值…….&参数=值
//data数据加密后的格式为：
//data=508E58F8E1A67E45A32D1ACE545EBDE613EFBB63D6F2DFF592DE1E11……
//注：上传十六进制（大写）*/
//
//    private String Get(String service, String cmd, System.Collections.Specialized.NameValueCollection paramlist) {
//        String url = "http://fpms.chinaap.com/admin/trade?executor=http&appCode=FPMSWS&data=" +
//                GetDataBody(service, cmd, paramlist, true);
//        Console.WriteLine(url);
//
//        //svc.Headers.Add("content-type", "application/x-www-form-urlencoded");
//        // Console.WriteLine(svc.UploadString("http://fpms.chinaap.com/admin/trade",
//        //     "executor=http&appCode=FPMSWS&data=" + GetDataBody(service, cmd, paramlist, true).ToUpper()));
//        String resp = svc.DownloadString(url);
//        Console.WriteLine(resp);
//        return resp;
//    }
//
//
//
//    //使用方法名
//    //encode:是否返回加密数据
//    private void GetDataBody(String service, String cmd) {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("service=" + service);
//        if (cmd != "login")//验证账号接口名称不需要加密，需要加密的填充方式一定要是PKCS7
//        {
//            if (String.IsNullOrEmpty(dataKey)) {
//                Console.WriteLine("datakey为空");
//                return "";
//            }
//        } else {
//            sb.Append("&cmd=" + cmd);// + HexString(System.Text.Encoding.UTF8.GetBytes(cmd)));6C6F67696E
//        }
//
//
//
//
//
//    }
//
//}
//
