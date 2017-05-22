package sk.epholl.dissim.sem3.ui;

import sk.epholl.dissim.sem3.entity.deciders.OfficeAgentValueProvider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Condition;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decider;
import sk.epholl.dissim.sem3.entity.deciders.Worker1Decision;
import sk.epholl.dissim.util.deciders.Comparator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ConditionLogicModel {

    private Worker1Condition.Default defaultReturnValue;

    public ConditionLogicModel(
            JList<Worker1Condition> list,
            DefaultListModel<Worker1Condition> listModel,
            JButton upButton,
            JButton downButton,
            JButton deleteButton,
            JComboBox<OfficeAgentValueProvider> leftOperandsComboBox,
            JComboBox<Comparator> operatorsComboBox,
            JComboBox<OfficeAgentValueProvider> rightOperandsComboBox,
            JTextField leftOperandConstantTextField,
            JTextField rightOperandConstantTextField,
            JComboBox resultSpinner,
            JComboBox defaultReturnValueComboBox,
            JButton addButton) {

        defaultReturnValue = new Worker1Condition.Default((Worker1Decision) defaultReturnValueComboBox.getSelectedItem());
        listModel.addElement(defaultReturnValue);

        initOperantEditTexts(leftOperandsComboBox, leftOperandConstantTextField);

        initOperantEditTexts(rightOperandsComboBox, rightOperandConstantTextField);

        defaultReturnValueComboBox.addActionListener(e -> {

            defaultReturnValue.setReturnValue((Worker1Decision) defaultReturnValueComboBox.getSelectedItem());
            listModel.setElementAt(defaultReturnValue, listModel.size()-1);
        });

        addButton.addActionListener(e -> {
            OfficeAgentValueProvider leftOperand = null;
            if (leftOperandsComboBox.isEnabled()) {
                leftOperand = (OfficeAgentValueProvider)leftOperandsComboBox.getModel().getSelectedItem();
            }
            //Double leftOperandConstant = getSpinnerHasValue(leftOperandConstantTextField);
            Comparator operator = (Comparator) operatorsComboBox.getModel().getSelectedItem();
            OfficeAgentValueProvider rightOperand = null;
            if (rightOperandsComboBox.isEnabled()) {
                rightOperand = (OfficeAgentValueProvider) rightOperandsComboBox.getModel().getSelectedItem();
            }
            //Double rightOperandConstant = getSpinnerHasValue(rightOperandConstantTextField);

            Worker1Decision result = (Worker1Decision) resultSpinner.getSelectedItem();

            if (leftOperand == OfficeAgentValueProvider.constant) {
                leftOperand = OfficeAgentValueProvider.newConstant(parseDoubleFromTextField(leftOperandConstantTextField));
            }
            if (rightOperand == OfficeAgentValueProvider.constant) {
                rightOperand = OfficeAgentValueProvider.newConstant(parseDoubleFromTextField(rightOperandConstantTextField));
            }

            Worker1Condition condition = instantiateCondition(
                    leftOperand,
                    operator,
                    rightOperand,
                    result);

            listModel.add(listModel.size()-1, condition);
        });

        upButton.addActionListener(e -> {
            final int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1 && selectedIndex != listModel.size()-1) {
                if (selectedIndex > 0) {
                    Worker1Condition cond1 = listModel.get(selectedIndex);
                    Worker1Condition cond2 = listModel.get(selectedIndex-1);
                    listModel.set(selectedIndex-1, cond1);
                    listModel.set(selectedIndex, cond2);
                    list.setSelectedIndex(selectedIndex-1);
                }
            }
        });

        downButton.addActionListener(e -> {
            final int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1 && selectedIndex != listModel.size()-2) {
                if (selectedIndex < (listModel.size()-1)) {
                    Worker1Condition cond1 = listModel.get(selectedIndex);
                    Worker1Condition cond2 = listModel.get(selectedIndex+1);
                    listModel.set(selectedIndex+1, cond1);
                    listModel.set(selectedIndex, cond2);
                    list.setSelectedIndex(selectedIndex+1);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            if (listModel.isEmpty()) {
                return;
            }

            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                if (! (listModel.getElementAt(selectedIndex) instanceof Worker1Condition.Default)) {
                    listModel.removeElementAt(selectedIndex);
                    list.setSelectedIndex(selectedIndex);
                }
            }
        });

        defaultReturnValue.setReturnValue((Worker1Decision) defaultReturnValueComboBox.getSelectedItem());
        listModel.setElementAt(defaultReturnValue, listModel.size()-1);
    }

    private void initOperantEditTexts(JComboBox comboBox, JTextField textField) {

        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedItem() == OfficeAgentValueProvider.constant ) {
                textField.setEditable(true);
            } else {
                textField.setText("");
                textField.setEditable(false);
            }
        });

        textField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {}

                    @Override
                    public void removeUpdate(DocumentEvent e) {}

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        try {
                            Double.parseDouble(textField.getText());
                        } catch (NumberFormatException ex) {
                            textField.setText("0.0");
                        }
                    }
                });
    }

    protected Worker1Condition instantiateCondition(
            OfficeAgentValueProvider leftOperand,
            Comparator operator,
            OfficeAgentValueProvider rightOperand,
            Worker1Decision result) {

        return new Worker1Condition(
                leftOperand,
                operator,
                rightOperand,
                result);
    }

    private double parseDoubleFromTextField(JTextField textField) {
        try {
            double number = Double.parseDouble(textField.getText());
            return number;
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}