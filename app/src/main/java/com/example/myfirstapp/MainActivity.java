package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import android.widget.Toast;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.TRUE;

public class MainActivity extends AppCompatActivity {
    //set if clicked
    boolean clickedPizza = false;
    boolean clickedSpaghetti = false;
    boolean clickedSalad = false;
    boolean clickedSoup = false;
    int button_counter = 0;
    private String m_Text = "";
    private static int RESULT_LOAD_IMAGE = 1;
    List <ImageButton> buttons;
    List <String> meals;
    TextView choiceText;
    ImageButton unknown0;
    ImageButton unknown1;
    ImageButton unknown2;
    MyDBHandler dbHandler;
    ImageButton uploadBtn;
    Button chooseBtn;
    private int STORAGE_PERMISSION_CODE = 1;
    void fun(final ImageButton a){
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Boolean.valueOf(String.valueOf(a.getTag(R.id.clickedValue))) == false) {
                    meals.add(String.valueOf(a.getTag(R.id.name)));
//                    a.setColorFilter(Color.argb(100,40, 143, 234));
                    a.setColorFilter(Color.argb(100,230, 230, 230));
                    //a.setBackgroundColor(Color.GRAY);
                    a.setTag(R.id.clickedValue,true);
                } else {
                    a.setColorFilter(Color.argb(0,0,0,0));
                    meals.remove(String.valueOf(a.getTag(R.id.name)));
                    a.setBackgroundColor(Color.WHITE);
                    a.setTag(R.id.clickedValue,false);
                }
            }
        });
        a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dbHandler.deleteProduct(String.valueOf(a.getTag(R.id.name)));
                                a.setVisibility(View.GONE);

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new ArrayList<ImageButton>();
        meals = new ArrayList <String>();

        ImageButton pizzaBtn =  new ImageButton(MainActivity.this);
        ImageButton spaghettiBtn = new ImageButton(MainActivity.this);
        ImageButton saladBtn = new ImageButton(MainActivity.this);
        ImageButton soupBtn = new ImageButton(MainActivity.this);
        addExistingButton(pizzaBtn,"Pizza",((BitmapDrawable) getResources().getDrawable(R.drawable.pizzapic)).getBitmap());
        addExistingButton(spaghettiBtn,"Spaghetti",((BitmapDrawable) getResources().getDrawable(R.drawable.spagi)).getBitmap());
        addExistingButton(saladBtn,"Salad",((BitmapDrawable) getResources().getDrawable(R.drawable.sala)).getBitmap());
        addExistingButton(soupBtn,"Soup",((BitmapDrawable) getResources().getDrawable(R.drawable.soup)).getBitmap());

        uploadBtn = findViewById(R.id.uploadButton);
        chooseBtn =  findViewById(R.id.choiceButton);
        choiceText  = findViewById(R.id.choiceTextView);


        for(ImageButton btn : buttons){
            btn.setTag(R.id.clickedValue,false);
            fun(btn);
        }

        dbHandler = new MyDBHandler(this,null,null,1);

        LinkedHashMap <Integer, List<String>> dbString = dbHandler.databaseToString();
        //choiceText.setText(String.valueOf(dbString));
        if(!dbString.isEmpty()) {
            for(int i = 0; i < dbString.size();i++) {
                List<String> a = dbString.get(i);
                String name = a.get(0);
                String picpath = a.get(1);
                ImageButton n = new ImageButton(MainActivity.this);
                addButton(n,name,picpath);

            }

        }


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    uploadBtn.setEnabled(false);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    requestStoragePermission();
                }
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choiceText.setText("Short click");
                Random rand = new Random();
                if(meals.size()==0) {
                    choiceText.setText("Nothing selected");
                } else {
                    String result = "Result is ";
                    String meal =  meals.get(new Random().nextInt(meals.size()));
                    String con = result.concat(meal);
                    choiceText.setText(con);
                }
                for(ImageButton btn:buttons) {
                    btn.setTag(R.id.clickedValue,false);
                    btn.setBackgroundColor(Color.WHITE);
                    btn.setColorFilter(Color.argb(0,0,0,0));
                }
                meals.clear();
            }
        });
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed so as to be able upload photo to the application")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void addExistingButton(ImageButton btn,String btnName,Bitmap bitmap){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        btn.setImageBitmap(resizedBitmap);
        buttons.add(btn);
        btn.setTag(R.id.name,btnName);
        btn.setTag(R.id.clickedValue,false);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(10,10,10,10);
        btn.setLayoutParams(lp);
        fun(btn);
        btn.setPadding(0,0,0,0);
        btn.setAdjustViewBounds(true);
        btn.setScaleType( ImageView.ScaleType.FIT_CENTER);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        ll.addView(btn,lp);
    }
    public void addButton(ImageButton btn,String btnName,String path){
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 300, 300, true);
        btn.setImageBitmap(BitmapFactory.decodeFile(path));
        btn.setImageBitmap(bitmap);
        buttons.add(btn);
        btn.setTag(R.id.name,btnName);
        btn.setTag(R.id.clickedValue,false);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn.setLayoutParams(lp);
        fun(btn);
        btn.setPadding(0,0,0,0);
        btn.setAdjustViewBounds(true);
        btn.setScaleType( ImageView.ScaleType.FIT_CENTER);
        lp.setMargins(10,10,10,10);
        LinearLayout ll = (LinearLayout)findViewById(R.id.linear);
        ll.addView(btn,lp);
    }
    //////////////////////for upload of picture
    String picturePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ///////////////////////////////////////////////////

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name your choice");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            //input.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                    Meals meal = new Meals(picturePath,m_Text);
                    dbHandler.addProduct(meal);
                    ImageButton n = new ImageButton(MainActivity.this);
                    addButton(n,m_Text,picturePath);
                    uploadBtn.setEnabled(true);
                    m_Text="";
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    choiceText.setText("Cancelled input");
                    uploadBtn.setEnabled(true);
                }
            });
            builder.show();
        }
    }
}
