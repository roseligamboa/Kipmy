package com.rgrc.kipmy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anotacoes);

        setTheme(R.style.listaAnotacoes);

        new Timer().schedule(new TimerTask() {
//O Timer e o TimerTaks são componentes de mais alto-nível com o propósito de executar ações agendadas recorrentes ou após um determinado tempo
            @Override
            public void run() {
                finish();

                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, ListaAnotacoesActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

}
