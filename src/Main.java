

import javax.swing.SwingUtilities;

import gui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Jalankan UI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
