package com.example.listadetarefas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ListView listViewTarefas;
    private Button buttonAdicionar;
    private ArrayList<String> tarefas;
    private ArrayAdapter<String> adapter;
    private AudioHelper audioHelper;
    private SpeechRecognizer speechRecognizer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inicializa o AudioHelper
        audioHelper = new AudioHelper(this);
        
        // Configura os Views
        listViewTarefas = findViewById(R.id.listView_tarefas);
        buttonAdicionar = findViewById(R.id.button_adicionar);
        
        // Inicializa a lista de tarefas
        tarefas = new ArrayList<>();
        tarefas.add("Reunião às 10h");
        tarefas.add("Entregar relatório");
        tarefas.add("Conferência às 15h");
        
        // Configura o adapter para a ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tarefas);
        listViewTarefas.setAdapter(adapter);
        
        // Inicializa o reconhecedor de voz
        inicializarReconhecimentoVoz();
        
        // Configurando o clique no botão adicionar
        buttonAdicionar.setOnClickListener(v -> {
            // Verifica permissão de áudio antes de iniciar o reconhecimento
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                iniciarReconhecimentoVoz();
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        });
        
        // Configura o clique no item da lista
        listViewTarefas.setOnItemClickListener((parent, view, position, id) -> {
            String tarefaSelecionada = tarefas.get(position);
            Toast.makeText(MainActivity.this, "Tarefa: " + tarefaSelecionada, Toast.LENGTH_SHORT).show();
            
            // Tentar reproduzir áudio da tarefa
            reproduzirAudioTarefa(tarefaSelecionada);
        });
        
        // Registra o callback de dispositivos de áudio
        registrarAudioDeviceCallback();
    }
    
    private void inicializarReconhecimentoVoz() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(MainActivity.this, "Estou ouvindo...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBeginningOfSpeech() {}

                @Override
                public void onRmsChanged(float rmsdB) {}

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {}

                @Override
                public void onError(int error) {
                    String mensagemErro;
                    switch (error) {
                        case SpeechRecognizer.ERROR_AUDIO:
                            mensagemErro = "Erro de áudio";
                            break;
                        case SpeechRecognizer.ERROR_CLIENT:
                            mensagemErro = "Erro no cliente";
                            break;
                        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                            mensagemErro = "Sem permissão";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK:
                            mensagemErro = "Erro de rede";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                            mensagemErro = "Tempo de rede esgotado";
                            break;
                        case SpeechRecognizer.ERROR_NO_MATCH:
                            mensagemErro = "Não entendi o que você disse";
                            break;
                        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                            mensagemErro = "Reconhecedor ocupado";
                            break;
                        case SpeechRecognizer.ERROR_SERVER:
                            mensagemErro = "Erro no servidor";
                            break;
                        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                            mensagemErro = "Tempo esgotado";
                            break;
                        default:
                            mensagemErro = "Erro desconhecido";
                            break;
                    }
                    Toast.makeText(MainActivity.this, mensagemErro, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String textoReconhecido = matches.get(0);
                        adicionarTarefa(textoReconhecido);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {}

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });
        } else {
            Toast.makeText(this, "Reconhecimento de voz não disponível neste dispositivo", Toast.LENGTH_SHORT).show();
            buttonAdicionar.setEnabled(false);
        }
    }
    
    private void iniciarReconhecimentoVoz() {
        // Toca um som de notificação para indicar o início do reconhecimento
        tocarSomNotificacao();
        
        // Inicia o reconhecimento de voz
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga o nome da tarefa");
        
        speechRecognizer.startListening(intent);
    }
    
    private void tocarSomNotificacao() {
        // Cria e toca um som de notificação
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.setOnCompletionListener(mp -> mp.release());
        mediaPlayer.start();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarReconhecimentoVoz();
            } else {
                Toast.makeText(this, "Permissão para gravação de áudio é necessária", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void adicionarTarefa(String tarefa) {
        if (tarefa != null && !tarefa.isEmpty()) {
            tarefas.add(tarefa);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Tarefa adicionada: " + tarefa, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void reproduzirAudioTarefa(String tarefa) {
        // Verificar se há dispositivo de áudio disponível para reprodução
        if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)) {
            // Implementar a reprodução de áudio aqui
            Toast.makeText(MainActivity.this, "Reproduzindo áudio: " + tarefa, Toast.LENGTH_SHORT).show();
            
            // Toca um som para simular a reprodução da tarefa
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
            mediaPlayer.start();
            
        } else if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
            Toast.makeText(MainActivity.this, "Reproduzindo via Bluetooth: " + tarefa, Toast.LENGTH_SHORT).show();
            
            // Toca um som para simular a reprodução da tarefa
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
            mediaPlayer.start();
            
        } else {
            // Oferecer opção para ir às configurações de Bluetooth
            exibirOpcaoConectarBluetooth();
        }
    }
    
    private void exibirOpcaoConectarBluetooth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Saída de áudio necessária");
        builder.setMessage("Para ouvir o áudio, conecte um dispositivo Bluetooth ou use um dispositivo com alto-falante.");
        
        // Adiciona os botões
        builder.setPositiveButton("Configurações Bluetooth", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            intent.putExtra("EXTRA_CONNECTION_ONLY", true);
            intent.putExtra("EXTRA_CLOSE_ON_CONNECT", true);
            intent.putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1);
            startActivity(intent);
        });
        
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        
        // Exibe o diálogo
        builder.show();
    }
    
    private void registrarAudioDeviceCallback() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.registerAudioDeviceCallback(new AudioDeviceCallback() {
            @Override
            public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
                super.onAudioDevicesAdded(addedDevices);
                for (AudioDeviceInfo device : addedDevices) {
                    if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Fone de ouvido Bluetooth conectado", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }

            @Override
            public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
                super.onAudioDevicesRemoved(removedDevices);
                for (AudioDeviceInfo device : removedDevices) {
                    if (device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Fone de ouvido Bluetooth desconectado", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        }, null);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}