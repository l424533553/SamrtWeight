//package com.axecom.smartweight.carouselservice1.service;
//
//import android.content.Intent;
//import android.os.Build;
//
//import com.luofx.base.MyBaseApplication;
//
//
//public class MyApplication extends MyBaseApplication {
//
//  /**
//   * 广告横屏
//   */
//  public SecondScreen banner = null;
//
//  @Override
//  public void onCreate() {
//    super.onCreate();
//
////        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
////        //获取屏幕数量
////        Display[] presentationDisplays = displayManager.getDisplays();
////        if (presentationDisplays.length > 1) {
////            banner = new SecondScreen(this, presentationDisplays[1]);
////            banner.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
////            banner.show();
////        }


//    // 测试 功能
//    Intent intentService = new Intent(getApplicationContext(), CarouselService.class);
//    startService(intentService);
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      //版本必须大于5.0
//      startService(new Intent(getApplicationContext(), JobWakeUpService.class));
//    }
//
//
//
//
//
//  }
//}
