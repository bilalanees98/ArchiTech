package com.hyderabbasnaqvi.architech;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.net.InetAddresses;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUpload extends AppCompatActivity {

    ImageView inputImg;
    EditText ipAddress, floorPlanWidth, floorPlanLength;
    Button uploadButton;
    Button uploadToServerBtn;
    Button logout_imageUpload;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressBar spinner;

    final String PORT_NUMBER = "5000";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        logout_imageUpload=findViewById(R.id.logout_imageUpload);
        inputImg = findViewById(R.id.inputImg);
        ipAddress = findViewById(R.id.ipAddress);
        uploadButton = findViewById(R.id.uploadButton);
        uploadToServerBtn = findViewById(R.id.uploadToServerBtn);
        floorPlanLength = findViewById(R.id.floorPlanLength);
        floorPlanWidth = findViewById(R.id.floorPlanWidth);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);


        uploadToServerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String length = floorPlanLength.getText().toString();
                String width = floorPlanWidth.getText().toString();

                String ip = ipAddress.getText().toString();
                if(uri == null){
                    Toast.makeText(ImageUpload.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
                else if(length.isEmpty() || length.equals(null) || length.startsWith("-") || length.equals("0")){
                    floorPlanLength.setError("Incorrect length value");
                    floorPlanLength.requestFocus();
                }
                else if(width.isEmpty() || width.equals(null) || width.startsWith("-") || width.equals("0")){
                    floorPlanWidth.setError("Incorrect width value");
                    floorPlanWidth.requestFocus();
                }
                else if(ip.isEmpty() || ip.equals(null) || !validate(ip)){
                    ipAddress.setError("Incorrect IP value");
                    ipAddress.requestFocus();
                }
                else {

                    try {
                        connectServer(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        logout_imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent back=new Intent(ImageUpload.this,Login.class);
                startActivity(back);

            }
        });


    }

    void connectServer(View v) throws IOException {
        ipAddress = findViewById(R.id.ipAddress);
        String ipv4Address = ipAddress.getText().toString();

        String portNumber = PORT_NUMBER;

        String postUrl= "http://"+ipv4Address+":"+portNumber+"/predict";
        System.out.println(postUrl);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        byte[] byteArray = convertImageToByte(uri);
        File f = new File(uri.toString());
        String imageName = f.getName();



        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageName, RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .addFormDataPart("length",floorPlanLength.getText().toString())
                .addFormDataPart("width",floorPlanWidth.getText().toString())
                .build();


        postRequest(postUrl, postBodyImage);
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            System.out.println("in try -------------------------------------------");
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            System.out.println("in catch ----------------------------------------");
            e.printStackTrace();
        }
        return data;
    }

    void postRequest(String postUrl, RequestBody postBody) {
        spinner.setVisibility(View.VISIBLE);

//        OkHttpClient client = new OkHttpClient();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                System.out.println("on failure------------------------------");
                e.printStackTrace();
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    spinner.setVisibility(View.GONE);
                        TextView responseText = findViewById(R.id.responseText);
                        responseText.setText("Failed to Connect to Server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                System.out.println("on response------------------------------");
                System.out.println(response.body().toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.getJSONArray("roomCounts").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("Status").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("percentageCoveredArea").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("costEstimate").toString());

                    String costEstimate = jsonObject.get("costEstimate").toString();
                    JSONArray jsArray = jsonObject.getJSONArray("roomCounts");
                    ArrayList<String> roomCounts = new ArrayList<>();
                    ArrayList<String> roomTypes = new ArrayList<>();
                    for(int i = 0; i<jsArray.length();i++){
                        JSONArray temp = jsArray.getJSONArray(i);
                        roomCounts.add(temp.getString(1));
                        roomTypes.add(temp.getString(0));
                    }

                    Intent i = new Intent(ImageUpload.this, ModelDisplay.class);
                    i.putExtra("PRED_IMAGE", jsonObject.get("ImageBytes").toString());
                    i.putStringArrayListExtra("ROOM_COUNT", roomCounts);
                    i.putStringArrayListExtra("ROOM_TYPES", roomTypes);
                    i.putExtra("COST_ESTIMATE", costEstimate);
//                    Percentage Covered Area
                    i.putExtra("PCA", jsonObject.get("percentageCoveredArea").toString());
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        responseImg.setImageBitmap(finalDecodedBytes);
                        spinner.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    protected void onActivityResult(int reqCode, int resCode, Intent data) {
//        super.onActivityResult(reqCode, resCode, data);
//        if (resCode == RESULT_OK && data != null) {
//            uri = data.getData();
//            inputImg.setImageURI(uri);
//            Toast.makeText(getApplicationContext(), "image selected", Toast.LENGTH_LONG).show();
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                inputImg.setImageURI(uri);
                Toast.makeText(getApplicationContext(), "image selected", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void selectImage(View v) {
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 0);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(ImageUpload.this);
    }
    public static boolean validate(final String ip) {
//        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
//        return ip.matches(PATTERN);
        return InetAddresses.isInetAddress(ip);
    }
}