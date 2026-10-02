package com.example.jogodeadivinhao

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.abs
import kotlin.random.Random

class JogoActivity : AppCompatActivity() {

    private var numeroSecreto = 0
    private var maximo = 50
    private var nome = ""
    private var tentativas = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_jogo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nome = intent.getStringExtra("nome") ?: "Jogador"
        maximo = intent.getIntExtra("maximo", 50)

        numeroSecreto = Random.nextInt(1, maximo + 1)

        val txtBoasVindas = findViewById<TextView>(R.id.txtBoasVindas)
        val edtPalpite = findViewById<EditText>(R.id.edtPalpite)
        val btnChutar = findViewById<Button>(R.id.btnChutar)
        val txtDica = findViewById<TextView>(R.id.txtDica)
        val txtTentativas = findViewById<TextView>(R.id.txtTentativas)
        val btnJogarDeNovo = findViewById<Button>(R.id.btnJogarDeNovo)

        txtBoasVindas.text = getString(R.string.msg_boas_vindas, nome, maximo)
        txtTentativas.text = getString(R.string.msg_tentativas, 0)
        txtDica.text = ""

        btnChutar.setOnClickListener {
            val palpiteText = edtPalpite.text.toString().trim()
            if (palpiteText.isEmpty()) {
                edtPalpite.error = "Digite um palpite"
                return@setOnClickListener
            }

            val palpite = palpiteText.toIntOrNull()
            if ((palpite == null) || (palpite < 1) || (palpite > maximo)) {
                edtPalpite.error = "Digite um número entre 1 e $maximo"
                return@setOnClickListener
            }

            tentativas++
            txtTentativas.text = getString(R.string.msg_tentativas, tentativas)

            if (palpite == numeroSecreto) {
                val msgAcertou = if (tentativas == 1) {
                    getString(R.string.msg_acertou_singular, nome, tentativas)
                } else {
                    getString(R.string.msg_acertou_plural, nome, tentativas)
                }
                txtDica.text = msgAcertou
                edtPalpite.isEnabled = false
                btnChutar.visibility = View.GONE
                btnJogarDeNovo.visibility = View.VISIBLE
            } else {
                val diferenca = abs(palpite - numeroSecreto)
                val limiteQuente = maximo * 0.10
                val temperatura = if (diferenca <= limiteQuente) getString(R.string.quente) else getString(R.string.frio)
                val direcao = if (numeroSecreto > palpite) getString(R.string.numero_maior) else getString(R.string.numero_menor)

                txtDica.text = getString(R.string.msg_dica, temperatura, direcao)
                edtPalpite.text.clear()
            }
        }

        btnJogarDeNovo.setOnClickListener {
            finish()
        }
    }
}
