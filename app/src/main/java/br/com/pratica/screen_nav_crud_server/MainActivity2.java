package br.com.pratica.screen_nav_crud_server;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    TextView text_id_input;
    EditText text_name_input;
    EditText text_ano_input;
    EditText text_placa_input;
    TextView id_text_view;
    int id_atual;
    int op;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text_id_input = (TextView) findViewById(R.id.text_id_input);
        text_name_input = (EditText) findViewById(R.id.text_name_input);
        text_placa_input = (EditText) findViewById(R.id.text_placa_input);
        text_ano_input = (EditText) findViewById(R.id.text_ano_input);
        id_text_view = (TextView) findViewById(R.id.id_text_view);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null) {
                int aux_op = params.getInt("adicionar");
                Log.d("MainActivity2",Integer.toString(aux_op) + "Aqui");
                op = aux_op;
                if (aux_op == 0) {
                    text_placa_input.setText(params.getString("placa"));
                    int ano_aux = params.getInt("ano");
                    String ano = Integer.toString(ano_aux);
                    text_ano_input.setText(ano);
                    text_name_input.setText(params.getString("nome"));
                    id_atual = params.getInt("id");
                    String id = Integer.toString(id_atual);
                    text_id_input.setText(id);
                    id_text_view.setText("Editando item ID: ");

                }
                if (aux_op == 1) {
                    id_text_view.setText("Criando item ID: ");
                    int qitens_aux = params.getInt("Qitens");
                    String pos_escrita = Integer.toString(qitens_aux + 1);
                    text_id_input.setText(pos_escrita);
                }
            }
        }
    }



    public void onClickBtnCancelar(View v) {
        onBackPressed();
    }

    public void onClickBtnConfirmar(View v) {

        String nome = text_name_input.getText().toString();
        String placa = text_placa_input.getText().toString();
        String teste = text_ano_input.getText().toString();
        //Log.d("MainActivity2",nome + placa + teste+ "Aqui");
        Boolean is_valid = true;

        for (int i = 0; i < teste.length(); i++) {
            if (teste.charAt(i) != '1' && teste.charAt(i) != '2' && teste.charAt(i) != '3' && teste.charAt(i) != '4' && teste.charAt(i) != '5'
                    && teste.charAt(i) != '6' && teste.charAt(i) != '7' && teste.charAt(i) != '8' && teste.charAt(i) != '9' && teste.charAt(i) != '0') {
                is_valid = false;
            }
        }
        if (nome.equals("") || placa.equals("") || teste.equals("")) {
            is_valid = false;
        }

        if(is_valid == true) {

            int ano = Integer.parseInt(teste);
            Bundle new_params = new Bundle();
            new_params.putInt("op", op);
            new_params.putInt("ano", ano);
            new_params.putString("nome", nome);
            new_params.putString("placa", placa);
            Log.d("MainActivity2", Integer.toString(op) +
                    new_params.getString("nome") + Integer.toString(ano) + new_params.getString("placa") + "Aqui" );

            Intent resultIntent = new Intent();
            resultIntent.putExtras(new_params);
            setResult(RESULT_OK, resultIntent);
            finishActivity(1);
            onBackPressed();
        }
        else{
            Toast.makeText(MainActivity2.this, "Campos Invalidos", Toast.LENGTH_SHORT).show();
        }
    }
}