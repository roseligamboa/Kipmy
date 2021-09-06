package com.rgrc.kipmy;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rgrc.kipmy.dao.AnotacaoDAO;
import com.rgrc.kipmy.modelo.Anotacao;

import java.util.List;


public class ListaAnotacoesActivity extends AppCompatActivity {

    //Lista de anotacoes:
    private ListView listaAnotacoes;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anotacoes);

        setTheme(R.style.Theme_Diario);

        Button novaAnotacao = findViewById(R.id.nova_anotacao);
        Button resetButton=(Button)findViewById(R.id.nova_anotacao);
        resetButton.setVisibility(View.VISIBLE); //To set visible

        //Defino a Lista
        listaAnotacoes = findViewById(R.id.lista_anotacoes);

/////////////////// EDITAR ANOTACAO ///////////////////

        //Com a lista na mão, posso editar quando clico num item dela:
        listaAnotacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Parametros: adapterView, lista, item, posição e id:
            public void onItemClick(AdapterView<?> lista, View item, int posicao, long id) {

                //Anotacao vem da lista, pegando a posição dela:
                Anotacao anotacao = (Anotacao) listaAnotacoes.getItemAtPosition(posicao);

                //Chamo a activity do formulário:
                Intent vaiProFormulario = new Intent(ListaAnotacoesActivity.this, FormularioActivity.class);

                //Manda na intent que vai pro formulário (só funciona se a classe for serializable):
                vaiProFormulario.putExtra("anotacao", anotacao);

                //Manda pra lá:
                startActivity(vaiProFormulario);
            }
        });

        novaAnotacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Parametros: de onde tá, pra onde vai
                Intent vaiProFormulario = new Intent(ListaAnotacoesActivity.this, FormularioActivity.class);
                startActivity(vaiProFormulario);
            }
        });

        //Diz que a lista tem o menu de contexto:
        registerForContextMenu(listaAnotacoes);
    }

    ////////////////// LISTAR ANOTACOES /////////////////////////////

    private void carregaLista() {
        //Instancia o DAO com esta activity como contexto:
        AnotacaoDAO dao = new AnotacaoDAO(this);

        //Pega as anotacoes:
        List<Anotacao> anotacoes = dao.buscaAnotacoes();

        //Fecha a conexão:
        dao.close();

        //Converte o array em varios TextViews (parametros: esta activity, layout padrão de lista do android, array com os dados):
        ArrayAdapter<Anotacao> adapter =  new ArrayAdapter<Anotacao>(this, android.R.layout.simple_list_item_1, anotacoes);

        //O ListView usa o adapter como conteúdo:
        listaAnotacoes.setAdapter(adapter);
    }

    //Pra quando sair da activity e atualizar a lista:
    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

////////////////// APAGAR ANOTACAO /////////////////////////////

    //Pra criar o menu de contexto NA MÃO:
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //Adiciona um item no menu:
        MenuItem deletar = menu.add("Deletar");

        //Ação pro deletar:
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //O menuInfo que veio como parametro é que diz quem teve o longo clique...
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

                //Com ele a gente pega a posição que teve o clique longo:
                Anotacao anotacao = (Anotacao) listaAnotacoes.getItemAtPosition(info.position);

                //Instancio o dao com esta activity de parâmetro (quando chiar, mete o nome da activity):
                AnotacaoDAO dao = new AnotacaoDAO(ListaAnotacoesActivity.this);

                //Apaga:
                dao.deleta(anotacao);
                //Fecha a conexão:
                dao.close();
                //Atualiza a lista:
                carregaLista();

                //Avisa:
                Toast.makeText(ListaAnotacoesActivity.this, "Anotacao " +anotacao.getTitulo() + " deletada!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}