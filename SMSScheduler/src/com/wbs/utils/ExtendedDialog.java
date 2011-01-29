/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.utils;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Border;
import com.wbs.constant.Color;
import com.wbs.form.decorator.FontUtil;

/**
 *
 * @author Vikram S
 */
public class ExtendedDialog extends Dialog {

    private Label titleComponent;
    private TextArea messageComponent;
    private Command okCmd;
    private int topMargin = 1;

    public ExtendedDialog() {
        this("");
    }

    public ExtendedDialog(String title) {
        this(title, "");
    }

    public ExtendedDialog(String title, String message) {
        this(title, message, Dialog.TYPE_INFO);
    }

    public ExtendedDialog(String title, String message, int type) {
        initStyle();
        initTitle(title);
        initMessage(message);
        setDialogType(type);
    }

    public void setDialogType(int type) {
        super.setDialogType(type);
        okCmd = new Command("Ok");
        switch (type) {
            case TYPE_CONFIRMATION:
                addCommand(new Command("Cancel"));
            default:
                addCommand(okCmd);
        }
    }

    private void initBackground() {
        getStyle().setBgColor(Color.WHITE);
    }

    private void initDefaults() {
        setAutoDispose(true);
        setDialogType(Dialog.TYPE_INFO);
        setScrollableX(false);
        setScrollableY(true);
    }

    private void initMenuStyle() {
        final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer(false);
        defaultListCellRenderer.getStyle().setFont(FontUtil.getMediumNormalFont());
        defaultListCellRenderer.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        defaultListCellRenderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        setMenuCellRenderer(defaultListCellRenderer);
    }

    private void initMessage(String message) {
        if (!UiUtil.isEmpty(message)) {
            messageComponent = new TextArea(message);
            messageComponent.getStyle().setFont(FontUtil.getMediumNormalFont());
            messageComponent.getStyle().setBorder(Border.createEmpty());
            final Font mediumNormalFont = FontUtil.getMediumNormalFont();
            final int columns = (Display.getInstance().getDisplayWidth() / mediumNormalFont.stringWidth("W"));
            messageComponent.getSelectedStyle().setFont(mediumNormalFont);
            messageComponent.getSelectedStyle().setBorder(Border.createEmpty());
            messageComponent.setRows(3);
            messageComponent.setGrowByContent(false);
            messageComponent.setEditable(false);
            messageComponent.setColumns(columns);
            addComponent(messageComponent);
            topMargin = Display.getInstance().getDisplayHeight() / 2;
        }
    }

    private void initSoftButtonsStyle() {
        getSoftButtonStyle().setFont(FontUtil.getMediumNormalFont());
        for (int i = 0; i < 3; i++) {
            try {
                final Button softButton = getSoftButton(i);
                softButton.getStyle().setFont(FontUtil.getMediumNormalFont());
            } catch (Exception e) {
                //                Logger.logError(e.getMessage());
            }
        }
    }

    private void initTitle(String title) {
        if (!UiUtil.isEmpty(title)) {
            titleComponent = new Label(title);
            titleComponent.getStyle().setFont(FontUtil.getMediumBoldFont());
            setTitleComponent(titleComponent);
        }
    }

    private void initStyle() {
        initDefaults();
        initBackground();
        initSoftButtonsStyle();
        initMenuStyle();
    }

    public boolean showExtendedDialog() {
        final Command selectedCmd = show(topMargin, 1, 1, 1, true, true);
        System.out.println("\"Ok\".equals(selectedCmd.getCommandName()) -> " + ("Ok".equals(selectedCmd.getCommandName())));
        return "Ok".equals(selectedCmd.getCommandName()) ? true : false;
    }

    public void setMessage(String message) {
        messageComponent.setText(message);
    }
}
