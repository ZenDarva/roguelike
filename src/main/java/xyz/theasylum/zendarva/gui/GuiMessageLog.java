package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.drawable.widget.WidgetTextBox;
import xyz.theasylum.zendarva.event.EventAddMessage;
import xyz.theasylum.zendarva.event.EventBus;

import java.awt.*;

public class GuiMessageLog extends GuiWindow {


    String lastMessage="";

    public GuiMessageLog(int width, int height) {
        super(width, height);
        EventBus.instance().registerHandler(this);
        WidgetTextBox textBox = new WidgetTextBox(this,width,height);
        textBox.setVisible(true);
        textBox.setText("test\r\nTest2,\n\rLine3\nline4");
        addWidget(textBox);
    }

    @Override
    public void drawBackground(Graphics g) {

    }

    @Override
    public void drawForeground(Graphics g) {

    }

    public void handleNewMessage(EventAddMessage e){
        lastMessage=e.getMessage();
    }
}
