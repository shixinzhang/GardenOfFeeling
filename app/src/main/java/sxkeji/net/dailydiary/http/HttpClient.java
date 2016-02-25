package sxkeji.net.dailydiary.http;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sxkeji.net.dailydiary.R;
import sxkeji.net.dailydiary.http.coreprogress.helper.ProgressHelper;
import sxkeji.net.dailydiary.http.coreprogress.listener.impl.UIProgressListener;
import sxkeji.net.dailydiary.utils.LogUtils;
import sxkeji.net.dailydiary.utils.NetWorkUtils;
import sxkeji.net.dailydiary.utils.UIUtils;

/**
 * Created by tiansj on 15/2/27.
 */
public class HttpClient {
    static volatile HttpClient singleton = null;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private String UTF_8 = "UTF-8";
    private MediaType MEDIA_TYPE = MediaType.parse("text/plain;");
    private OkHttpClient client = new OkHttpClient();
    private boolean isShowProgressDialog = true;
    private Context context;
    private Dialog dialog;
    private LoadingPage loadingPage;
    private View successedView;


    public HttpClient(Context context) {
        this.context = context;
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        initDialog();

    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.progressDialog);
        dialog = builder.create();
        dialog.setCancelable(false);
    }

    private void dialogShow() {
        dialog.show();
        dialog.getWindow().setContentView(R.layout.loading_view);
        dialog.findViewById(R.id.loadviewLinear).setVisibility(View.VISIBLE);
    }

    private void dialogCancle() {
        dialog.cancel();
    }

    public static HttpClient builder(Context context) {
//        if (singleton == null) {
//            synchronized (HttpClient.class) {
//                if (singleton == null) {
//                    singleton = new HttpClient(context);
//                }
//            }
//        }
        synchronized (HttpClient.class) {
            singleton = new HttpClient(context);
        }
        return singleton;
    }

    /*
    * 控制ProgressDialog的显示 默认是显示的，设置isShow为false 可以取消显示
    * */
    public HttpClient showProgress(boolean isShow) {
        this.isShowProgressDialog = isShow;
        return this;
    }

    /***
     * loadPage  就是LoadingPage
     * successedView 就是成功显示界面
     */
    public HttpClient setLoadingPage(LoadingPage loadPage, View successedView) {
        this.loadingPage = loadPage;
        this.successedView = successedView;
        this.isShowProgressDialog = false;
        return this;
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            Log.v("ConnectivityManager", e.getMessage());
        }
        return false;
    }

    public void get(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler) {
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }
            url = url + "?" + mapUrl(map);
            LogUtils.e("url++++++++++++" + url);
            httpResponseHandler.setLoadingPage(loadingPage, successedView);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            if (null != loadingPage) {
                loadingPage.setmState(LoadingPage.STATE_NONETWORK);
                loadingPage.showPage();
            } else {
                UIUtils.showToastSafe(context, UIUtils.getString(context, R.string.network_no));
            }
        }
    }

    public void post(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler) {
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }
            LogUtils.e("url++++++" + url);


            httpResponseHandler.setLoadingPage(loadingPage, successedView);
            FormEncodingBuilder builder = new FormEncodingBuilder();
            mapBuilder(map, builder);
            Request request = new Request.Builder().url(url).post(builder.build()).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            if (null != loadingPage) {
                loadingPage.setmState(LoadingPage.STATE_NONETWORK);
                loadingPage.showPage();
            } else {
                UIUtils.showToastSafe(context, UIUtils.getString(context, R.string.network_no));
            }

        }

    }

    public void postImage(String url, Map<String, String> map, final HttpResponseHandler httpResponseHandler, UIProgressListener uiProgressRequestListener) {
        RequestBody requestBody = null;
        if (NetWorkUtils.isNetworkAvailable(context)) {


            if (isShowProgressDialog) {
                dialogShow();
            }


            File uploadFile = new File(map.get("imageFile"));
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart("moduleName", map.get("moduleName"))
                    .addFormDataPart("token", map.get("token"))
                    .addFormDataPart("clientVersion", "1.0.0")

                    .addFormDataPart("imageFile", "image.png", RequestBody.create(MEDIA_TYPE_PNG, uploadFile))

                    .build();


            Request request = new Request.Builder()
                    .url(url)
//                .addHeader("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
                    .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendSuccessMessage(response);
                }

                @Override
                public void onFailure(Request request, IOException e) {
                    if (isShowProgressDialog) {
                        dialogCancle();
                    }
                    httpResponseHandler.sendFailureMessage(request, e);
                }
            });
        } else {

            if (null != loadingPage) {
                loadingPage.setmState(LoadingPage.STATE_NONETWORK);
                loadingPage.showPage();
            } else {
                UIUtils.showToastSafe(context, UIUtils.getString(context, R.string.network_no));
            }

        }

    }


    private void mapBuilder(Map<String, String> map, FormEncodingBuilder builder) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
            LogUtils.e("m.getKey()+++++" + entry.getKey() + " " + entry.getValue());
        }
    }

    private String mapUrl(Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append("=");
            buffer.append(entry.getValue());
            buffer.append("&");
        }
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }


}
