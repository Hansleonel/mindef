package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.mindef.gob.R;
import com.mindef.gob.utilities.Paths;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private int downloadValue;
    private String URL_PDF = "http://161.132.207.81/serciumd-be/api/sistradoc/link/445122";
    private String PATH_PDF;

    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        // PATH_PDF = Paths.getRootDirPath(getApplicationContext());

        PATH_PDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        progressBar = findViewById(R.id.pbV_pdf);

        getUserTokenShared();
        downloadPDF();
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void downloadPDF() {

        if (Status.RUNNING == PRDownloader.getStatus(downloadValue)) {
            PRDownloader.pause(downloadValue);
            return;
        }

        progressBar.setIndeterminate(true);

        if (Status.PAUSED == PRDownloader.getStatus(downloadValue)) {
            PRDownloader.resume(downloadValue);
            return;
        }

        downloadValue = PRDownloader.download(URL_PDF, PATH_PDF, "response.pdf")
                .setHeader("Authorization", "Bearer " + TOKENUSER)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        progressBar.setIndeterminate(false);
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("PDF Download", "onDownloadComplete: " + PATH_PDF);
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("PDF Download", "onError: " + error.getServerErrorMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
