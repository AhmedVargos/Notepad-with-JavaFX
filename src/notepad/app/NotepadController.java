package notepad.app;

import javafx.scene.input.*;
import java.lang.*;
import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

public class NotepadController extends BorderPane {

    protected final MenuBar menuBar;
    protected final Menu menu;
    protected final MenuItem newMenuBtn;
    protected final MenuItem openMenuBtn;
    protected final MenuItem saveMenuBtn;
    protected final SeparatorMenuItem separatorMenuItem;
    protected final MenuItem exitMenuBtn;
    protected final Menu menu0;
    protected final MenuItem undoMenuBtn;
    protected final SeparatorMenuItem separatorMenuItem0;
    protected final MenuItem copyMenuBtn;
    protected final MenuItem cutMenuBtn;
    protected final MenuItem pasteMenuBtn;
    protected final MenuItem deleteMenuBtn;
    protected final SeparatorMenuItem separatorMenuItem1;
    protected final MenuItem selectAllMenuBtn;
    protected final Menu menu1;
    protected final MenuItem aboutMenuBtn;
    protected final TextArea enteredText;

    public NotepadController() {

        menuBar = new MenuBar();
        menu = new Menu();
        newMenuBtn = new MenuItem();
        openMenuBtn = new MenuItem();
        saveMenuBtn = new MenuItem();
        separatorMenuItem = new SeparatorMenuItem();
        exitMenuBtn = new MenuItem();
        menu0 = new Menu();
        undoMenuBtn = new MenuItem();
        separatorMenuItem0 = new SeparatorMenuItem();
        copyMenuBtn = new MenuItem();
        cutMenuBtn = new MenuItem();
        pasteMenuBtn = new MenuItem();
        deleteMenuBtn = new MenuItem();
        separatorMenuItem1 = new SeparatorMenuItem();
        selectAllMenuBtn = new MenuItem();
        menu1 = new Menu();
        aboutMenuBtn = new MenuItem();
        enteredText = new TextArea();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        BorderPane.setAlignment(menuBar, javafx.geometry.Pos.CENTER);

        menu.setMnemonicParsing(false);
        menu.setText("File");

        newMenuBtn.setMnemonicParsing(false);
        newMenuBtn.setText("New");
        newMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        openMenuBtn.setMnemonicParsing(false);
        openMenuBtn.setText("Open");
        openMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        saveMenuBtn.setMnemonicParsing(false);
        saveMenuBtn.setText("Save");
        saveMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        separatorMenuItem.setMnemonicParsing(false);
        separatorMenuItem.setText("Exit Border");

        exitMenuBtn.setMnemonicParsing(false);
        exitMenuBtn.setText("Exit");

        menu0.setMnemonicParsing(false);
        menu0.setText("Edit");

        undoMenuBtn.setMnemonicParsing(false);
        undoMenuBtn.setText("Undo");
        undoMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));

        separatorMenuItem0.setMnemonicParsing(false);
        separatorMenuItem0.setText("separator");

        copyMenuBtn.setMnemonicParsing(false);
        copyMenuBtn.setText("Copy");
        copyMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        cutMenuBtn.setMnemonicParsing(false);
        cutMenuBtn.setText("Cut");
        cutMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        pasteMenuBtn.setMnemonicParsing(false);
        pasteMenuBtn.setText("Paste");
        pasteMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

        deleteMenuBtn.setMnemonicParsing(false);
        deleteMenuBtn.setText("Delete");

        separatorMenuItem1.setMnemonicParsing(false);
        separatorMenuItem1.setText("separator");

        selectAllMenuBtn.setMnemonicParsing(false);
        selectAllMenuBtn.setText("Select All");
        selectAllMenuBtn.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        menu1.setMnemonicParsing(false);
        menu1.setText("Help");

        aboutMenuBtn.setMnemonicParsing(false);
        aboutMenuBtn.setText("About Notepad");
        setTop(menuBar);

        BorderPane.setAlignment(enteredText, javafx.geometry.Pos.CENTER);
        enteredText.setPrefHeight(200.0);
        enteredText.setPrefWidth(200.0);
        setCenter(enteredText);

        menu.getItems().add(newMenuBtn);
        menu.getItems().add(openMenuBtn);
        menu.getItems().add(saveMenuBtn);
        menu.getItems().add(separatorMenuItem);
        menu.getItems().add(exitMenuBtn);
        menuBar.getMenus().add(menu);
        menu0.getItems().add(undoMenuBtn);
        menu0.getItems().add(separatorMenuItem0);
        menu0.getItems().add(copyMenuBtn);
        menu0.getItems().add(cutMenuBtn);
        menu0.getItems().add(pasteMenuBtn);
        menu0.getItems().add(deleteMenuBtn);
        menu0.getItems().add(separatorMenuItem1);
        menu0.getItems().add(selectAllMenuBtn);
        menuBar.getMenus().add(menu0);
        menu1.getItems().add(aboutMenuBtn);
        menuBar.getMenus().add(menu1);

    }
}
