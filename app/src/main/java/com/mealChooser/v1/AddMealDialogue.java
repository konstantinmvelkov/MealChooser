package com.mealChooser.v1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.mealChooser.v1.R.layout;
public class AddMealDialogue extends AppCompatDialogFragment {
    protected EditText name;
    protected EditText description;
    protected EditText recipe;
    protected ImageView image;
    private AddMealDialogListener listener;
    protected String picturePath;
    public AddMealDialogue(String picPath) {
        this.picturePath = picPath;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(layout.add_meal_custom_dialogue,null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 300, 300, true);
        name = view.findViewById(R.id.dialogueMealName);
        description = view.findViewById(R.id.dialogueMealDescription);
        recipe = view.findViewById(R.id.dialogueMealRecipe);

        image = view.findViewById(R.id.dialogueMealPicture);
        image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        image.setImageBitmap(bitmap);

        System.out.println(picturePath);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String nameToListen = name.getText().toString();
                        String descriptionToListen = description.getText().toString();
                        String recipeToListen = recipe.getText().toString();
                        String mealPicturePathToListen = picturePath;
                        listener.addMeal(nameToListen, descriptionToListen, recipeToListen,mealPicturePathToListen);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddMealDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface AddMealDialogListener {
        void addMeal(String name, String description, String recipe, String picPath);
    }
}
