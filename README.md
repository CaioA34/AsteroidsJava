Atividade de Estrutura de Dados II aplicando conceitos de árvore.
# Galaga AVL - Projeto Final de Estrutura de Dados II

> Um jogo de nave desenvolvido em JavaFX, demonstrando a aplicação prática de estruturas de dados (Árvores Binárias e AVL).

# Sobre o Projeto

Este projeto foi desenvolvido como trabalho final da disciplina de **Estrutura de Dados II** do curso de Ciência da Computação.
O jogo utiliza de estrutura de dados para seu funcionamento.

# Funcionalidades Principais

**Map Selection (Árvore Binária):** O sistema de fases é organizado em uma árvore binária de decisão. Ao final de cada fase, o jogador escolhe entre duas fases:
    * **Esquerda:** Caminho mais fácil.
    * **Direita:** Caminho mais difícil.
* **Ranking (Árvore AVL):** O sistema de pontuação utiliza uma Árvore AVL (Auto-Balanceada) para armazenar os recordes. Isso garante que a inserção e busca de scores sejam sempre eficientes (*O(log n)*), independente da ordem de entrada.
* **Visualização Gráfica:**
    * Visualizador da estrutura da árvore de fases.
    * Visualizador da estrutura da árvore AVL (Scores) na tela de Game Over.
* **Gameplay:** Movimentação, sistema de tiro, inimigos (asteroides) e detecção de colisão.

---

# Tecnologias Utilizadas

* **Linguagem:** Java (JDK 21)
* **Interface Gráfica:** JavaFX
* **Gerenciador de Dependências:** Maven
* **IDE Recomendada:** IntelliJ IDEA

---

# Estruturas de Dados Aplicadas

### 1. Árvore Binária de Navegação (Fases)
A progressão do jogo não é linear. As fases são nós (`FaseNode`) conectados.
- **Implementação:** Classe `FaseBuilder`.
- **Visualização:** O mapa desenha as conexões recursivamente na tela de seleção.

### 2. Árvore AVL (Scoreboard)
Para o placar, foi implementada uma árvore AVL completa com rotações (Simples e Duplas) para manter o balanceamento.
- **Implementação:** Classe `AVLTree` e `ScoreNode`.
- **Destaque:** Ao pressionar 'T' na tela de Game Over, é possível ver visualmente como a árvore se auto-organizou para manter o equilíbrio das alturas.

---

## 📸 Screenshots

<img width="999" height="744" alt="image" src="https://github.com/user-attachments/assets/5cabafe6-09da-4113-b438-86a15964ef01" />

<img width="989" height="745" alt="image" src="https://github.com/user-attachments/assets/17515fab-dae0-4940-9473-ae741659d10f" />

<img width="999" height="743" alt="image" src="https://github.com/user-attachments/assets/d43b10b3-96cd-46ef-8bd3-ac4fe6051af8" />

<img width="996" height="746" alt="image" src="https://github.com/user-attachments/assets/eee90d41-efdb-421b-b929-c9ca31a6dd8e" />

---

## Como Rodar o Projeto

### Pré-requisitos
* Ter o **JDK 21** (ou superior) instalado.
* Ter o **Maven** instalado (ou usar o embutido na IDE).

### Passo a Passo (IntelliJ IDEA)

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/galaga-avl.git](https://github.com/seu-usuario/galaga-avl.git)
    ```
2.  **Abra o projeto:**
    * Abra o IntelliJ > `File` > `Open` > Selecione a pasta do projeto.
3.  **Execute:**
    * Navegue até `src/main/java/com/org/asteroids/AsteroidsGame.java`.
    * Clique no botão verde de **Play** ao lado da classe.

---

# Comandos

| Tecla | Ação | Contexto |
| A / D ou Setas | Move a Nave / Seleciona Fase | Jogo / Mapa |
| **ESPAÇO** | Atirar | Jogo |
| **ENTER** | Confirmar / Iniciar Fase | Mapa |
| **ESC** | Voltar ao Mapa | Jogo |
| **T** | Visualizar Árvore AVL | Game Over |

---

## Estrutura de Pastas

* `src/main/java/com/org/asteroids/`
    * `game/`: Entidades do jogo (Player, Tiro, Asteroide).
    * `model/`: Lógica das Estruturas de Dados (AVLTree, FaseNode).
    * `view/`: Classes de desenho (MapVisualizer).
    * `GalagaGame.java`: Classe principal e Loop do jogo.
* `src/main/resources/assets/`: Sprites e Imagens (Nave, Fundo, etc).

---

## Autor

Desenvolvido por **Caio Almeida Oliveira**
Estudante de Ciência da Computação - Estrutura de Dados II
