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
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class EditMealDialogue extends AppCompatDialogFragment {
    protected EditText name;
    protected EditText description;
    protected EditText recipe;
    protected ImageView image;
    protected ImageButton clickedButton;

    private EditMealDialogueListener listener;

    protected String picturePath;
    public EditMealDialogue(ImageButton clickedButton) {
        this.clickedButton = clickedButton;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_meal_custom_dialogue,null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        name = view.findViewById(R.id.dialogueMealName);
        name.setText(String.valueOf(clickedButton.getTag(R.id.name)));

        description = view.findViewById(R.id.dialogueMealDescription);
        description.setText(String.valueOf(clickedButton.getTag(R.id.description)));

        recipe = view.findViewById(R.id.dialogueMealRecipe);
        recipe.setText(String.valueOf(clickedButton.getTag(R.id.recipe)));

        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(String.valueOf(clickedButton.getTag(R.id.picturePath))), 300, 300, true);
        image = view.findViewById(R.id.dialogueMealPicture);
        image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        image.setImageBitmap(bitmap);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String nameToListen = name.getText().toString();
                        String descriptionToListen = description.getText().toString();
                        String recipeToListen = recipe.getText().toString();
                        String mealPicturePathToListen = picturePath;
                        listener.editMeal(clickedButton,nameToListen, descriptionToListen, recipeToListen, mealPicturePathToListen);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.deleteMeal(clickedButton,String.valueOf(clickedButton.getTag(R.id.name)));
                    }
                });

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditMealDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface EditMealDialogueListener {
        void editMeal(ImageButton clickedButton,String name, String description, String recipe, String picPath);
        void deleteMeal(ImageButton clickedButton, String name);
    }
}
