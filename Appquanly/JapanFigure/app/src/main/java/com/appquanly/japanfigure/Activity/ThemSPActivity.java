package com.appquanly.japanfigure.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.appquanly.japanfigure.Model.MessageModel;
import com.appquanly.japanfigure.Model.SanPhamMoi;
import com.appquanly.japanfigure.R;
import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.appquanly.japanfigure.Retrofit.RetrofitClient;
import com.appquanly.japanfigure.databinding.ActivityThemSpactivityBinding;
import com.appquanly.japanfigure.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Callback;

import com.appquanly.japanfigure.Retrofit.ApiBanHang;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class ThemSPActivity extends AppCompatActivity {

    Spinner spinner;
    Toolbar toolbar;
    int loai = 0 ;
    ActivityThemSpactivityBinding binding;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    String mediaPath;
    String ten = "";
    SanPhamMoi sua ;
    Boolean flag= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        binding = ActivityThemSpactivityBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        ten = getIntent().getStringExtra("ten");
        sua = (SanPhamMoi) getIntent().getSerializableExtra("sua");
        initView();
        initData();
        ActionBar();


        if(sua == null){
            flag = false;
        }else {
            flag = true;
            binding.btnthem.setText("Sửa sản phẩm");
            // show data
            binding.tensp.setText(sua.getTensp());
            binding.mota.setText(sua.getMota());
            binding.giasp.setText(sua.getGia());
            binding.hinhanh.setText(sua.getHinhanh());
            binding.slsp.setText(sua.getSltonkho()+"");
            binding.spinnerLoai.setSelection(sua.getLoai());

        }


    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ten);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        stringList.add("Loai 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == false){
                    themSp();
                }else {
                    suaSanPham();
                }
            }
        });
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }
    private void suaSanPham() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_sl = binding.slsp.getText().toString();
        if(TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) ||
                TextUtils.isEmpty(str_sl) || TextUtils.isEmpty(str_gia) || loai == 0){
            Toast.makeText(getApplicationContext() ,"Vui lòng nhập đầy đủ thông tin! ",Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.updateSp(str_ten,str_gia,str_hinhanh,loai,str_mota,sua.getId(), Integer.parseInt(str_sl))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                    ));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log","onActivityResult" +mediaPath);
    }

    private void themSp() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_sl = binding.slsp.getText().toString();
        if(TextUtils.isEmpty(str_sl) || TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || TextUtils.isEmpty(str_gia) || loai == 0){
            Toast.makeText(getApplicationContext() ,"Vui lòng nhập đầy đủ thông tin! ",Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.insertSp(str_ten,str_gia,str_hinhanh,loai,str_mota, Integer.parseInt(str_sl))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                                    binding.tensp.setText("");
                                    binding.giasp.setText("");
                                    binding.mota.setText("");
                                    binding.hinhanh.setText("");
                                    binding.slsp.setText("");
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),messageModel.getMessage() ,Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                    ));
        }

    }
    private String getMediaPath(Uri uri){
        String result;
        Cursor cursor  = getContentResolver().query(uri,null,null,null,null);
        if (cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
// upload hình ảnh
private void uploadMultipleFiles() {
    Uri uri = Uri.parse(mediaPath);
    File file = new File(getMediaPath(uri));
    RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
    MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
    retrofit2.Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
    call.enqueue(new Callback<MessageModel>() {
        @Override
        public void onResponse(retrofit2.Call<MessageModel> call, retrofit2.Response<MessageModel> response) {
            MessageModel serverResponse = response.body();
            if (serverResponse != null) {
                if (serverResponse.isSuccess()) {
                    binding.hinhanh.setText(serverResponse.getName());

                } else {
                    Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.v("Response", serverResponse.toString());
            }
        }
        @Override
        public void onFailure(retrofit2.Call<MessageModel> call, Throwable t) {
            Log.d("Response", t.toString());
        }
    });
}
    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}