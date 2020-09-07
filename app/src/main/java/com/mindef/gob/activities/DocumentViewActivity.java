package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mindef.gob.R;
import com.mindef.gob.utilities.Paths;

import java.io.File;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private PDFView pdfView;
    private FloatingActionButton fabViewer;

    private int downloadValue;
    private String linkDocument;
    private String URL_PDF = "http://161.132.207.81/serciumd-be/api/sistradoc/link/445122";
    private String PATH_PDF;

    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        progressBar = findViewById(R.id.pbV_pdf);
        pdfView = findViewById(R.id.pdfView);
        fabViewer = findViewById(R.id.fabViewer);

        // PATH_PDF = Paths.getRootDirPath(getApplicationContext());

        // TODO recordar corregir el link devuelto por el servidor
        linkDocument = getIntent().getStringExtra("codeDocument");

        PATH_PDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        getUserTokenShared();
        downloadPDF();

        fabViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFile();
            }
        });
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
                        progressBar.setVisibility(View.GONE);
                        showDocument();
                    }

                    @Override
                    public void onError(Error error) {
                        Log.d("PDF Download", "onError: " + error.getServerErrorMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), R.string.warning_pdf, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showDocument() {
        pdfView.fromUri(Uri.fromFile(new File(PATH_PDF + "/response.pdf")))
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeHorizontal(false)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onDrawAll(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Log.d("PDF Viewer", "onPageError: " + t.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.warning_pdf, Toast.LENGTH_LONG).show();
                    }
                })
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        return true;
                    }
                })
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        pdfView.fitToWidth();
                    }
                })
                .enableAnnotationRendering(true)
                .invalidPageColor(Color.WHITE)
                .load();
    }

    private void shareFile() {

    }
}
