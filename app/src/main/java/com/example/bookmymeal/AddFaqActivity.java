package com.example.bookmymeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddFaqActivity extends AppCompatActivity {

    public static final String ExtraId="com.example.bookmymeal.EXTRA_ID";
    public static final String ExtraQuestion="com.example.bookmymeal.EXTRA_QUESTION";
    public static final String ExtraAnswer="com.example.bookmymeal.EXTRA_ANSWER";


    private TextInputEditText editTextquestion,editTextanswer;
    MaterialButton faqSave;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq);
        SharedPreferences sharedPreferences =getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        role=sharedPreferences.getString("Role","NotFound");
        System.out.println("Role:"+role);
        editTextquestion=(TextInputEditText)findViewById(R.id.textInputEditTextQuestion);
        editTextanswer=(TextInputEditText)findViewById(R.id.textInputEditTextAnswer);
        faqSave=(MaterialButton)findViewById(R.id.buttonFAQSave);
        faqSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFaq();
            }
        });
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

        Intent intent=getIntent();
        System.out.println("IntentValue:"+intent.getIntExtra(ExtraId,-1));
        if(intent.hasExtra(ExtraId)){
            setTitle("Edit FAQ");
            if(role.equals("Admin")){
                editTextquestion.setText(intent.getStringExtra(ExtraQuestion));
                editTextanswer.setText(intent.getStringExtra(ExtraAnswer));
            }
            else{
                editTextquestion.setEnabled(false);
                editTextanswer.setEnabled(false);
                editTextquestion.setText(intent.getStringExtra(ExtraQuestion));
                editTextanswer.setText(intent.getStringExtra(ExtraAnswer));
                faqSave.setVisibility(View.GONE);
            }



        }
        else{
            setTitle("Add FAQ");
        }
    }



    private void saveFaq(){
        String question=editTextquestion.getText().toString();
        String answer=editTextanswer.getText().toString();

        if(question.trim().isEmpty() || answer.trim().isEmpty()){
            Toast.makeText(this, "Fill the empty field", Toast.LENGTH_LONG).show();
            return;
        }


        Intent data = new Intent();
        data.putExtra(ExtraQuestion, question);
        data.putExtra(ExtraAnswer, answer);

        System.out.println(question + "/ " + answer);

        int id =getIntent().getIntExtra(ExtraId,-1);
        System.out.println("id:"+id);
        //String id = getIntent().getStringExtra(ExtraId);
        //int pos=Integer.valueOf(id);

        if (id != -1) {
            data.putExtra(ExtraId, id);
        }

        setResult(RESULT_OK, data);
        System.out.println("ResultOK:"+RESULT_OK);
        finish();

    }
}
