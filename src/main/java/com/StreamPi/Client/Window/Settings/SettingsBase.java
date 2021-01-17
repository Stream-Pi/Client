package com.StreamPi.Client.Window.Settings;

import java.io.File;

import com.StreamPi.Client.Connection.Client;
import com.StreamPi.Client.Connection.ClientListener;
import com.StreamPi.Client.IO.Config;
import com.StreamPi.Client.Info.ClientInfo;
import com.StreamPi.Client.Profile.ClientProfile;
import com.StreamPi.Client.Window.ExceptionAndAlertHandler;
import com.StreamPi.ThemeAPI.Theme;
import com.StreamPi.Util.Alert.StreamPiAlert;
import com.StreamPi.Util.Alert.StreamPiAlertType;
import com.StreamPi.Util.ComboBox.StreamPiComboBox;
import com.StreamPi.Util.ComboBox.StreamPiComboBoxFactory;
import com.StreamPi.Util.ComboBox.StreamPiComboBoxListener;
import com.StreamPi.Util.Exception.MinorException;
import com.StreamPi.Util.Exception.SevereException;
import com.StreamPi.Util.FormHelper.HBoxInputBox;
import com.StreamPi.Util.FormHelper.SpaceFiller;
import com.StreamPi.Util.Platform.ReleaseStatus;
import com.StreamPi.Util.StartAtBoot.SoftwareType;
import com.StreamPi.Util.StartAtBoot.StartAtBoot;
import com.gluonhq.attach.util.Services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.w3c.dom.Text;

public class SettingsBase extends VBox {
    private TextField serverPortTextField;
    private TextField serverHostNameOrIPTextField;

    private StreamPiComboBox<ClientProfile> clientProfileComboBox;
    private StreamPiComboBox<Theme> themeComboBox;

    private TextField displayWidthTextField;
    private TextField displayHeightTextField;

    private TextField nickNameTextField;

    private Button closeButton;
    private Button saveButton;
    private Button connectDisconnectButton;

    private ToggleButton startOnBootToggleButton;

    private ToggleButton showCursorToggleButton;
    private ToggleButton fullScreenToggleButton;

    private ClientListener clientListener;
    private ExceptionAndAlertHandler exceptionAndAlertHandler;

    private TextField themesPathTextField;
    private TextField iconsPathTextField;
    private TextField profilesPathTextField;

