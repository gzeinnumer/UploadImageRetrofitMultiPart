package com.gzeinnumer.uploadimageretrofit;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gzeinnumer.uploadimageretrofit.adapter.AdapterRV;
import com.gzeinnumer.uploadimageretrofit.model.ImagesItem;
import com.gzeinnumer.uploadimageretrofit.model.ResponseApiModel;
import com.gzeinnumer.uploadimageretrofit.model.ResponseDelete;
import com.gzeinnumer.uploadimageretrofit.model.ResponseGambarNoImage;
import com.gzeinnumer.uploadimageretrofit.model.ResponseGetData;
import com.gzeinnumer.uploadimageretrofit.server.ApiServices;
import com.gzeinnumer.uploadimageretrofit.server.RetroServer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//todo 36. implemen onItemClick from adapter
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterRV.onItemClick {

    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    //todo 2. deklar
    private Button uploadNew;
    EditText namaDialog ;
    Button btnPilihGambar;
    ImageView gambarDialog;
    Button btnUpload;
    Dialog dialogNew;
    RecyclerView recyclerData;
    AdapterRV adapter;
    private List<ImagesItem> list;
    Dialog dialogUpdate;
    EditText idDialog ;
    Button btnUpdate;
    Button btnDelete;

    //for value
    private Uri filePath;
    private String path;
    private String isNewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo 3. find and onclick
        uploadNew= findViewById(R.id.upload_new);
        uploadNew.setOnClickListener(this);

        //todo 5.
        requestStoragePermission();

        //todo 20.
        recyclerData=findViewById(R.id.recyclerData);

        //todo 21.
        getAllData();
    }

    //todo 4.
    @Override
    public void onClick(View v) {
        if (v == uploadNew) {
            //todo 7.
            dialogInsert();
        }
    }

    //todo 6.
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //todo 8.
    private void dialogInsert() {
        dialogNew = new Dialog(MainActivity.this);
        dialogNew.setContentView(R.layout.dialog_new);
        dialogNew.setTitle("hay");
        dialogNew.setCancelable(true);
        dialogNew.setCanceledOnTouchOutside(false);

        namaDialog = dialogNew.findViewById(R.id.nama_dialognew);
        btnPilihGambar = dialogNew.findViewById(R.id.btn_pilihgambarnew);
        gambarDialog = dialogNew.findViewById(R.id.gambar_dialognew);
        btnUpload = dialogNew.findViewById(R.id.btn_upload);

        btnPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 9.
                showFileChooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 13.
                uploadMultipartNew(namaDialog.getText().toString().trim());
            }
        });
        dialogNew.show();
    }

    //todo 10.
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //todo 11.
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //todo 12.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            String[] imageprojection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(filePath,imageprojection,null,null,null);

            if (cursor != null) {
                cursor.moveToFirst();
                int indexImage = cursor.getColumnIndex(imageprojection[0]);
                path = cursor.getString(indexImage);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    gambarDialog.setImageBitmap(bitmap);
                    isNewImage = "new";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //todo 14.
    public void uploadMultipartNew(String editNama) {
        String name = editNama;

        String path = getPath(filePath);

//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        //todo 18.
        File imagefile = new File(path);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"),imagefile);
        RequestBody nameSent = RequestBody.create(MediaType.parse("text/plain"), name);

        //$_POST['image']
        String imagePost = "image";
        MultipartBody.Part partImage = MultipartBody.Part.createFormData(imagePost, imagefile.getName(),reqBody);

        //make json from postmant
        //17.1
        ApiServices api = RetroServer.getInstance();
        Call<ResponseApiModel> upload = api.uploadImage(partImage, nameSent);
        upload.enqueue(new Callback<ResponseApiModel>() {
            @Override
            public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                Log.d("RETRO", "ON RESPONSE  : " + response.body().toString());

                if(!response.body().isError()) {
                    Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                Toast.makeText(MainActivity.this, "test "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //todo 22.
    private void getAllData() {
        //todo 23. get json
        //todo 25.
        RetroServer.getInstance().getAllImage().enqueue(new Callback<ResponseGetData>() {
            @Override
            public void onResponse(Call<ResponseGetData> call, Response<ResponseGetData> response) {
                boolean sukses = response.body().isSukses();
                list = response.body().getImages();
                if (sukses) {
                    Toast.makeText(getApplicationContext(), "Terhubung nih!!", Toast.LENGTH_SHORT).show();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetData> call, Throwable t) {

            }
        });
    }

    //todo 26.
    private void initData() {
        //todo 27.
        adapter = new AdapterRV(getApplicationContext(), list);
        recyclerData.setLayoutManager(new LinearLayoutManager(this));
        recyclerData.setHasFixedSize(true);
        recyclerData.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //todo 35.
        adapter.setOnClickListener2(MainActivity.this);
    }

    //todo 38.
    @Override
    public void onItemClick(final int position) {
        dialogUpdate = new Dialog(MainActivity.this);
        dialogUpdate.setContentView(R.layout.dialog_update);
        dialogUpdate.setTitle("hay");
        dialogUpdate.setCancelable(true);
        dialogUpdate.setCanceledOnTouchOutside(false);

        idDialog = dialogUpdate.findViewById(R.id.id_dialog);
        namaDialog = dialogUpdate.findViewById(R.id.nama_dialog);
        btnPilihGambar = dialogUpdate.findViewById(R.id.btn_pilihgambar);
        gambarDialog = dialogUpdate.findViewById(R.id.gambar_dialog);
        btnUpdate = dialogUpdate.findViewById(R.id.btn_update);
        btnDelete = dialogUpdate.findViewById(R.id.btn_delete);

        idDialog.setText(list.get(position).getId());
        namaDialog.setText(list.get(position).getName());
        Picasso.get().load(Constants.BASE_IMAGE_URL + list.get(position).getUrl())
                .into(gambarDialog);

        isNewImage="old";

        btnPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 39.
                showFileChooser();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 50.
                deleteDataFromDataBase(position);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 40.
                uploadMultipartUpdate(position);
            }
        });


        dialogUpdate.show();
    }

    //todo 41.
    public void uploadMultipartUpdate(int position) {
        //getting name for the image
        String nameNew = namaDialog.getText().toString().trim();

        //getting the actual path of the image
        String pathNew = "";
        if (isNewImage.equals("new")){
            pathNew = getPath(filePath);
        } else {

        }

        //Uploading code
        //todo 42.
        if (isNewImage.equals("new")) {
            nameNew = namaDialog.getText().toString().trim();

            pathNew = getPath(filePath);

//            Toast.makeText(this, nameNew, Toast.LENGTH_SHORT).show();
            File imagefile = new File(pathNew);
            //todo 43.
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"),imagefile);
            RequestBody nameSent = RequestBody.create(MediaType.parse("text/plain"), nameNew);
            RequestBody isNewImageSent = RequestBody.create(MediaType.parse("text/plain"), isNewImage);

            //$_POST['image']
            String imagePost = "image";
            MultipartBody.Part partImage = MultipartBody.Part.createFormData(imagePost, imagefile.getName(),reqBody);

            ApiServices api = RetroServer.getInstance();

            //todo 44. make json model from postmant
            //todo 46.
            Call<ResponseApiModel> upload = api.uploadImageUpdate(partImage, nameSent, isNewImageSent);
            upload.enqueue(new Callback<ResponseApiModel>() {
                @Override
                public void onResponse(Call<ResponseApiModel> call, Response<ResponseApiModel> response) {
                    Log.d("RETRO", "ON RESPONSE  : " + response.body().toString());

                    if(!response.body().isError()) {
                        Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Not Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseApiModel> call, Throwable t) {
                    Log.d("RETRO", "ON FAILURE : " + t.getMessage());
                    Toast.makeText(MainActivity.this, "test "+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //todo 47. get json
            //todo 49.
            RetroServer.getInstance().updateDataNoImageChanged(list.get(position).getId(),nameNew).enqueue(new Callback<ResponseGambarNoImage>() {
                @Override
                public void onResponse(Call<ResponseGambarNoImage> call, Response<ResponseGambarNoImage> response) {
                    boolean result = response.body().isSukses();
                    if (!result){
                        Toast.makeText(MainActivity.this, "Diupdate", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGambarNoImage> call, Throwable t) {

                }
            });
        }
    }

    //todo 51.
    private void deleteDataFromDataBase(int position) {
        //todo 52. make json
        //todo 54.
        RetroServer.getInstance().deleteData(list.get(position).getId()).enqueue(new Callback<ResponseDelete>() {
            @Override
            public void onResponse(Call<ResponseDelete> call, Response<ResponseDelete> response) {
                String result = response.body().getResult();
                if(result.equals("1")){
                    dialogUpdate.dismiss();
                    Toast.makeText(MainActivity.this, "Data Didelete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDelete> call, Throwable t) {

            }
        });
    }
}

