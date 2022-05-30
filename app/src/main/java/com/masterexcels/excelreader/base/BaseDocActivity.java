package com.masterexcels.excelreader.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.wxiwei.office.R;
import com.wxiwei.office.constant.MainConstant;
import com.wxiwei.office.macro.DialogListener;
import com.wxiwei.office.officereader.beans.AToolsbar;
import com.wxiwei.office.officereader.database.DBService;
import com.wxiwei.office.res.ResKit;
import com.wxiwei.office.system.IControl;
import com.wxiwei.office.system.IMainFrame;
import com.wxiwei.office.system.MainControl;
import com.wxiwei.office.utils.RealPathUtil;

import java.io.File;
import java.util.List;


public abstract class BaseDocActivity extends AppCompatActivity implements IMainFrame {
    private MainControl control;
    private DBService dbService;
    private String filePath;
    private boolean isDispose;
    private boolean isThumbnail;
    private boolean writeLog = true;
    private final Object bg = -3355444;
    private FrameLayout appFrame;

    private final OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {
            pageChanged(page + 1, pageCount);
        }
    };
    private final OnErrorListener errorListener = new OnErrorListener() {
        @Override
        public void onError(Throwable t) {
            errorLoadPdf(t);
        }
    };
    private final OnLoadCompleteListener onLoadListener = new OnLoadCompleteListener() {
        @Override
        public void loadComplete(int nbPages) {
            onLoadComplete(nbPages);
        }
    };
    private final OnTapListener onTapListener = new OnTapListener() {
        @Override
        public boolean onTap(MotionEvent e) {
            return tap(e);
        }
    };

    @Override
    public void changePage() {
    }

    @Override
    public void changeZoom() {
    }

    @Override
    public void completeLayout() {
    }

    @Override
    public void error(int i) {
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public DialogListener getDialogListener() {
        return null;
    }

    @Override
    public byte getPageListViewMovingPosition() {
        return 0;
    }

    @Override
    public String getTXTDefaultEncode() {
        return "GBK";
    }

    @Override
    public int getTopBarHeight() {
        return 0;
    }

    @Override
    public byte getWordDefaultView() {
        return 0;
    }

    @Override
    public boolean isChangePage() {
        return true;
    }

    @Override
    public boolean isDrawPageNumber() {
        return false;
    }

    @Override
    public boolean isIgnoreOriginalSize() {
        return false;
    }

    @Override
    public boolean isPopUpErrorDlg() {
        return true;
    }

    @Override
    public boolean isShowFindDlg() {
        return true;
    }

    @Override
    public boolean isShowPasswordDlg() {
        return true;
    }

    @Override
    public boolean isShowProgressBar() {
        return true;
    }

    @Override
    public boolean isShowTXTEncodeDlg() {
        return true;
    }

    @Override
    public boolean isShowZoomingMsg() {
        return true;
    }

    @Override
    public boolean isTouchZoom() {
        return true;
    }

    @Override
    public boolean isZoomAfterLayoutForWord() {
        return true;
    }

    public void onCurrentPageChange() {
    }

    @Override
    public boolean onEventMethod(View view, MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, byte b) {
        return false;
    }

    public void onPagesCountChange() {
    }

    @Override
    public void setIgnoreOriginalSize(boolean z) {
    }

    @Override
    public void updateViewImages(List<Integer> list) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        appFrame = getFrameLayoutDoc();
        init();
        initView();
        addEvent();
    }

    protected abstract int getLayoutId();

    protected abstract FrameLayout getFrameLayoutDoc();

    protected abstract void initView();

    protected abstract void addEvent();


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (203 == i /*&& this.control.canBackLayout()*/) {
            finish();
            startActivity(getIntent());
        }
    }


    @Override
    public void onDestroy() {
        dispose();
        super.onDestroy();
    }

    @Override
    public void showProgressBar(boolean z) {
        setProgressBarIndeterminateVisibility(z);
    }

    private void init() {
        Intent intent = getIntent();
        this.dbService = new DBService(getApplicationContext());
        Uri fileUri;
        if ("android.intent.action.VIEW".equals(intent.getAction())) {
            Uri data = intent.getData();
            fileUri = data;
            if (data != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(" fileUri = ");
                sb.append(fileUri);
                this.filePath = RealPathUtil.getPathFromData(this, fileUri);
            }
        } else {
            this.filePath = intent.getStringExtra(MainConstant.INTENT_FILED_FILE_PATH);
        }
        if (filePath.toLowerCase().endsWith(".pdf")) {
            readPdfFile("");
            return;
        }
        this.control = new MainControl(this);

        if (TextUtils.isEmpty(this.filePath)) {
            this.filePath = intent.getDataString();
            int indexOf = getFilePath().indexOf(":");
            if (indexOf > 0) {
                this.filePath = this.filePath.substring(indexOf + 3);
            }
            this.filePath = Uri.decode(this.filePath);
        }
        if (!TextUtils.isEmpty(this.filePath) && this.filePath.contains("/raw:")) {
            String str = this.filePath;
            this.filePath = str.substring(str.indexOf("/raw:") + 5);
        }
        String fileName = new File(filePath).getName();

        this.control.openFile(this.filePath, fileName, Uri.fromFile(new File(filePath)));

    }

    public void readPdfFile(String pass) {
        PDFView pdfView = new PDFView(this);
        pdfView.fromFile(new File(filePath)).onPageChange(pageChangeListener)
                .onError(errorListener).onLoad(onLoadListener).password(pass).onTap(onTapListener).load();
        appFrame.addView(pdfView);
    }

    @Override
    public void setFindBackForwardState(boolean z) {
    }


    @Override
    public void updateToolsbarStatus() {
        if (!this.isDispose) {
            int childCount = appFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = appFrame.getChildAt(i);
                if (childAt instanceof AToolsbar) {
                    ((AToolsbar) childAt).updateStatus();
                }
            }
        }
    }


    public IControl getControl() {
        return this.control;
    }

    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public boolean doActionEvent(int actionID, Object obj) {
        return false;
    }

    @Override
    public void openFileFinish() {
        appFrame.addView(this.control.getView(), new LinearLayout.LayoutParams(-1, -1));
    }

    @Override
    public int getBottomBarHeight() {
        return 0;
    }

    @Override
    public String getAppName() {
        return getString(R.string.app_name);
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    @Override
    public void fullScreen(boolean z) {
    }

    public void destroyEngine() {
        super.onBackPressed();
    }

    @Override
    public String getLocalString(String str) {
        return ResKit.instance().getLocalString(str);
    }

    @Override
    public void setWriteLog(boolean z) {
        this.writeLog = z;
    }

    @Override
    public boolean isWriteLog() {
        return this.writeLog;
    }

    @Override
    public void setThumbnail(boolean z) {
        this.isThumbnail = z;
    }

    @Override
    public Object getViewBackground() {
        return bg;
    }

    @Override
    public boolean isThumbnail() {
        return this.isThumbnail;
    }

    @Override
    public File getTemporaryDirectory() {
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir;
        }
        return getFilesDir();
    }

    @Override
    public void dispose() {
        this.isDispose = true;
        MainControl mainControl = this.control;
        if (mainControl != null) {
            mainControl.dispose();
            this.control = null;
        }
        DBService dBService = this.dbService;
        if (dBService != null) {
            dBService.dispose();
            this.dbService = null;
        }
        int childCount = appFrame.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = appFrame.getChildAt(i);
            if (childAt instanceof AToolsbar) {
                ((AToolsbar) childAt).dispose();
            }
        }
    }


    @Override
    public void onWordScrollPercentY(float scrollY) {

    }

    @Override
    public void error(Throwable t) {

    }

    @Override
    public void onLoadComplete(int nbPages) {

    }

    @Override
    public boolean tap(MotionEvent e) {
        return false;
    }

    @Override
    public void showDialogLoading() {

    }

    @Override
    public void dismissDialogLoading() {

    }

    @Override
    public long getTimeLoading() {
        return 1500;
    }
}
