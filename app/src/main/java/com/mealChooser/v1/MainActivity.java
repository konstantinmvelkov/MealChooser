package com.mealChooser.v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.*;

import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity implements AddMealDialogue.AddMealDialogListener, EditMealDialogue.EditMealDialogueListener {

    ////ADDS
    //private InterstitialAd mInterstitialAd;


    private String m_Text = "";
    private static int RESULT_LOAD_IMAGE = 1;
    List<ImageButton> buttons;
    List<String> meals;
    TextView choiceText;

    MyDBHandler dbHandler;
    ImageButton uploadBtn;
    Button chooseBtn;
    private int STORAGE_PERMISSION_CODE = 1;

    void setMealImageButtonListeners(final ImageButton clickedButton) {
        clickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Boolean.valueOf(String.valueOf(clickedButton.getTag(R.id.isClicked))) == false) {
                    meals.add(String.valueOf(clickedButton.getTag(R.id.name)));
                    clickedButton.setColorFilter(Color.argb(100, 230, 230, 230));
                    clickedButton.setTag(R.id.isClicked, true);
                } else {
                    clickedButton.setColorFilter(Color.argb(0, 0, 0, 0));
                    meals.remove(String.valueOf(clickedButton.getTag(R.id.name)));
                    clickedButton.setBackgroundColor(Color.WHITE);
                    clickedButton.setTag(R.id.isClicked, false);
                }
            }
        });
        if (clickedButton.getTag(R.id.picturePath) != null) {
            clickedButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    EditMealDialogue ed = new EditMealDialogue(clickedButton);
                    ed.show(getSupportFragmentManager(), "editDialogue");
                    return true;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
//        //BannerAdd
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        //
//
//        //InterstitialADD
//        MobileAds.initialize(this,
//                "ca-app-pub-3940256099942544~3347511713");
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        //

        buttons = new ArrayList<ImageButton>();
        meals = new ArrayList<String>();

        ImageButton pizzaBtn = new ImageButton(MainActivity.this);
//        ImageButton spaghettiBtn = new ImageButton(MainActivity.this);
//        spaghettiBtn.setBackgroundColor(Color.WHITE);
        ImageButton saladBtn = new ImageButton(MainActivity.this);
        ImageButton soupBtn = new ImageButton(MainActivity.this);
        addExistingButton(pizzaBtn, "Pizza", ((BitmapDrawable) getResources().getDrawable(R.drawable.pizzapic)).getBitmap());
//        addExistingButton(spaghettiBtn,"Spaghetti",((BitmapDrawable) getResources().getDrawable(R.drawable.spagi)).getBitmap());
        addExistingButton(saladBtn, "Salad", ((BitmapDrawable) getResources().getDrawable(R.drawable.sala)).getBitmap());
        addExistingButton(soupBtn, "Soup", ((BitmapDrawable) getResources().getDrawable(R.drawable.soup)).getBitmap());

        uploadBtn = findViewById(R.id.uploadButton);
        chooseBtn = findViewById(R.id.choiceButton);
        choiceText = findViewById(R.id.choiceTextView);


        for (ImageButton btn : buttons) {
            btn.setTag(R.id.isClicked, false);
            setMealImageButtonListeners(btn);
        }

        dbHandler = new MyDBHandler(this, null, null, 1);

        LinkedHashMap<Integer, List<String>> dbString = dbHandler.databaseToString();
        //choiceText.setText(String.valueOf(dbString));
        if (!dbString.isEmpty()) {
            for (int i = 0; i < dbString.size(); i++) {
                List<String> a = dbString.get(i);
                String mealName = a.get(0);
                String mealPicPath = a.get(1);
                String mealDescription = a.get(2);
                String mealRecipe = a.get(3);
                ImageButton n = new ImageButton(MainActivity.this);
                addButton(n, mealName, mealPicPath, mealDescription, mealRecipe);

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
                if (meals.size() == 0) {
                    choiceText.setText("Nothing selected");
                } else {
                    String result = "Result is ";
                    String meal = meals.get(new Random().nextInt(meals.size()));
                    String con = result.concat(meal);
                    choiceText.setText(con);
                }
                for (ImageButton btn : buttons) {
                    btn.setTag(R.id.isClicked, false);
                    btn.setBackgroundColor(Color.WHITE);
                    btn.setColorFilter(Color.argb(0, 0, 0, 0));
                }
                meals.clear();
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
//                }
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
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addExistingButton(ImageButton btn, String btnName, Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        btn.setImageBitmap(resizedBitmap);
        buttons.add(btn);
        btn.setTag(R.id.name, btnName);
        btn.setTag(R.id.isClicked, false);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        btn.setLayoutParams(lp);
        setMealImageButtonListeners(btn);
        btn.setPadding(0, 0, 0, 0);
        btn.setAdjustViewBounds(true);
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear);
        ll.addView(btn, lp);
    }

    public void addButton(ImageButton btn, String btnName, String path, String description, String recipe) {
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(path), 300, 300, true);
        btn.setImageBitmap(BitmapFactory.decodeFile(path));
        btn.setImageBitmap(bitmap);
        buttons.add(btn);
        btn.setTag(R.id.name, btnName);
        btn.setTag(R.id.isClicked, false);
        btn.setTag(R.id.picturePath, path);
        btn.setTag(R.id.description, description);
        btn.setTag(R.id.recipe, recipe);
        LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        btn.setLayoutParams(layout);
        setMealImageButtonListeners(btn);
        btn.setPadding(0, 0, 0, 0);
        btn.setAdjustViewBounds(true);
        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        layout.setMargins(10, 10, 10, 10);
        LinearLayout linLay = findViewById(R.id.linear);
        linLay.addView(btn, layout);

    }

    @Override
    public void deleteMeal(ImageButton clickedButton, String name) {
        dbHandler.deleteProduct(name);
        LinearLayout linLay = findViewById(R.id.linear);
        linLay.removeView(clickedButton);
        clickedButton.setVisibility(View.GONE);
    }

    @Override
    public void editMeal(ImageButton clickedButton, String nameFromEdit, String descriptionFromEdit, String recipeFromEdit, String mealPicturePath) {
        clickedButton.setTag(R.id.description, descriptionFromEdit);
        clickedButton.setTag(R.id.recipe, recipeFromEdit);
        dbHandler.updateProduct(nameFromEdit, descriptionFromEdit, recipeFromEdit);
    }

    @Override
    public void addMeal(String nameFromEdit, String descriptionFromEdit, String recipeFromEdit, String mealPicturePath) {
        Meals meal = new Meals(mealPicturePath, nameFromEdit, descriptionFromEdit, recipeFromEdit);
        dbHandler.addProduct(meal);
        ImageButton mealButton = new ImageButton(MainActivity.this);
        addButton(mealButton, nameFromEdit, mealPicturePath, descriptionFromEdit, recipeFromEdit);
        uploadBtn.setEnabled(true);
    }


    //////////////////////for upload of picture
    String picturePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            ///////////////////////////////////////////////////
            AddMealDialogue addMeal = new AddMealDialogue(picturePath);
            addMeal.show(getSupportFragmentManager(), "editDialogue");
        }
    }
}
