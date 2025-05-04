# Listador de Tarefas para Wear OS

Este repositório contém o resultado da Missão Prática do Nível 3 do Mundo 4, onde foi desenvolvido um aplicativo para dispositivos Wear OS que utiliza sensores e recursos de áudio para criar uma experiência de lista de tarefas acessível.

## Estrutura do Repositório

O repositório está organizado da seguinte forma:

- **app**: Contém o código-fonte do aplicativo Android para Wear OS
  - **src/main/java**: Classes Java do aplicativo
  - **src/main/res**: Recursos (layouts, strings, etc.)
  - **AndroidManifest.xml**: Arquivo de manifesto com configurações do aplicativo

## Microatividades Realizadas

### Microatividade 1: Implementar a visão geral e melhores práticas para acesso a sensores
Configuração inicial do ambiente de desenvolvimento, incluindo a instalação do Android Studio e compreensão das melhores práticas para acesso a sensores em dispositivos Wear OS.

### Microatividade 2: Criando um novo projeto no Android Studio
Criação do projeto base para o aplicativo Wear OS, configurando as estruturas iniciais necessárias.

### Microatividade 3: Arquivos de Lógica e Configurações
Implementação dos arquivos básicos de lógica e configuração, incluindo MainActivity e configurações no AndroidManifest.xml.

### Microatividade 4: Criando um emulador
Configuração de um emulador para Wear OS para testar o aplicativo.

### Microatividade 5: Fazer capturas de telas com app complementar
Utilização de ferramentas para capturar telas do aplicativo em execução no emulador ou dispositivo Wear OS.

## Projeto Final: Lista de Tarefas com Reconhecimento de Voz

O projeto final é um aplicativo de lista de tarefas para Wear OS que utiliza recursos de reconhecimento de voz e reprodução de áudio, especialmente útil para pessoas com necessidades especiais.

### Funcionalidades Principais

1. **Reconhecimento de Voz para Criar Tarefas**
   - O usuário pode adicionar tarefas falando no dispositivo
   - Um som é reproduzido para indicar o início do reconhecimento
   - A tarefa reconhecida é adicionada automaticamente à lista

2. **Reprodução de Áudio para Tarefas**
   - Ao selecionar uma tarefa, o sistema tenta reproduzir seu conteúdo em áudio
   - Suporta reprodução tanto pelo alto-falante integrado quanto por dispositivos Bluetooth

3. **Detecção Inteligente de Dispositivos de Áudio**
   - Detecta automaticamente dispositivos de áudio disponíveis
   - Alterna entre alto-falante integrado e dispositivos Bluetooth quando disponíveis
   - Oferece opção para abrir configurações de Bluetooth quando nenhum dispositivo é encontrado

4. **Interface Adaptativa**
   - Layout otimizado para dispositivos Wear OS, tanto para formatos redondos quanto quadrados
   - Design minimalista para melhor visibilidade em telas pequenas

5. **Suporte a Sensores Corporais**
   - Inclui suporte para sensores corporais, demonstrando a integração com hardware específico do Wear OS
   - Implementa proteção para Wake Lock, permitindo que o aplicativo continue funcionando mesmo com a tela desligada

### Tecnologias e APIs Utilizadas

- **Android Wear OS SDK**
- **SpeechRecognizer**: Para reconhecimento de voz
- **MediaPlayer**: Para reprodução de áudio
- **AudioManager e AudioDeviceCallback**: Para gerenciamento de dispositivos de áudio
- **ConstraintLayout e BoxInsetLayout**: Para interfaces adaptativas em Wear OS

## Como Executar

Para executar este projeto:

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Configure um emulador Wear OS ou conecte um dispositivo físico
4. Execute o aplicativo usando a configuração de execução "Android App"

## Requisitos do Sistema

- Android Studio (versão mais recente recomendada)
- Emulador Wear OS (API 30 ou superior) ou dispositivo Wear OS físico
- SDK Android atualizado com componentes Wear OS

## Autor

Este projeto foi desenvolvido como parte do curso de desenvolvimento mobile, como uma atividade prática para aprendizado de desenvolvimento para Wear OS com foco em acessibilidade e uso de sensores.
