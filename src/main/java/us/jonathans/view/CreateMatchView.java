package us.jonathans.view;

import us.jonathans.interface_adapter.cancel_match.CancelMatchController;
import us.jonathans.interface_adapter.cancel_match.CancelMatchState;
import us.jonathans.interface_adapter.cancel_match.CancelMatchViewModel;
import us.jonathans.interface_adapter.start_game.StartGameController;
import us.jonathans.interface_adapter.start_game.StartGameState;
import us.jonathans.interface_adapter.start_game.StartGameViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateMatchView extends JPanel implements PropertyChangeListener {
    private final static String viewName = "Create Match";
    private final JButton cancelMatchButton;
    private final JButton startMatchButton;
    private final JCheckBox usePhoneCheckbox;
    private final JTextField phoneInputField;
    private final JComboBox<String> selectEngineDropdown;
    private final JTextField usernameInputField;
    private final JLabel failReasonLabel;
    private boolean usePhoneNumber = false;

    public CreateMatchView(
            StartGameController startGameController,
            StartGameViewModel startGameViewModel,
            CancelMatchController cancelMatchController,
            CancelMatchViewModel cancelMatchViewModel
    ) {
        startGameViewModel.addPropertyChangeListener(this);
        cancelMatchViewModel.addPropertyChangeListener(this);
        setBorder(BorderFactory.createTitledBorder(viewName));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 1;
        gbc2.gridy = 0;


        JLabel usernameInputLabel = new JLabel("Username");
        usernameInputField = new JTextField(12);

        add(usernameInputLabel, gbc1);
        add(usernameInputField, gbc2);

        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 2;
        gbc3.gridy = 1;
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.gridx = 3;
        gbc4.gridy = 1;
        JLabel phoneInputLabel = new JLabel("Phone");
        phoneInputField = new JTextField(12);

        add(phoneInputLabel, gbc3);
        add(phoneInputField, gbc4);


        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.gridx = 3;
        gbc6.gridy = 0;
        usePhoneCheckbox = new JCheckBox("Send SMS");
        add(usePhoneCheckbox, gbc6);

        GridBagConstraints gbc7 = new GridBagConstraints();
        gbc7.gridx = 0;
        gbc7.gridy = 1;
        GridBagConstraints gbc8 = new GridBagConstraints();
        gbc8.gridx = 1;
        gbc8.gridy = 1;
        JLabel selectEngineLabel = new JLabel("Engine");
        selectEngineDropdown = new JComboBox<>(
                new String[]{"Randomizer3000", "minimax_easy", "minimax_medium", "minimax_hard"}
        );

        add(selectEngineLabel, gbc7);
        add(selectEngineDropdown, gbc8);

        failReasonLabel = new JLabel();
        GridBagConstraints gbc10 = new GridBagConstraints();
        gbc10.gridy = 2;
        gbc10.fill = GridBagConstraints.HORIZONTAL;
        gbc10.gridwidth = 4;
        gbc10.anchor = GridBagConstraints.CENTER;
        add(failReasonLabel, gbc10);

        GridBagConstraints gbc9 = new GridBagConstraints();
        gbc9.gridy = 3;
        gbc9.fill = GridBagConstraints.HORIZONTAL;
        gbc9.gridwidth = 4;

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        startMatchButton = new JButton("Start");
        cancelMatchButton = new JButton("Cancel");
        cancelMatchButton.setEnabled(false);

        buttons.add(cancelMatchButton);
        buttons.add(startMatchButton);

        add(buttons, gbc9);

        phoneInputField.setEnabled(usePhoneNumber);
        usePhoneCheckbox.addItemListener(e -> {
            usePhoneNumber = e.getStateChange() == ItemEvent.SELECTED;
            phoneInputField.setEnabled(usePhoneNumber);
        });

        startMatchButton.addActionListener(e -> {
            startGameController.execute(
                    usernameInputLabel.getText(),
                    phoneInputField.getText(),
                    usePhoneCheckbox.isSelected(),
                    (String) selectEngineDropdown.getSelectedItem()
            );
        });

        cancelMatchButton.addActionListener(a -> cancelMatchController.execute());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof StartGameState) {
            StartGameState startGameState = (StartGameState) evt.getNewValue();
            if (startGameState.isSuccessful()) {
                cancelMatchButton.setEnabled(true);
                startMatchButton.setEnabled(false);
                usePhoneCheckbox.setEnabled(false);
                phoneInputField.setEnabled(false);
                selectEngineDropdown.setEnabled(false);
                usernameInputField.setEnabled(false);
                failReasonLabel.setText("");
            } else {
                failReasonLabel.setText(startGameState.getFailReason());
            }
        } else if (evt.getNewValue() instanceof CancelMatchState) {
            cancelMatchButton.setEnabled(false);
            startMatchButton.setEnabled(true);
            usePhoneCheckbox.setEnabled(true);
            phoneInputField.setEnabled(usePhoneNumber);
            selectEngineDropdown.setEnabled(true);
            usernameInputField.setEnabled(true);
            failReasonLabel.setText("");
        }
    }
}
