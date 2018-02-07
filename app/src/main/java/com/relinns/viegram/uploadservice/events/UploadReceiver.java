package com.relinns.viegram.uploadservice.events;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.R;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

/**
 * This implementation is empty on purpose, just to show how it's possible to intercept
 * all the upload events app-wise with a global broadcast receiver registered in the manifest.
 *
 * @author Aleksandar Gotev
 */

public class UploadReceiver extends UploadServiceBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        super.onProgress(context, uploadInfo);
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        super.onError(context, uploadInfo, serverResponse, exception);
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        super.onCompleted(context, uploadInfo, serverResponse);
        Toast.makeText(context,context.getResources().getString(R.string.upload_file_done),Toast.LENGTH_LONG).show();
        if (WeakInstanceClass.getInstance().getReference()!=null){
            try {
                ((Timeline) WeakInstanceClass.getInstance().getReference().get()).onRefresh();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        super.onCancelled(context, uploadInfo);
    }
}
