package br.com.pratica.screen_nav_crud_server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ListView lista_itens;
    ArrayList<Carro> carros;
    ArrayAdapter ad;
    Button btn_editar;
    Button btn_adicionar;
    ArrayList<String> descricao_carros = new ArrayList<>();
    EditText text_id;
    String teste = "";
    int world_id;
    ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, "OnResume" , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carros = new ArrayList<>();

        btn_editar = findViewById(R.id.btn_editar);
        btn_adicionar = findViewById(R.id.btn_adicionar);
        text_id = (EditText) findViewById(R.id.text_id);
        lista_itens = (ListView) findViewById(R.id.lista_itens);

        ad = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, descricao_carros);
        lista_itens.setAdapter(ad);

        lista_itens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Você Selecionou : " + carros.get(position).getNome(), Toast.LENGTH_SHORT).show();
                teste = Integer.toString(carros.get(position).getId());
                text_id.setText(teste);
            }
        });

        new GetCarrosInFirebase().execute("Buscando carros registrados");
    }

    public class GetCarrosInFirebase extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(MainActivity.this, "Aguarde",
                    "confirmando pedido...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("teste","tentando pegar carros");

            FirebaseFirestore.getInstance().collection("carros")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot doc: task.getResult()){
                                    Log.d("teste", doc.getId() + " " + doc.get("ano"));
                                    Carro carro_novo = new Carro(Integer.parseInt(doc.get("id").toString()) - 1,doc.get("placa").toString(),doc.get("nome").toString(),
                                            Integer.parseInt(doc.get("ano").toString()));
                                    Log.d("teste", "carro do firebase:" + carro_novo.getNome() + carro_novo.getAno() + carro_novo.getId() + carro_novo.getPlaca());
                                    carros.add(carro_novo);
                                    Log.d("teste","id describe:" + doc.get("id"));
                                    descricao_carros.add("ID: " + carro_novo.getId() + " Placa: " + carro_novo.getPlaca() + " Nome: " + carro_novo.getNome() + " Ano: " + carro_novo.getAno());
                                    ad.notifyDataSetChanged();
                                }
                            }
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    public void setCarroInFirebase(Carro new_carro){

        Log.d("teste","Entrei na fn salvar");
        FirebaseFirestore.getInstance().collection("carros")
                .document("" + new_carro.getId())
                .set(new_carro);
    }


    public void onClickBtnEditar(View v){

        teste = text_id.getText().toString();
        boolean is_valid = false;

        if(teste.equals("")){
            is_valid = false;
        }

        if(carros.size() < 1){
            Toast.makeText(this,"Não há carros cadastrados",Toast.LENGTH_SHORT).show();
            return;
        }

        String comparador = "";

        for(int i = 0; i < carros.size(); i++){
            comparador = "" + carros.get(i).getId();
            if(comparador.equals(teste)){
                is_valid = true;
            }
        }

        if(is_valid == true) {
            int id_teste = Integer.parseInt(teste);
            //Toast.makeText(MainActivity.this, Integer.toString(id_teste), Toast.LENGTH_SHORT).show();

            if (id_teste > carros.size()) {
                is_valid = false;
                Toast.makeText(MainActivity.this, "ID invalido", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MainActivity.this, "ID invalido", Toast.LENGTH_SHORT).show();
        }

        if(is_valid == true) {
            int id_number = Integer.parseInt(teste);
            int index_id = id_number - 1;
            //Log.d("MainActivity", Integer.toString(world_id));
            world_id = id_number;
            Bundle params = new Bundle();
            params.putInt("id", id_number);
            params.putInt("index",index_id);
            params.putString("nome", carros.get(index_id).getNome());
            params.putInt("ano", carros.get(index_id).getAno());
            params.putString("placa", carros.get(index_id).getPlaca());
            params.putInt("Qitens",carros.size());
            params.putInt("adicionar", 0);

            Intent tela_editar = new Intent(this, MainActivity2.class);
            tela_editar.putExtras(params);
            startActivityForResult(tela_editar,1);
        }
    }

    public void onClickBtnAdicionar(View v){

        Bundle params = new Bundle();
        params.putInt("adicionar", 1);
        params.putInt("Qitens",carros.size());

        Intent tela_editar = new Intent(this, MainActivity2.class);
        tela_editar.putExtras(params);
        startActivityForResult(tela_editar,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int ano = data.getIntExtra("ano",0);
                int op = data.getIntExtra("op",0);
                String nome = data.getStringExtra("nome");
                String placa = data.getStringExtra("placa");
                Log.d("MainActivity", op + nome + ano + placa +"Aqui_f");

                if(op == 1) {
                    Log.d("MainActivity", "OP 1");
                    Carro carro_novo = new Carro(carros.size(), placa, nome, ano);
                    Log.d("MainActivity", carro_novo.getNome() + carro_novo.getAno() + carro_novo.getId() + carro_novo.getPlaca());
                    carros.add(carro_novo);
                    //carro_novo.setId(carro_novo.getId()-1);
                    setCarroInFirebase(carro_novo);
                    descricao_carros.add("ID: " + carro_novo.getId() + " Placa: " + carro_novo.getPlaca() + " Nome: " + carro_novo.getNome() + " Ano: " + carro_novo.getAno());
                    ad.notifyDataSetChanged();
                }

                if(op == 0){
                    Log.d("MainActivity", "OP 0");
                    world_id = world_id - 1;
                    carros.get(world_id).setAno(ano);
                    carros.get(world_id).setNome(nome);
                    carros.get(world_id).setPlaca(placa);
                    Carro new_carro = new Carro(world_id,placa,nome,ano);
                    setCarroInFirebase(new_carro);
                    descricao_carros.set(world_id, "ID: " + carros.get(world_id).getId() + " Placa: " + carros.get(world_id).getPlaca() + " Nome: " +
                            carros.get(world_id).getNome() + " Ano: " + carros.get(world_id).getAno());
                    ad.notifyDataSetChanged();
                }
            }
            if(resultCode == RESULT_CANCELED){
            }
        }
    }
}