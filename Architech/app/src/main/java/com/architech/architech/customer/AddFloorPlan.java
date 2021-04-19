package com.architech.architech.customer;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.architech.architech.NetworkConfigurations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.architech.architech.R;
import com.architech.architech.contractor.MainPageContractor;
import com.architech.architech.contractor.SignupContractor;
import com.architech.architech.model.Customer;
import com.architech.architech.model.FloorPlan;
import com.architech.architech.model.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddFloorPlan extends AppCompatActivity {
    Customer myProfile;
    Button addFloorplanButton;
    EditText title,length,width,bedroom,bathrooms,carsCapacity;
    ImageButton imageButton;
    TextView selectImageText;
    ProgressBar progressBar;
    LinearLayout backButton;
    ImageView imageView;

    private FirebaseAuth mAuth;
    String uid;

    int croppedWidth, croppedLength;


    Uri uri;//for storing selected image
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_floor_plan);

        myProfile = getIntent().getParcelableExtra("MYPROFILE");
        Log.i("ADD_FLOORPLAN",myProfile.getName());


        imageButton=findViewById(R.id.image_addfloorplan);
        title=findViewById(R.id.title_addfloorplan);
        length=findViewById(R.id.length_addfloorplan);
        width=findViewById(R.id.width_addfloorplan);
        bedroom=findViewById(R.id.bedrooms_addfloorplan);
        bathrooms=findViewById(R.id.bathrooms_addfloorplan);
        carsCapacity=findViewById(R.id.car_capacity_addfloorplan);
        addFloorplanButton=findViewById(R.id.addFloorPlanButton);

        imageView = findViewById(R.id.imageViewAddFloorPlan);
        backButton= findViewById(R.id.backButtonAddFloorPlan);



        selectImageText= findViewById(R.id.selectImageText);
        progressBar= findViewById(R.id.progressBarAddFloorPlan);

        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            uid = mAuth.getCurrentUser().getUid();
        }
        else{
            uid=myProfile.getUid();
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });


        addFloorplanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String titleString= title.getText().toString().trim();
                String widthString= width.getText().toString().trim();
                String lengthString= length.getText().toString().trim();
                String carsCapacityString= carsCapacity.getText().toString().trim();
                String bathroomsString= bathrooms.getText().toString().trim();
                String bedroomString= bedroom.getText().toString().trim();


                if (uri == null) {
                    Toast.makeText(AddFloorPlan.this, "Add an image", Toast.LENGTH_SHORT).show();
                }
                else if (titleString.isEmpty())
                {
                    title.setError("Set title");
                    title.requestFocus();
                }
                else if (lengthString.isEmpty())
                {
                    length.setError("Enter length");
                    length.requestFocus();
                }
                else if (widthString.isEmpty())
                {
                    width.setError("Enter width");
                    width.requestFocus();
                }
                else if (bedroomString.isEmpty())
                {
                    bedroom.setError("Enter no. of bedrooms");
                    bedroom.requestFocus();
                }
                else if (bathroomsString.isEmpty())
                {
                    bathrooms.setError("Enter number of bathrooms");
                    bathrooms.requestFocus();
                }
                else if (carsCapacityString.isEmpty())
                {
                    carsCapacity.setError("Add number of cars");
                    carsCapacity.requestFocus();
                }
                else{
                    hideKeyboard(AddFloorPlan.this);
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        connectServer(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    void connectServer(View v) throws IOException {

        String ipv4Address = NetworkConfigurations.getIpAddress();
        String portNumber = NetworkConfigurations.getPortNumber();

        String postUrl= "http://"+ipv4Address+":"+portNumber+"/predict";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
//      convert image to byte array, store its string value for sending
        byte[] byteArray = convertImageToByte(uri);

        File f = new File(uri.toString());
//        filename that is sent to server, to make it unique
        String imageName = FirebaseDatabase.getInstance().getReference().child("Floorplans").push().getKey();

//        request holds image(file), lenght and width
        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageName, RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                .addFormDataPart("length",length.getText().toString())
                .addFormDataPart("width",width.getText().toString())
                .build();

        postRequest(postUrl, postBodyImage, imageName);
    }

    public byte[] convertImageToByte(Uri uri){
//        just to convert image to bytes
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    void postRequest(String postUrl, RequestBody postBody, String key) {
        progressBar.setVisibility(View.VISIBLE);

//        create client with long timeouts. bandwith issues may cause longer wait times
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
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
                e.printStackTrace();
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddFloorPlan.this, "Failed to connect to server",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            //            when response received
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                System.out.println(response.body().toString());
                String costEstimate = "";
                String percentageCoveredArea = "";

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.getJSONArray("roomCounts").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("Status").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("percentageCoveredArea").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.get("costEstimate").toString());
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.getJSONArray("array").toString());
                    Log.i("RECEIVED_FROM_SERVER", String.valueOf(jsonObject.getJSONArray("array").length()));
                    Log.i("RECEIVED_FROM_SERVER",jsonObject.getJSONArray("array").getJSONArray(0).get(0).toString());

//                  converting received array to arraylist
                    JSONArray jsNumpyArray = jsonObject.getJSONArray("array");
                    ArrayList<ArrayList<Integer>> predictionArray = new ArrayList<ArrayList<Integer>>();
                    for(int i = 0;i<jsNumpyArray.length();i++){
                        JSONArray temp = jsNumpyArray.getJSONArray(i);
                        ArrayList<Integer> tempArr = new ArrayList<>();
                        for(int j = 0;j<temp.length();j++){
                            tempArr.add(temp.getInt(j));
                        }
                        predictionArray.add(tempArr);
                    }
                    Log.i("RECEIVED_FROM_SERVER", String.valueOf(predictionArray.size()));
                    Log.i("RECEIVED_FROM_SERVER", String.valueOf(predictionArray.get(0).size()));

//                    json array received in specific format, seperating individual components
                    costEstimate = jsonObject.get("costEstimate").toString();
                    percentageCoveredArea = jsonObject.get("percentageCoveredArea").toString();

                    JSONArray jsArray = jsonObject.getJSONArray("roomCounts");
                    ArrayList<String> roomCounts = new ArrayList<>();
                    ArrayList<String> roomTypes = new ArrayList<>();
                    for(int i = 0; i<jsArray.length();i++){
                        JSONArray temp = jsArray.getJSONArray(i);
                        roomCounts.add(temp.getString(1));
                        roomTypes.add(temp.getString(0));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addToDatabase(key,costEstimate,percentageCoveredArea);

            }
        });
    }

    void addToDatabase(String key, String costEstimate, String percentageCoveredArea){
//        String key = FirebaseDatabase.getInstance().getReference().child("Floorplans").push().getKey();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("Floorplans")
                .child(key);


        StorageReference st = FirebaseStorage.getInstance().getReference();
        st = st.child("Floorplan").child(key);
        st.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        FloorPlan newFloorplan = new FloorPlan(
                                title.getText().toString().trim(),
                                uid,
                                width.getText().toString().trim(),
                                length.getText().toString().trim(),
                                carsCapacity.getText().toString().trim(),
                                bathrooms.getText().toString().trim(),
                                bedroom.getText().toString().trim(),
                                key,
                                myProfile.getName(),
                                String.valueOf(croppedWidth),
                                String.valueOf(croppedLength),
                                downloadUrl,
                                percentageCoveredArea,
                                costEstimate
                        );
//                        imagePaths.add(dp);
                        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference()
                                .child("Floorplans")
                                .child(key);
                        database2.setValue(newFloorplan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent= new Intent(AddFloorPlan.this, FloorPlanDetails.class);
                                    intent.putExtra("FLOORPLAN",newFloorplan);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(AddFloorPlan.this, "Floorplan uploading failed! - 1", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFloorPlan.this, "Floorplan uploading failed! - 2", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddFloorPlan.this, "Image uploading failed!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        handles cropped image intents
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageButton.setVisibility(View.GONE);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                croppedLength = result.getBitmap().getHeight();
//                croppedWidth = result.getBitmap().getWidth();

                uri = result.getUri();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
                croppedLength = options.outHeight;
                croppedWidth = options.outWidth;

                imageView.setImageURI(uri);

                Toast.makeText(getApplicationContext(), "image selected", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




    //  starts crop activity
    public void selectImage(View v) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop the floor plan as closely as possible")
                .start(AddFloorPlan.this);
    }
    void hideKeyboard(Context c)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(c);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
