package utils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

public class AutoSuggestComboBox {
    public static DefaultComboBoxModel<String> getSuggestedModel(List<String> list, String text) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        var sortedList = new TreeSet<>(list.stream().map(String::strip).sorted().toList());
        model.addElement(text);
        sortedList.remove(text);

        for (String s : sortedList) {
            if (s.toLowerCase().contains(text.toLowerCase())) {
                model.addElement(s);
            }
        }
        return model;
    }

    public static JTextField create(JComboBox<String> comboBox, List<String> list) {
        return create(comboBox, 0, (i)-> list);
    }

    public static JTextField create(JComboBox<String> comboBox, int col, Function<Integer, List<String>> function) {
        return create(comboBox, col, function, new AutoSuggestBoxOption());
    }

    public static JTextField create(JComboBox<String> comboBox, int col, Function<Integer, List<String>> function, AutoSuggestBoxOption option) {
        var textField = (JTextField) comboBox.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                var list = function.apply(col);
                autoSuggestBoxEventHandler(e, comboBox, list);
            }
        });

        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                var list = function.apply(col);
                comboBox.setModel(getSuggestedModel(list, textField.getText()));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        return textField;
    }

    private static void autoSuggestBoxEventHandler(KeyEvent e, JComboBox<String> comboBox, List<String> list) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> {
                return;
            }
            case KeyEvent.VK_ENTER -> {
                comboBox.hidePopup();
                return;
            }
        }

        var textField = (JTextField) comboBox.getEditor().getEditorComponent();
        comboBox.setModel(AutoSuggestComboBox.getSuggestedModel(list, textField.getText()));
        if (textField.getText().length() == 0) {
            comboBox.hidePopup();
        } else {
            comboBox.showPopup();
        }
    }

    public static JTextField createIgnored(JComboBox<String> comboBox, List<String> list) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        list.forEach(model::addElement);
        comboBox.setModel(model);
        return (JTextField) comboBox.getEditor().getEditorComponent();
    }

    static public JTextField createAutoSuggest(JComboBox<String> comboBox, int col,
                                                 Function<Integer, List<String>> function) {
        return createAutoSuggest(comboBox, col, function, new AutoSuggestBoxOption());
    }

    static public JTextField createAutoSuggest(JComboBox<String> comboBox, int col,
                                                 Function<Integer, List<String>> function, AutoSuggestBoxOption option) {
        var textField = create(comboBox, col, function);

        return textField;
    }
}
