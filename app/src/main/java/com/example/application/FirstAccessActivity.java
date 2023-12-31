package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.model.dao.UserController;

public class FirstAccessActivity extends AppCompatActivity {
    private EditText editTextNovaSenha;
    private EditText editTextConfirmacaoSenha;
    private ImageView backButtonBool;
    private TextView textViewMessage;
    private Button buttonConfirmar;

    private String token;
    private Boolean back;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);

        editTextNovaSenha = findViewById(R.id.editTextNovaSenha);
        editTextConfirmacaoSenha = findViewById(R.id.editTextConfirmacaoSenha);
        buttonConfirmar = findViewById(R.id.buttonConfirmar);
        textViewMessage = findViewById(R.id.textViewMessage);
        backButtonBool = findViewById(R.id.backButtonBool);

        token = getIntent().getStringExtra("token");
        back = getIntent().getBooleanExtra("back", false);
        String msg = getIntent().getStringExtra("msg");
        textViewMessage.setText(msg);

        backButtonBool.setVisibility(back ? View.VISIBLE : View.INVISIBLE);

        if (back) {
            backButtonBool.setOnClickListener(v -> {
                Intent intent = new Intent(FirstAccessActivity.this, MenuActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
                finish();
                backButtonBool.setVisibility(View.INVISIBLE);
            });
        }

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novaSenha = editTextNovaSenha.getText().toString();
                String confirmacaoSenha = editTextConfirmacaoSenha.getText().toString();

                if (novaSenha.isEmpty() || confirmacaoSenha.isEmpty()) {
                    showToast("Por favor, preencha todos os campos.");
                } else if (!novaSenha.equals(confirmacaoSenha)) {
                    showToast("As senhas não coincidem.");
                } else {
                    UserController userController = new UserController();
                    boolean atualizadoComSucesso = userController.updateUserPasswordAndFirstAccess(token, novaSenha, 0);

                    if (atualizadoComSucesso) {
                        showToast("Senha modificada com sucesso.");
                        Intent intent = new Intent(FirstAccessActivity.this, MenuActivity.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Erro na modificação da senha.");
                    }
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(FirstAccessActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
