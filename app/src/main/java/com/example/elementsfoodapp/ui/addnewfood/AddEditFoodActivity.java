package com.example.elementsfoodapp.ui.addnewfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.elementsfoodapp.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**This activity class handles the UI to add new food or edit food in the database. It is also
 * used to show the food details.*/
public class AddEditFoodActivity extends AppCompatActivity
        implements AddFoodAdapter.ListItemClickListener {

    public static final String EXTRA_ID = "com.example.elementsfoodapp.EXTRA_ID";
    public static final String EXTRA_REPLY = "com.example.elementsfoodapp.REPLY";

    private Intent intent;
    private RecyclerView recyclerView;
    private AddFoodAdapter.ViewHolder holder;
    private TextInputEditText addFoodName;
    private TextInputEditText addFoodEffect;
    private String[] foodData;
    private String[] foodType;
    private String[] foodElements;
    private String[] foodFlavor;
    private String[] foodTempBehavior;
    private String[] foodTargetOrgan;

    private final boolean[] checkedFoodType = new boolean[14];
    private final boolean[] checkedFoodElements = new boolean[5];
    private final boolean[] checkedFoodFlavor = new boolean[5];
    private final boolean[] checkedFoodTempBehavior = new boolean[5];
    private final boolean[] checkedFoodTargetOrgan = new boolean[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);

        AddFoodAdapter adapter = new AddFoodAdapter(this, this);
        recyclerView = findViewById(R.id.foodPropertiesView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);

        Resources res = getResources();
        foodType = res.getStringArray(R.array.food_type_array);
        foodElements = res.getStringArray(R.array.food_elements_array);
        foodFlavor = res.getStringArray(R.array.food_flavor_array);
        foodTempBehavior = res.getStringArray(R.array.food_temp_behavior_array);
        foodTargetOrgan = res.getStringArray(R.array.food_target_organ);
        addFoodName = findViewById(R.id.addFoodName);
        addFoodEffect = findViewById(R.id.addFoodEffect);

        intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Detail");
            foodData = intent.getStringArrayExtra(EXTRA_REPLY);
            addFoodName.setText(foodData[0]);
            addFoodEffect.setText(foodData[1]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_new_food_menu, menu);

        // Sets the food properties text for the Detail Activity
        if (intent.hasExtra(EXTRA_ID)) {
            for (int i = 0; i < 5; i++) {
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    holder.getSecondaryTextView().setText(foodData[i + 2]);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        boolean isDispatchable = true;

        if (id == R.id.action_confirm) {
            if (Objects.requireNonNull(addFoodName.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(addFoodEffect.getText()).toString().isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Bitte fülle alle Felder aus", Toast.LENGTH_SHORT).show();
                isDispatchable = false;
            } else {
                for (int i = 0; i < 5; i++) {
                    holder = (AddFoodAdapter.ViewHolder) recyclerView
                            .findViewHolderForAdapterPosition(i);
                    assert holder != null;
                    if (holder.getSecondaryTextView().getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(),
                                "Bitte fülle alle Felder aus", Toast.LENGTH_SHORT).show();
                        isDispatchable = false;
                        break;
                    }
                }
            }
            // Prepare intent to send food data to FoodListActivity
            if (isDispatchable) {
                Intent replyIntent = new Intent();
                String[] foodData = new String[7];
                foodData[0] = addFoodName.getText().toString();
                foodData[1] = addFoodEffect.getText().toString();
                for (int i = 0; i < 5; i++) {
                    holder = (AddFoodAdapter.ViewHolder) recyclerView
                            .findViewHolderForAdapterPosition(i);
                    assert holder != null;
                    foodData[i + 2] = holder.getSecondaryTextView().getText().toString();
                }
                replyIntent.putExtra(EXTRA_REPLY, foodData);

                int idEXTRA = getIntent().getIntExtra(EXTRA_ID, -1);
                if (idEXTRA != -1) {
                    replyIntent.putExtra(EXTRA_ID, idEXTRA);
                }

                setResult(RESULT_OK, replyIntent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(View v, int position) {
        openDialog(position);
    }

    public void openDialog(int position) {
        final int FOOD_TYPE = 0;
        final int FOOD_ELEMENTS = 1;
        final int FOOD_FLAVOR = 2;
        final int FOOD_TEMP_BEHAVIOR = 3;
        final int FOOD_TARGET_ORGAN = 4;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Building the list to be shown in AlertDialog
        if (position == FOOD_TYPE) {
            //Setting AlertDialog Characteristics
            builder.setTitle("Wähle Nahrungsmittelart aus");

            builder.setMultiChoiceItems(
                    foodType, checkedFoodType, (dialog, which, isChecked) -> {
                        //Update the current item's checked status
                        checkedFoodType[which] = isChecked;

                        if (((AlertDialog) dialog).getListView().getCheckedItemCount() > 1) {
                            Toast.makeText(getApplicationContext(),
                                    "Maximal eine Art", Toast.LENGTH_SHORT).show();
                            checkedFoodType[which] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(
                                    which, false);
                        }
                    });

            //Set positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(position);
                assert holder != null;
                for (int i = 0; i < checkedFoodType.length; i++) {
                    boolean checked = checkedFoodType[i];
                    if (checked) {
                        holder.getSecondaryTextView().setText(foodType[i]);
                    }
                }

                boolean allFalse = true;
                for (boolean b : checkedFoodType) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    holder.getSecondaryTextView().setText("");
                }
            });
        } else if (position == FOOD_ELEMENTS) {
            //Setting AlertDialog Characteristics
            builder.setTitle("Wähle Element(e) aus");

            builder.setMultiChoiceItems(
                    foodElements, checkedFoodElements, (dialog, which, isChecked) -> {
                        //Update the current item's checked status
                        checkedFoodElements[which] = isChecked;

                        if (((AlertDialog) dialog).getListView().getCheckedItemCount() > 2) {
                            Toast.makeText(getApplicationContext(),
                                    "Maximal 2 Elemente", Toast.LENGTH_SHORT).show();
                            checkedFoodElements[which] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(
                                    which, false);
                        }
                    });

            //Set positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder items = new StringBuilder();
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(position);
                assert holder != null;
                for (int i = 0; i < checkedFoodElements.length; i++) {
                    boolean checked = checkedFoodElements[i];
                    if (checked) {
                        items.append(foodElements[i]).append(", ");
                    }
                }
                // Delete last comma
                if (items.length() > 2)
                    items.deleteCharAt(items.length() - 2);
                holder.getSecondaryTextView().setText(items.toString());

                boolean allFalse = true;
                for (boolean b : checkedFoodElements) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    holder.getSecondaryTextView().setText("");
                }
            });
        } else if (position == FOOD_FLAVOR) {
            //Setting AlertDialog Characteristics
            builder.setTitle("Wähle Geschmacksrichtung(en) aus");

            builder.setMultiChoiceItems(
                    foodFlavor, checkedFoodFlavor, (dialog, which, isChecked) -> {
                        //Update the current item's checked status
                        checkedFoodFlavor[which] = isChecked;

                        if (((AlertDialog) dialog).getListView().getCheckedItemCount() > 2) {
                            Toast.makeText(getApplicationContext(),
                                    "Maximal 2 Geschmacksrichtungen", Toast.LENGTH_SHORT)
                                    .show();
                            checkedFoodFlavor[which] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(
                                    which, false);
                        }
                    });

            //Set positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder items = new StringBuilder();
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(position);
                assert holder != null;
                for (int i = 0; i < checkedFoodFlavor.length; i++) {
                    boolean checked = checkedFoodFlavor[i];
                    if (checked) {
                        items.append(foodFlavor[i]).append(", ");
                    }
                }
                // Delete last comma
                if (items.length() > 2)
                    items.deleteCharAt(items.length() - 2);
                holder.getSecondaryTextView().setText(items.toString());

                boolean allFalse = true;
                for (boolean b : checkedFoodFlavor) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    holder.getSecondaryTextView().setText("");
                }
            });
        } else if (position == FOOD_TEMP_BEHAVIOR) {
            //Setting AlertDialog Characteristics
            builder.setTitle("Wähle thermische Wirkung(en) aus");

            builder.setMultiChoiceItems(
                    foodTempBehavior, checkedFoodTempBehavior, (dialog, which, isChecked) -> {
                        //Update the current item's checked status
                        checkedFoodTempBehavior[which] = isChecked;

                        if (((AlertDialog) dialog).getListView().getCheckedItemCount() > 2) {
                            Toast.makeText(getApplicationContext(),
                                    "Maximal 2 thermische Wirkungen", Toast.LENGTH_SHORT)
                                    .show();
                            checkedFoodTempBehavior[which] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(
                                    which, false);
                        }
                    });

            //Set positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder items = new StringBuilder();
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(position);
                assert holder != null;
                for (int i = 0; i < checkedFoodTempBehavior.length; i++) {
                    boolean checked = checkedFoodTempBehavior[i];
                    if (checked) {
                        items.append(foodTempBehavior[i]).append(", ");
                    }
                }

                // Delete last comma
                if (items.length() > 2)
                    items.deleteCharAt(items.length() - 2);
                holder.getSecondaryTextView().setText(items.toString());

                boolean allFalse = true;
                for (boolean b : checkedFoodTempBehavior) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    holder.getSecondaryTextView().setText("");
                }
            });
        } else if (position == FOOD_TARGET_ORGAN) {
            //Setting AlertDialog Characteristics
            builder.setTitle("Wähle Zielorgan(e) aus");

            builder.setMultiChoiceItems(
                    foodTargetOrgan, checkedFoodTargetOrgan, (dialog, which, isChecked) -> {
                        //Update the current item's checked status
                        checkedFoodTargetOrgan[which] = isChecked;
                    });

            //Set positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                StringBuilder items = new StringBuilder();
                holder = (AddFoodAdapter.ViewHolder) recyclerView
                        .findViewHolderForAdapterPosition(position);
                assert holder != null;
                for (int i = 0; i < checkedFoodTargetOrgan.length; i++) {
                    boolean checked = checkedFoodTargetOrgan[i];
                    if (checked) {
                        items.append(foodTargetOrgan[i]).append(", ");
                    }
                }

                // Delete last comma
                if (items.length() > 2)
                    items.deleteCharAt(items.length() - 2);
                holder.getSecondaryTextView().setText(items.toString());

                boolean allFalse = true;
                for (boolean b : checkedFoodTargetOrgan) {
                    if (b) {
                        allFalse = false;
                        break;
                    }
                }
                if (allFalse) {
                    holder.getSecondaryTextView().setText("");
                }
            });
        }

        //Set negative button
        builder.setNegativeButton("Abbrechen", (dialog, which) ->
                Toast.makeText(getApplicationContext(), "Abgebrochen", Toast.LENGTH_SHORT)
                        .show());

        //Creating AlertDialog
        AlertDialog alertDialog = builder.create();
        //Displaying AlertDialog
        alertDialog.show();
    }
}
