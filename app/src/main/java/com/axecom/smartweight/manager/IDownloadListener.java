package com.axecom.smartweight.manager;

/**
 * time: 15/7/15
 * description:
 *
 * @author sunjianfei
 */

public interface IDownloadListener {
    void onDownloadStarted();

    void onDownloadFinished(DownloadResult result);

    void onProgressUpdate(Float... value);
}

