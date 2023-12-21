import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class OutputWindow {

    private StyledDocument doc;
    private Style style;
    private JTextPane textPane;
    private Color green;
    private Color gold;
    private Color red;
    private Color purple;
    private Color cyan;

    public OutputWindow() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // causes program to end when window is X'd out
        frame.setSize(600, 600); // window size
        frame.setLocation(300, 50); // where on screen window appears
        textPane = new JTextPane(); // panel that can handle custom text
        textPane.setEditable(false); // prevents user from typing into window
        doc = textPane.getStyledDocument(); // call getter method for panel's style doc
        style = doc.addStyle("my style", null); // add a custom style to the doc
        StyleConstants.setFontSize(style, 25); // apply font size to custom style
        StyleConstants.setFontFamily(style, "Comic Sans MS"); // apply font type to custom style
        frame.add(textPane); // add the panel to the frame
        frame.setVisible(true); // display the frame on screen
        // custom colors for later use
        green = new Color(31, 176, 53);
        gold = new Color(232, 196, 22);
        red = new Color(225, 54, 54);
        purple = new Color(166, 94, 204);
        cyan = new Color(146, 222, 218);
    }

    public void addTextToWindow(String text, Color color) {
        StyleConstants.setForeground(style, color); // apply color to custom style
        try {
            doc.insertString(doc.getLength(), text, style); } // insert text at end the panel
        catch (Exception e) { }
    }

    public void clear() {
        textPane.setText("");  // set panel's text to empty string to "reset it"
    }
}