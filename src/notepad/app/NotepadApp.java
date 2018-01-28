/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad.app;

import com.sun.glass.ui.Robot;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
/*import java.awt.AWTException;
import java.awt.Robot;*/
//import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.awt.SunHints;

/**
 *
 * @author Vargos
 */
public class NotepadApp extends Application {

    private FileChooser fileChooser = new FileChooser();
    private File file;
    private boolean isNewFile = false;
    NotepadController rootObj;
    private long stringHash = 0L;
    //private boolean enableUndo = false;
    private Stack<String> historyContent = new Stack();

    //private String oldText = "";
    @Override
    public void start(Stage stage) throws Exception {
        rootObj = new NotepadController();

        Scene scene = new Scene(rootObj);
        //((Stage) rootObj.getScene().getWindow()).setTitle("Untitled - Notepad"); //set Stage title
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));

        handleFileMenu();
        handleEditMenu();
        handleHelpMenu();
        historyContent.push("");
        Platform.setImplicitExit(false); //For prevent closing
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                makeExitAlert();
            }
        });

        rootObj.enteredText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //rootObj.undoMenuBtn.setDisable(false);
                if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.SPACE) {
                    String oldContentStr = rootObj.enteredText.getText();
                    historyContent.push(oldContentStr);
                }
            }
        });
        /*rootObj.enteredText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                
            }
        });*/
        stage.setScene(scene);
        stage.show();
    }

    private void handleFileMenu() {
        rootObj.newMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newFile();
            }
        });

        rootObj.openMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openFile();
            }
        });

        rootObj.saveMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveFile();
            }
        });

        rootObj.exitMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                makeExitAlert();
            }
        });
    }

    private void newFile() {
        if (rootObj.enteredText.getText().hashCode() != stringHash) {
            makeSaveAlert();
        }
        rootObj.enteredText.clear();
        ((Stage) rootObj.getScene().getWindow()).setTitle("Untitled - Notepad"); //set Stage title 
        historyContent.clear();
        historyContent.push("");
        isNewFile = false; //flag for later
    }

    private void openFile() {
        //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        if (rootObj.enteredText.getText().hashCode() != stringHash) {
            makeSaveAlert();
        }
        file = fileChooser.showOpenDialog(rootObj.getScene().getWindow());
        if (file != null) {
            Stage stage = (Stage) rootObj.getScene().getWindow();
            stage.setTitle(file.getName());
            BufferedReader lineReader = null;
            try {
                String fileLine;
                lineReader = new BufferedReader(new FileReader(file));
                System.out.println(lineReader);
                rootObj.enteredText.clear();
                while ((fileLine = lineReader.readLine()) != null) {
                    rootObj.enteredText.appendText(fileLine + "\n");
                }
                stringHash = rootObj.enteredText.getText().hashCode();
                isNewFile = true;
                //enableUndo = false;
                historyContent.clear();
                historyContent.push(rootObj.enteredText.getText());

                //rootObj.undoMenuBtn.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (!isNewFile) {

            file = fileChooser.showSaveDialog(null);
        }

        //String content = rootObj.enteredText.getText();
        if (file != null) {
            Stage stage = (Stage) rootObj.enteredText.getScene().getWindow();
            stage.setTitle(file.getName());
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                String filePath = file.getAbsoluteFile().getPath();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                PrintStream printStream = new PrintStream(fileOutputStream);
                for (String line : rootObj.enteredText.getText().split("\\n")) {
                    printStream.println(line);
                }
                //bufferedWriter.write(content);
                printStream.close();

                stringHash = rootObj.enteredText.getText().hashCode();
                isNewFile = true;
                historyContent.clear();
                historyContent.push(rootObj.enteredText.getText());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void makeExitAlert() {
        if (rootObj.enteredText.getText().hashCode() != stringHash) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Notepad");
            alert.setHeaderText("Do you want to save the file?");
            //alert.setContentText("Choose your option.");

            //ButtonType buttonTypeOne = new ButtonType("One");
            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeNoSave = new ButtonType("Don't Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            /*alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                Platform.exit();
            }
        });*/

            alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSave) {
                // ... user chose "Save"
                saveFile();
                Platform.exit();
            } else if (result.get() == buttonTypeNoSave) {
                // ... user chose "Don't save"
                Platform.exit();
            } else {
                // ... user chose CANCEL or closed the dialog
                //Platform.exit();
            }
        } else {
            Platform.exit();

        }
    }

    private void makeSaveAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Notepad");
        alert.setHeaderText("Do you want to save the file?");
        //alert.setContentText("Choose your option.");

        //ButtonType buttonTypeOne = new ButtonType("One");
        ButtonType buttonTypeSave = new ButtonType("Save");
        ButtonType buttonTypeNoSave = new ButtonType("Don't Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeSave) {
            // ... user chose "Save"
            saveFile();
            //Platform.exit();
        } else if (result.get() == buttonTypeNoSave) {
            // ... user chose "Don't save"
            //Platform.exit();
        } else {
            // ... user chose CANCEL or closed the dialog
            //Platform.exit();
        }
    }

    private void handleEditMenu() {
        rootObj.copyMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                copyText();
            }
        });

        rootObj.cutMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cutText();
            }
        });

        rootObj.pasteMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pasteText();
            }
        });

        rootObj.deleteMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteText();
            }
        });

        rootObj.undoMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                undoText();
            }
        });

        rootObj.selectAllMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectAllText();
            }
        });
    }

    private void copyText() {
        if (!rootObj.enteredText.getSelectedText().isEmpty()) {
            String copiedText = rootObj.enteredText.getSelectedText();
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(copiedText);
            clipboard.setContent(content);

        }

    }

    private void cutText() {

        if (!rootObj.enteredText.getSelectedText().isEmpty()) {

            String cutText = rootObj.enteredText.getSelectedText();
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(cutText);
            clipboard.setContent(content);

            IndexRange range = rootObj.enteredText.getSelection();
            /*String origText = rootObj.enteredText.getText();
            String firstPart = rootObj.enteredText.getText().substring(0, range.getStart());//substring(origText, 0, range.getStart());
            String lastPart = rootObj.enteredText.getText().substring(range.getEnd(), origText.length());
            rootObj.enteredText.setText(firstPart + lastPart);*/

            rootObj.enteredText.replaceSelection("");
            rootObj.enteredText.positionCaret(range.getStart());

        }
    }

    private void pasteText() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasContent(DataFormat.PLAIN_TEXT)) {
            String clipboardText = clipboard.getString();
            int location = rootObj.enteredText.getCaretPosition();
            /*rootObj.enteredText.replaceSelection("");
            String origText = rootObj.enteredText.getText();
            String firstPart = rootObj.enteredText.getText().substring(0, location);//substring(origText, 0, range.getStart());
            String lastPart = rootObj.enteredText.getText().substring(location, origText.length());
            rootObj.enteredText.setText(firstPart + clipboardText + lastPart);
            rootObj.enteredText.positionCaret(location + clipboardText.length());*/

            rootObj.enteredText.replaceSelection(clipboardText);
            historyContent.push(rootObj.enteredText.getText());

        }
    }

    private void deleteText() {

        IndexRange range = rootObj.enteredText.getSelection();
        String origText = rootObj.enteredText.getText();
        String firstPart = rootObj.enteredText.getText().substring(0, range.getStart());//substring(origText, 0, range.getStart());
        String lastPart = rootObj.enteredText.getText().substring(range.getEnd(), origText.length());
        rootObj.enteredText.setText(firstPart + lastPart);
        rootObj.enteredText.positionCaret(range.getStart());

        historyContent.push(rootObj.enteredText.getText());
    }

    private void selectAllText() {
        rootObj.enteredText.selectAll();
    }

    private void undoText() {

        if (historyContent.size() > 0) {
            int caretPos = rootObj.enteredText.getCaretPosition();
            rootObj.enteredText.setText(historyContent.pop());
            rootObj.enteredText.positionCaret(rootObj.enteredText.getText().length());
        }
        if (rootObj.enteredText.getText().isEmpty() == true) {
            historyContent.push("");
        }

        if (historyContent.isEmpty()) {
            String oldContentStr = rootObj.enteredText.getText();
            historyContent.push(oldContentStr);
        }
        // try {
        //Robot robot = new Robot();
        /* FXRobot robot = FXRobotFactory.createRobot(rootObj.getScene());
        robot.keyPress(javafx.scene.input.KeyCode.CONTROL);
        robot.keyPress(javafx.scene.input.KeyCode.Z);
        robot.keyRelease(javafx.scene.input.KeyCode.CONTROL);
        robot.keyRelease(javafx.scene.input.KeyCode.Z);/*
        /*
robot.keyPress(KeyEvent.VK_Z);
// CTRL+Z is now pressed (receiving application should see a "key down" event.)
        robot.keyRelease(KeyEvent.VK_Z);
        robot.keyRelease(KeyEvent.VK_CONTROL);*/
//robot.keyPress(javafx.scene.input.KeyCode.Z.impl_getChar());
        //robot.keyPress(KeyEvent.VK_Z);

        //robot.keyRelease(KeyEvent.VK_CONTROL);
        //robot.keyRelease(KeyEvent.VK_Z);
        //rootObj.enteredText.undo();
        //rootObj.enteredText.undo
        /*} catch (AWTException ex) {
            Logger.getLogger(NotepadApp.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    private void handleHelpMenu() {
        rootObj.aboutMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutMeDialog();
            }
        });
    }

    private void aboutMeDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About notepad");
        alert.setHeaderText("A Notepad created using JavaFX");
        alert.setContentText("Created Ahmed Ehab the best in intake 38 JAVA MAD :D");

        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
