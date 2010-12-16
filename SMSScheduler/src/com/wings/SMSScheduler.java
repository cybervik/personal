/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wings;

import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.table.DefaultTableModel;
import com.sun.lwuit.table.Table;
import com.sun.lwuit.table.TableModel;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class SMSScheduler extends MIDlet {

    private static final String APP_NAME = "SMS Scheduler";
    private Form form;
    private Table schedules;

    public void startApp() {
        initialize();
        addHeader();
        addSchedulesList();
        addMenuOptions();
        form.show();
    }

    private void addSchedulesList() {
        schedules = new Table(new TableModel() {

            String [][] data = new Sring {{"Anniv","Today"},{"Pay bills","10-Dec-2010"},{"Pay Utils","Tomorrow"}};

            public int getRowCount() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getColumnCount() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getColumnName(int i) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isCellEditable(int row, int column) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Object getValueAt(int row, int column) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setValueAt(int row, int column, Object o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void addDataChangeListener(DataChangedListener d) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void removeDataChangeListener(DataChangedListener d) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        schedules.
        form.addComponent(schedules);
    }

    private void initialize() {
        Display.init(this);
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    private void addMenuOptions() {
        addCommand("Details");
        addCommand("About");
        addCommand("Help");
        addCommand("Exit");
    }

    private void addHeader() {
        addLabel(APP_NAME);
    }

    private void addLabel(String title) {
        form.addComponent(new Label(title));
    }

    private void addCommand(String aboutCommandName) {
        form.addCommand(new Command(aboutCommandName));
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
