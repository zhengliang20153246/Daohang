package com.amap.navi.daohang.Xiangce;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.navi.daohang.Bianliang;
import com.amap.navi.daohang.MainActivity;
import com.amap.navi.daohang.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.leefeng.promptlibrary.PromptDialog;

 /*
 * Created by LI on 2017/11/3.
 */

public class AddxiangceActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    private ImageView picture1;
    private ImageView picture2;
    private ImageView picture3;

    private int flag=0;//判断是哪一个imageview：0第一个 1第二个
    private Uri imageUri1;
    private Uri imageUri2;
    private Uri imageUri3;
    private BmobFile img;
    private File file1;
    private File file2;
    private Spinner sp;
    private String leixing;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocation myLocation;
    private TextView address;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addxiangce);

        //Bmob.initialize(this, Bmob_APPID);//初始化Bmob
        // Bmob.initialize(this, "a7219d8ddbcd940c1b73012bd14eaefd");
        address=(TextView)findViewById(R.id.address);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        myLocation=amapLocation;
                        address.setText(myLocation.getAoiName());

                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Toast.makeText(AddxiangceActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                    }
                }

            }

        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);

        picture1 = (ImageView) findViewById(R.id.tupian) ;
        // picture2 = (ImageView) findViewById(R.id.bookimage2) ;
        /*
            imageview1点击事件**************
        */
        picture1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddxiangceActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddxiangceActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    flag = 0;//判断是哪一个imageview：0第一个 1第二个
                    //创建File对象，存储拍摄的照片
                    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri1 = FileProvider.getUriForFile(AddxiangceActivity.this, "com.amap.navi.daohang.ARprovider", outputImage);
                    } else {
                        imageUri1 = Uri.fromFile(outputImage);
                    }
                    //启动相机
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
                    startActivityForResult(intent, TAKE_PHOTO);

                }
            }
        });


        /*
            返回按钮事件**************
        */
        ImageButton addbookfanhui = (ImageButton) findViewById(R.id.addbookfanhui);
        addbookfanhui.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //finish();
                    Intent i = new Intent(AddxiangceActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    //Toast.makeText(getApplicationContext(), "返回成功！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        /*
            添加按钮事件
        */
        final Button addbook = (Button) findViewById(R.id.addbutton);
        addbook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //addbook.setBackgroundColor(Color.rgb(190, 190, 190));
                final String intr=((EditText)findViewById(R.id.intruduce)).getText().toString();
                if(file1!=null)
                {
                    //是否有没填项
                    if(intr.equals(""))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddxiangceActivity.this);
                        builder.setTitle("提示:");
                        builder.setIcon(R.drawable.youqingtishi);
                        builder.setMessage("请添加你的描述哦！");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                    }
                    else//信息全部填好，正常提交
                    {
                        final PromptDialog promptDialog = new PromptDialog(AddxiangceActivity.this);

                        final ProgressDialog progressDialog = new ProgressDialog(AddxiangceActivity.this);
                        progressDialog.setMessage("正在上传图片1...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        final BmobFile icon1 = new BmobFile(file1);
                        //final BmobFile icon2 = new BmobFile(file2);
                        icon1.upload(new UploadFileListener() {

                            @Override
                            public void onProgress(Integer arg0) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void done(BmobException e) {
                                Xiangce s=new Xiangce();

                                s.setIntruduce(intr);
                                s.setUserid(Bianliang.getUserID());
                                s.setTupian(icon1);
                                s.setLat(myLocation.getLatitude());
                                s.setLng(myLocation.getLongitude());
                                s.setAddress(myLocation.getAoiName());
                                s.save(new SaveListener<String>() {
                                    public void done(String objectId, BmobException e) {
                                        if(e==null)
                                        {
                                            //promptDialog.dismiss();
                                            //flag=0;
                                            progressDialog.dismiss();
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(AddxiangceActivity.this);
                                            builder2.setTitle("提示:");
                                            builder2.setIcon(R.drawable.success);
                                            builder2.setMessage("添加图书成功，是否继续添加图书？");
                                            builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(AddxiangceActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(AddxiangceActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder2.create().show();

                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            //promptDialog.showError("上传图书信息失败！");
                                            Toast.makeText(getApplicationContext(), "上传图书信息失败！"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
                else//确保图片
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddxiangceActivity.this);
                    builder.setTitle("提示:");
                    builder.setIcon(R.drawable.youqingtishi);
                    builder.setMessage("请添图片！");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.create().show();
                }


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //openAlbum();
                }else{
                    Toast.makeText(this,"你拒绝了权限访问！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        //显示拍摄的图片
                            Matrix matrix = new Matrix();
                            Bitmap bitmap0 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri1));

                            matrix.postRotate(90);//旋转照片
                            Toast.makeText(this,"jiaodu:"+readPictureDegree(imageUri1.getPath()),Toast.LENGTH_SHORT).show();

                            bitmap0 = Bitmap.createBitmap(bitmap0, 0, 0, bitmap0.getWidth(),bitmap0.getHeight(), matrix, true);
                            Bitmap  img0 = getScaledBitmap(bitmap0, 0.15f, 0.15f);//改变大小f(倍数)
                            Bitmap  img1 = getScaledBitmap(bitmap0, 0.3f, 0.3f);//改变大小f(倍数)
                            file1=compressImage(img1);//压缩图片img********************************
                            picture1 .setBackgroundResource(0);//清空背景
                            picture1.setImageBitmap(img0);//设置image*/


                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /*
        改变图片大小函数
    */
    public static Bitmap getScaledBitmap(Bitmap m_img, float sx, float sy) {
        Matrix matrix = new Matrix();

        matrix.postScale(sx, sy);
        Bitmap rst = Bitmap.createBitmap(m_img, 0, 0, m_img.getWidth(), m_img.getHeight(), matrix, true);
        return rst;
    }
    /*
        质量压缩
    */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //recycleBitmap(bitmap);//这个bitmap还有用不可回收
        return file;
    }
    //bitmap回收函数
    /*
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }
    */

    //复写返回键功能**************************
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddxiangceActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}