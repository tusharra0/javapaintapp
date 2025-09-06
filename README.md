# Java Paint App 🎨

A feature-rich drawing application built with JavaFX that allows users to create, edit, and manipulate digital artwork with an intuitive graphical interface.

## Features

- **Shape Drawing**: Create various shapes including circles, rectangles, lines, and freehand drawings
- **Color Palette**: Choose from a wide range of colors for your artwork
- **File Operations**: Save and load your artwork in multiple formats
- **Interactive Tools**: Resize, move, and manipulate drawn elements
- **User-Friendly Interface**: Clean and intuitive design for smooth user experience

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK) 11 or higher**
- **JavaFX SDK** (if not included with your JDK)
- **IDE** (recommended: IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

### Installing JavaFX

If JavaFX is not included with your JDK:

1. Download JavaFX SDK from [OpenJFX website](https://openjfx.io/)
2. Extract the downloaded file
3. Note the path to the `lib` folder for later use

## Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/tusharra0/javapaintapp.git
   cd javapaintapp
   ```

2. **Compile the application:**
   ```bash
   javac -cp "path/to/javafx/lib/*" *.java
   ```

3. **Alternative: Using an IDE**
   - Open the project in your preferred IDE
   - Configure JavaFX in your project settings
   - Build the project

## Running the Application

### Method 1: Command Line

```bash
java -cp ".:path/to/javafx/lib/*" --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml Paint
```

### Method 2: Using IDE

1. Set up run configuration with JavaFX VM options:
   ```
   --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml
   ```
2. Set main class as `Paint`
3. Run the application


## Usage

1. **Launch the Application**: Run `Paint.java` using one of the methods above
2. **Select Tools**: Choose from various drawing tools in the toolbar
3. **Pick Colors**: Use the color palette to select your preferred colors
4. **Draw**: Click and drag on the canvas to create your artwork
5. **Save/Load**: Use the file menu to save your work or load existing files

### Key Controls

- **Mouse**: Primary drawing tool
- **File Menu**: Save, Open, New canvas
- **Tools Panel**: Switch between different drawing modes
- **Color Picker**: Change brush/shape colors

## Architecture

The application follows object-oriented design principles:

- **Model-View-Presenter (MVP)**: Separates business logic from UI
- **Command Pattern**: Enables undo/redo functionality
- **Factory Pattern**: Creates different shape objects
- **Observer Pattern**: Updates UI based on model changes

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add some new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request


## Acknowledgments

- Built as part of software engineering coursework at University of Toronto
- Inspired by classic paint applications and modern drawing tools
---

**Note**: Replace `path/to/javafx/lib` with the actual path to your JavaFX installation directory.
