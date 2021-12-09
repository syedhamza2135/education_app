package com.hamza.app.ocr;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.hamza.app.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityOcr extends AppCompatActivity implements View.OnClickListener {

    private Bitmap myBitmap;
    private ImageView myImageView;
    private TextView myTextView;
    public static final int WRITE_STORAGE = 100;
    public static final int SELECT_PHOTO = 1022;
    private static final int CAMERA_REQUEST = 1888;
    public File photo;
    Context context = this;


    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;


    String currentPhotoPath;

    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ocr);

        myTextView = findViewById(R.id.textView);
        myImageView = findViewById(R.id.imageView);
        findViewById(R.id.checkText).setOnClickListener(this);
        findViewById(R.id.select_image).setOnClickListener(this);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK ) {
//                        if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
                            // There are no request codes
//                            Intent data = result.getData();
//                            doSomeOperations();
                            Toast.makeText(context, "here activity result", Toast.LENGTH_SHORT).show();

                            File f = new File(currentPhotoPath);
//                    myImageView.setImageURI(Uri.fromFile(f));
//                           myBitmap = (Bitmap) data.getExtras().get("data");

                            String filePath = f.getPath();

                           myBitmap = BitmapFactory.decodeFile(filePath);
                            Uri urii = Uri.fromFile(f);
//                            myImageView.setImageBitmap(myBitmap);
                            try {
                               myBitmap= getBitmapFormUri(MainActivityOcr.this,urii  )  ;
                                myImageView.setImageBitmap(myBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.e("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
//                            myImageView.setImageBitmap(myBitmap);

                        }
                    }
                });

    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //Image resolution is based on 480x800
        float hh = 800f;//The height is set as 800f here
        float ww = 480f;//Set the width here to 480f
        //Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
        int be = 1;//be=1 means no scaling
        if (originalWidth > originalHeight && originalWidth > ww) {//If the width is large, scale according to the fixed size of the width
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//If the height is high, scale according to the fixed size of the width
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //Proportional compression
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//Set scaling
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//Mass compression again
    }


    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//Quality compression method, here 100 means no compression, store the compressed data in the BIOS
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //Cycle to determine if the compressed image is greater than 100kb, greater than continue compression
            baos.reset();//Reset the BIOS to clear it
            //First parameter: picture format, second parameter: picture quality, 100 is the highest, 0 is the worst, third parameter: save the compressed data stream
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//Here, the compression options are used to store the compressed data in the BIOS
            options -= 10;//10 less each time
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//Store the compressed data in ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//Generate image from ByteArrayInputStream data
        return bitmap;
    }


    public void openSomeActivityForResult() {
//        Intent intent = new Intent(this, SomeActivity.class);
//        someActivityResultLauncher.launch(intent);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
//                Toast.makeText(context, "ok1", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Toast.makeText(context, "ok2", Toast.LENGTH_SHORT).show();
                Uri photoURI = FileProvider.getUriForFile(this,
                        context.getApplicationContext()
                                .getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                someActivityResultLauncher.launch(takePictureIntent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkText:
                if (myBitmap != null) {
                    runTextRecognition();
                    Log.e("check", "bitmap not null");
                }
                break;
            case R.id.select_image:
                checkPermission(WRITE_STORAGE);


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("onActi", "ok");
            switch (requestCode) {
                case WRITE_STORAGE:
                    checkPermission(requestCode);
                    break;
                case SELECT_PHOTO:
                    Log.e("onActi", "ok2");
                    Uri dataUri = data.getData();
                    Log.e("onActi", "ok3" + dataUri);
                    String path = CommonUtils.getPath(this, dataUri);
                    if (path == null) {
                        myBitmap = CommonUtils.resizePhoto(photo, this, dataUri, myImageView);
                    } else {
                        myBitmap = CommonUtils.resizePhoto(photo, path, myImageView);
                    }
                    if (myBitmap != null) {
                        myTextView.setText(null);
                        myImageView.setImageBitmap(myBitmap);
                    }
                    break;

                case CAMERA_REQUEST_CODE:
                    if (data != null) {
                        File f = new File(currentPhotoPath);
//                    myImageView.setImageURI(Uri.fromFile(f));
                        myBitmap = (Bitmap) data.getExtras().get("data");
                        Uri urii = Uri.fromFile(f);
                        if (currentPhotoPath == null) {
                            myBitmap = CommonUtils.resizePhoto(photo, this, urii, myImageView);
                        } else {
                            myBitmap = CommonUtils.resizePhoto(photo, currentPhotoPath, myImageView);
                        }
                        Log.e("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                        myImageView.setImageBitmap(myBitmap);
                    }
//                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri contentUri = Uri.fromFile(f);
//                    mediaScanIntent.setData(contentUri);
//                    this.sendBroadcast(mediaScanIntent);
//                    myBitmap = CommonUtils.resizePhoto(photo, this, urii, myImageView);
                    break;

            }
        }
    }

    private void runTextRecognition() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(myBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processExtractedText(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure
                    (@NonNull Exception exception) {
                Toast.makeText(MainActivityOcr.this,
                        "Exception", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void Take_Image() {

        new AlertDialog.Builder(MainActivityOcr.this).
                setTitle("Choose Photo")
                .setMessage("Select an Option to Image Recognition:")
                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        selectPicture();
                        askCameraPermissions();
                    }
                })

                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        photo = CommonUtils.createTempFile(photo);
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //Start an Activity where the user can choose an image//
                        startActivityForResult(intent, SELECT_PHOTO);
                    }
                })

                .show();
    }


    private void processExtractedText(FirebaseVisionText firebaseVisionText) {
        myTextView.setText(null);
        if (firebaseVisionText.getBlocks().size() == 0) {
            myTextView.setText(R.string.no_text);
            return;
        }
        for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
            myTextView.append(block.getText());

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_STORAGE:

                //If the permission request is granted, then...//
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //...call selectPicture//
//                    selectPicture();
                    Take_Image();
                    //If the permission request is denied, then...//
                } else {
                    //...display the “permission_request” string//
                    requestPermission(this, requestCode, R.string.permission_request);
                }
                break;

            case CAMERA_PERM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //Display the permission request dialog//
    public static void requestPermission(final Activity activity, final int requestCode, int msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(msg);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent permissonIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                permissonIntent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(permissonIntent, requestCode);
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    //Check whether the user has granted the WRITE_STORAGE permission//

//    private void selectPicture() {
//        photo = CommonUtils.createTempFile(photo);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        //Start an Activity where the user can choose an image//
//        startActivityForResult(intent, SELECT_PHOTO);
//    }


    private void selectPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
//            File photoFile = null;
            //                photoFile = createImageFile();
            photo = CommonUtils.createTempFile(photo);
            // Continue only if the File was successfully created
            if (photo != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        context.getApplicationContext()
                                .getPackageName() + ".provider",
                        photo);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);

            }
        }
    }

    public void checkPermission(int requestCode) {
        switch (requestCode) {
            case WRITE_STORAGE:
                int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                //If we have access to external storage...//
                if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                    //...call selectPicture, which launches an Activity where the user can select an image//
//                    selectPicture();

                    Take_Image();

                    //If permission hasn’t been granted, then...//
                } else {
                    //...request the permission//
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                }
                break;

        }
    }

    private void selectPictures() {
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("ddMMyy-hhmmss");
        photo = CommonUtils.createTempFile(photo);
        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + dateFormat3.format(new Date()) + ".jpg";
        File imageFile = new File(imageFilePath);
        Uri imageUri = Uri.fromFile(imageFile);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri apkURI = FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", imageFile);
        intent.setDataAndType(apkURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //Start an Activity where the user can choose an image//
//        startActivityForResult(intent, SELECT_PHOTO);
        context.startActivity(intent);
    }


    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
//            dispatchTakePictureIntent();
            openSomeActivityForResult();
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == CAMERA_PERM_CODE){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                dispatchTakePictureIntent();
//            }else {
//                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                File f = new File(currentPhotoPath);
//                myImageView.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
//
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//            }
//
//        }
//
//        if (requestCode == GALLERY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri contentUri = data.getData();
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
//                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                myImageView.setImageURI(contentUri);
//            }
//
//        }
//
//
//    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
//
                        context.getApplicationContext()
                                .getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}