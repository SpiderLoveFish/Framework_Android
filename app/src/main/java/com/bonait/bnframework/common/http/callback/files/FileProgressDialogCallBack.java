package com.bonait.bnframework.common.http.callback.files;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.Formatter;

import com.bonait.bnframework.application.MainApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.Locale;

/**
 * Created by LY on 2019/4/15.
 * 带加载进度条的下载回调接口，默认OkGo文件回调
 */
public abstract class FileProgressDialogCallBack extends FileCallback {

    private ProgressDialog progressDialog;

    public FileProgressDialogCallBack(Context context) {
        super(null);
        initDialog(context);
    }

    public FileProgressDialogCallBack(Context context,String destFileName) {
        super(destFileName);
        initDialog(context);
    }

    public FileProgressDialogCallBack(Context context,String destFileDir, String destFileName) {
        super(destFileDir,destFileName);
        initDialog(context);
    }


    @Override
    public void onStart(Request<File, ? extends Request> request) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void downloadProgress(Progress progress) {
        String downloadLength = Formatter.formatFileSize(MainApplication.getContext(), progress.currentSize);
        String totalLength = Formatter.formatFileSize(MainApplication.getContext(), progress.totalSize);

        if (progress.totalSize>0) {
            progressDialog.setMax((int) progress.totalSize);
        }
        progressDialog.setProgress((int) (progress.fraction * progress.totalSize));
        progressDialog.setProgressNumberFormat(String.format(Locale.CHINA,"%sMb / %sMb",downloadLength,totalLength));
    }

    /**
     * 显示进度条对话框
     */
    private void initDialog(final Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在下载");


        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "停止下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkGo.getInstance().cancelTag(context);
                dialog.dismiss();
            }
        });

        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

}
