# 3D Tic Tac Toe

## Description

Welcome to the 3D Tic Tac Toe project, a strategic and engaging twist on the classic Tic Tac Toe game that introduces a three-dimensional playfield. This innovative version features a board with 4 layers, each containing 16 slots, for a total of 64 slots. The 3D aspect of the game adds a unique layer of depth to the traditional gameplay, requiring players to think not only in two dimensions but also vertically across layers.

The aim is to align your symbols (X or O) vertically, horizontally, or diagonally within or across the layers, adding a new dimension of strategy and challenge to the game.

## Features

- **3D Board Configuration**: Dive into a more complex version of Tic Tac Toe with a board that extends across three dimensions, increasing the depth of strategy required.
- **Multiple Difficulty Levels**: Face off against an AI opponent that can adjust its difficulty level, offering a suitable challenge for both beginners and advanced players.
- **Advanced AI with MiniMax and Alpha-Beta Pruning**: The AI leverages the MiniMax algorithm with alpha-beta pruning to make smart, efficient moves, ensuring a challenging gameplay experience.

## Requirements

- Java Development Kit (JDK) installed on your computer.

## Compilation and Execution Instructions

To enjoy 3D Tic Tac Toe, follow these steps to compile and execute the game:

1. **Open your terminal or command prompt**.
2. **Navigate to the directory** where the 3D Tic Tac Toe source files are located by using the `cd` command. Make sure to replace `/path/to/3D-Tic-Tac-Toe/src` with the actual path to the source files on your system:

```bash
$ cd /path/to/3D-Tic-Tac-Toe/src
```

3. **Compile the Java source files** using the `javac` command. Assuming the main class file is named `TicTacToe.java`, compile it as follows:

```bash
$ javac TicTacToe.java
```

4. **Run the game** using the `java` command after the compilation is successful:

```bash
$ java TicTacToe
```

## How to Play

- The game kicks off with an empty 3D board consisting of 4 layers, each with 16 slots.
- Players alternate turns placing their symbol (X or O) into any unoccupied slot, striving to create a line of their symbols either horizontally, vertically, or diagonally within a layer or spanning across layers.
- The first player to align their symbols in a straight line wins the game. If all slots are filled without any player achieving this, the game is considered a draw.

## Strategy Tips

- **Forward Thinking**: Predict your opponent's moves and plan your strategy several moves in advance.
- **Central Control**: Taking control of the central slots in each layer can offer strategic advantages, providing more opportunities to align your symbols.
- **Vertical Strategy**: Remember, aligning symbols vertically through layers is a viable strategy, not just within the individual layers.

Prepare for a challenging and strategic gameplay experience with 3D Tic Tac Toe. Good luck, and may the best player win!