    public SettingsBase(ExceptionAndAlertHandler exceptionAndAlertHandler, ClientListener clientListener) {

        getStyleClass().add("settings_base");
        this.clientListener = clientListener;
        this.exceptionAndAlertHandler = exceptionAndAlertHandler;

        serverPortTextField = new TextField();
        serverHostNameOrIPTextField = new TextField();
        nickNameTextField = new TextField();

        clientProfileComboBox = new StreamPiComboBox<>();

        clientProfileComboBox.setStreamPiComboBoxFactory(new StreamPiComboBoxFactory<ClientProfile>()
        {
            @Override
            public String getOptionDisplayText(ClientProfile object) 
            {
                return object.getName();
            }
        });

        clientProfileComboBox.setStreamPiComboBoxListener(new StreamPiComboBoxListener<ClientProfile>(){
            @Override
            public void onNewItemSelected(ClientProfile selectedItem) 
            {
                clientListener.renderProfile(selectedItem);
            }
        });


        themeComboBox = new StreamPiComboBox<>();
        themeComboBox.setStreamPiComboBoxFactory(new StreamPiComboBoxFactory<Theme>()
        {
            @Override
            public String getOptionDisplayText(Theme object) 
            {
                return object.getShortName();
            }
        });

        displayHeightTextField = new TextField();
        displayWidthTextField = new TextField();

        themesPathTextField = new TextField();
        iconsPathTextField = new TextField();
        profilesPathTextField = new TextField();

        startOnBootToggleButton = new ToggleButton("Start On Boot");
        startOnBootToggleButton.managedProperty().bind(startOnBootToggleButton.visibleProperty());

        showCursorToggleButton = new ToggleButton("Show Cursor");
        showCursorToggleButton.managedProperty().bind(showCursorToggleButton.visibleProperty());

        fullScreenToggleButton = new ToggleButton("Full Screen");
        fullScreenToggleButton.managedProperty().bind(fullScreenToggleButton.visibleProperty());

        int prefWidth = 200;

        Label licenseLabel = new Label("This software is licensed to GNU GPLv3. Check StreamPi Server > Settings > About to see full License Statement.");
        licenseLabel.setWrapText(true);

        Label versionLabel = new Label(ClientInfo.getInstance().getReleaseStatus().getUIName() +" "+ClientInfo.getInstance().getVersion().getText());


        HBoxInputBox themesPathInputBox = new HBoxInputBox("Themes Path", themesPathTextField, prefWidth);
        themesPathInputBox.managedProperty().bind(themesPathInputBox.visibleProperty());


        HBoxInputBox iconsPathInputBox = new HBoxInputBox("Icons Path", iconsPathTextField, prefWidth);
        iconsPathInputBox.managedProperty().bind(iconsPathInputBox.visibleProperty());


        HBoxInputBox profilesPathInputBox = new HBoxInputBox("Profiles Path", profilesPathTextField, prefWidth);
        profilesPathInputBox.managedProperty().bind(profilesPathInputBox.visibleProperty());


        HBoxInputBox screenHeightInputBox = new HBoxInputBox("Screen Height", displayHeightTextField, prefWidth);
        screenHeightInputBox.managedProperty().bind(screenHeightInputBox.visibleProperty());


        HBoxInputBox screenWidthInputBox = new HBoxInputBox("Screen Width", displayWidthTextField, prefWidth);
        screenWidthInputBox.managedProperty().bind(screenWidthInputBox.visibleProperty());

        if(ClientInfo.getInstance().getPlatformType() == com.StreamPi.Util.Platform.Platform.ANDROID)
        {
            themesPathInputBox.setVisible(false);
            iconsPathInputBox.setVisible(false);
            profilesPathInputBox.setVisible(false);

            startOnBootToggleButton.setVisible(false);
            showCursorToggleButton.setVisible(false);
            fullScreenToggleButton.setVisible(false);

            screenHeightInputBox.setVisible(false);
            screenWidthInputBox.setVisible(false);
        }


        VBox vBox = new VBox(
                new HBoxInputBox("Nick Name", nickNameTextField, prefWidth),
                new HBoxInputBox("Host Name/IP", serverHostNameOrIPTextField, prefWidth),
                new HBoxInputBox("Port", serverPortTextField, prefWidth),
                new HBox(
                        new Label("Current Profile"),
                        new SpaceFiller(SpaceFiller.FillerType.HBox),
                        clientProfileComboBox
                ),
                new HBox(
                        new Label("Theme"),
                        new SpaceFiller(SpaceFiller.FillerType.HBox),
                        themeComboBox
                ),
                screenHeightInputBox,
                screenWidthInputBox,
                themesPathInputBox,
                iconsPathInputBox,
                profilesPathInputBox,
                startOnBootToggleButton,
                showCursorToggleButton,
                fullScreenToggleButton,
                licenseLabel,
                versionLabel
        );
        vBox.getStyleClass().add("settings_base_vbox");

        vBox.setSpacing(5.0);
        vBox.setPadding(new Insets(5));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStyleClass().add("settings_base_scrollpane");
        scrollPane.setContent(vBox);

        vBox.setMinWidth(300);

        vBox.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));


        Label settingsLabel = new Label("Settings");
        settingsLabel.setPadding(new Insets(5,0,0,5));
        settingsLabel.getStyleClass().add("settings_heading");

        saveButton = new Button("Save");
        saveButton.setOnAction(event->onSaveButtonClicked());

        closeButton = new Button("Close");

        connectDisconnectButton = new Button("Connect");
        connectDisconnectButton.setOnAction(event -> onConnectDisconnectButtonClicked());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> onExitButtonClicked());

        HBox buttonBar = new HBox(connectDisconnectButton, saveButton, exitButton, closeButton);
        buttonBar.setPadding(new Insets(0,5,5,0));
        buttonBar.setSpacing(5.0);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        setSpacing(5.0);

        getChildren().addAll(
                settingsLabel,
                scrollPane,
                buttonBar
        );
    }

    public void onExitButtonClicked()
    {
        Platform.exit();
        clientListener.onCloseRequest();
    }

    public void setDisableStatus(boolean status)
    {
        saveButton.setDisable(status);
        connectDisconnectButton.setDisable(status);
    }

    public void onConnectDisconnectButtonClicked()
    {
        try
        {
            if(clientListener.isConnected())
                clientListener.disconnect("Client disconnected from settings");
            else
                clientListener.setupClientConnection();
        }
        catch (SevereException e)
        {
            e.printStackTrace();
            exceptionAndAlertHandler.handleSevereException(e);
        }
    }

    public void setConnectDisconnectButtonStatus()
    {
        Platform.runLater(()->{
            setDisableStatus(false);

            System.out.println("q24qwdqwd : "+clientListener.isConnected());

            if(clientListener.isConnected())
                connectDisconnectButton.setText("Disconnect");
            else
                connectDisconnectButton.setText("Connect");
        });

    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void loadData() throws SevereException
    {
        nickNameTextField.setText(Config.getInstance().getClientNickName());

        serverHostNameOrIPTextField.setText(Config.getInstance().getSavedServerHostNameOrIP());
        serverPortTextField.setText(Config.getInstance().getSavedServerPort()+"");

        clientProfileComboBox.setOptions(clientListener.getClientProfiles().getClientProfiles());

        int ind = 0;
        for(int i = 0;i<clientProfileComboBox.getOptions().size();i++)
        {
            if(clientProfileComboBox.getOptions().get(i).getID().equals(clientListener.getCurrentProfile().getID()))
            {
                ind = i;
                break;
            }
        }

        clientProfileComboBox.setCurrentSelectedItemIndex(ind);

        themeComboBox.setOptions(clientListener.getThemes().getThemeList());

        int ind2 = 0;
        for(int i = 0;i<themeComboBox.getOptions().size();i++)
        {
            if(themeComboBox.getOptions().get(i).getFullName().equals(clientListener.getCurrentTheme().getFullName()))
            {
                ind2 = i;
                break;
            }
        }

        themeComboBox.setCurrentSelectedItemIndex(ind2);

        displayWidthTextField.setText(Config.getInstance().getStartupWindowWidth()+"");
        displayHeightTextField.setText(Config.getInstance().getStartupWindowHeight()+"");

        themesPathTextField.setText(Config.getInstance().getThemesPath());
        iconsPathTextField.setText(Config.getInstance().getIconsPath());
        profilesPathTextField.setText(Config.getInstance().getProfilesPath());

        startOnBootToggleButton.setSelected(Config.getInstance().isStartOnBoot());

        fullScreenToggleButton.setSelected(Config.getInstance().isFullscreen());
        showCursorToggleButton.setSelected(Config.getInstance().isShowCursor());
    }

    public void onSaveButtonClicked()
    {
        StringBuilder errors = new StringBuilder();

        int port = -1;
        try
        {
            port = Integer.parseInt(serverPortTextField.getText());

            if(port < 1024)
                errors.append("* Server IP should be above 1024.\n");
        }
        catch (NumberFormatException exception)
        {
            errors.append("* Server IP should be a number.\n");
        }

        double width = -1;
        try
        {
            width = Double.parseDouble(displayWidthTextField.getText());

            if(width < 0)
                errors.append("* Display Width should be above 0.\n");
        }
        catch (NumberFormatException exception)
        {
            errors.append("* Display Width should be a number.\n");
        }

        double height = -1;
        try
        {
            height = Double.parseDouble(displayHeightTextField.getText());

            if(height < 0)
                errors.append("* Display Height should be above 0.\n");
        }
        catch (NumberFormatException exception)
        {
            errors.append("* Display Height should be a number.\n");
        }

        if(serverHostNameOrIPTextField.getText().isBlank())
        {
            errors.append("* Server IP cannot be empty.\n");
        }

        if(nickNameTextField.getText().isBlank())
        {
            errors.append("* Nick name cannot be blank.\n");
        }


        if(!errors.toString().isEmpty())
        {
            exceptionAndAlertHandler.handleMinorException(new MinorException(
                    "You made mistakes",
                    "Please fix the errors and try again :\n"+errors.toString()
            ));
            return;
        }



        try {
            boolean toBeReloaded = false;

            boolean breakConnection = false;

            if(!Config.getInstance().getCurrentThemeFullName().equals(themeComboBox.getCurrentSelectedItem().getFullName()))
                toBeReloaded = true;

            Config.getInstance().setCurrentThemeFullName(themeComboBox.getCurrentSelectedItem().getFullName());

            if(width != Config.getInstance().getStartupWindowWidth() || height != Config.getInstance().getStartupWindowHeight())
                toBeReloaded = true;

            Config.getInstance().setStartupWindowSize(width, height);


            if(!Config.getInstance().getClientNickName().equals(nickNameTextField.getText()))
                breakConnection = true;

            Config.getInstance().setNickName(nickNameTextField.getText());

            if(port != Config.getInstance().getSavedServerPort() || !serverHostNameOrIPTextField.getText().equals(Config.getInstance().getSavedServerHostNameOrIP()))
                breakConnection = true;

            Config.getInstance().setServerPort(port);
            Config.getInstance().setServerHostNameOrIP(serverHostNameOrIPTextField.getText());

            boolean startOnBoot = startOnBootToggleButton.isSelected();
            
            if(Config.getInstance().isStartOnBoot() != startOnBoot)
            {
                if(ClientInfo.getInstance().getRunnerFileName() == null)
                {
                    new StreamPiAlert("Uh Oh", "No Runner File Name Specified as startup arguments. Cant set run at boot.", StreamPiAlertType.ERROR).show();
                    startOnBoot = false;
                }
                else
                {
                    StartAtBoot startAtBoot = new StartAtBoot(SoftwareType.CLIENT, ClientInfo.getInstance().getPlatformType());
                    if(startOnBoot)
                    {
                        startAtBoot.create(new File(ClientInfo.getInstance().getRunnerFileName()));
                    }
                    else
                    {
                        boolean result = startAtBoot.delete();
                        if(!result)
                            new StreamPiAlert("Uh Oh!", "Unable to delete starter file", StreamPiAlertType.ERROR).show();
                    }
                }
            }

            Config.getInstance().setStartOnBoot(startOnBoot);

            if(Config.getInstance().isFullscreen() != fullScreenToggleButton.isSelected() ||
                    Config.getInstance().isShowCursor() != showCursorToggleButton.isSelected())
                toBeReloaded = true;


            Config.getInstance().setFullscreen(fullScreenToggleButton.isSelected());
            Config.getInstance().setShowCursor(showCursorToggleButton.isSelected());



            if(!Config.getInstance().getThemesPath().equals(themesPathTextField.getText()))
                toBeReloaded = true;

            Config.getInstance().setThemesPath(themesPathTextField.getText());


            if(!Config.getInstance().getIconsPath().equals(iconsPathTextField.getText()))
                toBeReloaded = true;

            Config.getInstance().setIconsPath(iconsPathTextField.getText());

            if(!Config.getInstance().getProfilesPath().equals(profilesPathTextField.getText()))
                toBeReloaded = true;

            Config.getInstance().setProfilesPath(profilesPathTextField.getText());


            Config.getInstance().save();

            loadData();


            if(breakConnection)
            {
                if(clientListener.isConnected())
                    clientListener.disconnect("Client connection settings were changed.");
            }

            if(toBeReloaded)
            {
                clientListener.init();
                clientListener.renderRootDefaultProfile();
            }
        }
        catch (SevereException e)
        {
            e.printStackTrace();
            exceptionAndAlertHandler.handleSevereException(e);
        }
        catch (MinorException e)
        {
            e.printStackTrace();
            exceptionAndAlertHandler.handleMinorException(e);
        }
    }

}