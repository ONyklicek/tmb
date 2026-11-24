import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Udrzuje desktopovou relaci "aktivni", aby Teams neprepnul stav na Pryc (oranzova).
 *
 * - Pri dvojkliku bezi na pozadi a ukaze ikonu v system tray (lista u hodin);
 *   pravy klik na ikonu -> Konec.
 * - Z konzole (nebo run.bat) vypisuje stav; ukonceni Ctrl+C.
 *
 * Potrebuje bezici grafickou relaci (prihlaseneho uzivatele s plochou).
 * NEbezi na headless serveru (Jakarta EE / JBoss bezi bez plochy -> HeadlessException).
 *
 * Build:  javac --release 8 TeamsKeepAlive.java
 *         jar --create --file Kel.jar --main-class TeamsKeepAlive *.class
 * Run:    java -jar Kel.jar
 */
public class TeamsKeepAlive {

    private static final long INTERVAL_MS = 60_000L;
    private static volatile boolean running = true;

    public static void main(String[] args) {
        final Robot robot;
        try {
            robot = new Robot();
        } catch (HeadlessException e) {
            System.err.println("Chybi graficka relace (headless prostredi). Nastroj potrebuje plochu.");
            return;
        } catch (AWTException e) {
            System.err.println("Nelze inicializovat Robot: " + e.getMessage());
            return;
        }

        setupTray();

        Point last = MouseInfo.getPointerInfo().getLocation();
        System.out.println("Bezi... (tray ikona: pravy klik -> Konec, nebo Ctrl+C)");

        while (running) {
            try {
                Thread.sleep(INTERVAL_MS);
            } catch (InterruptedException e) {
                break;
            }
            Point now = MouseInfo.getPointerInfo().getLocation();

            // Zasahuj jen kdyz uzivatel sam s mysi nehybal.
            if (now.equals(last)) {
                robot.mouseMove(now.x + 1, now.y);
                robot.delay(50);
                robot.mouseMove(now.x, now.y);
                robot.keyPress(KeyEvent.VK_F15);
                robot.keyRelease(KeyEvent.VK_F15);
            }
            last = MouseInfo.getPointerInfo().getLocation();
        }
    }

    /** Prida ikonu do system tray s polozkou Konec. Kdyz tray neni, program bezi dal bez ikony. */
    private static void setupTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray neni podporovan, bezim bez ikony.");
            return;
        }
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Dimension d = tray.getTrayIconSize();
            Image img = createIcon(d.width, d.height);

            PopupMenu menu = new PopupMenu();
            MenuItem quit = new MenuItem("Konec");
            quit.addActionListener(e -> {
                running = false;
                System.exit(0);
            });
            menu.add(quit);

            TrayIcon icon = new TrayIcon(img, "TeamsKeepAlive - udrzuje Teams aktivni", menu);
            icon.setImageAutoSize(true);
            tray.add(icon);
        } catch (AWTException e) {
            System.out.println("Nepodarilo se pridat tray ikonu: " + e.getMessage());
        }
    }

    /** Vykresli jednoduchou ikonu (fialove kolecko s "T"). */
    private static Image createIcon(int w, int h) {
        if (w <= 0) w = 16;
        if (h <= 0) h = 16;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x5B, 0x5F, 0xC7)); // Teams fialova
        g.fillOval(0, 0, w - 1, h - 1);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, h - 4)));
        FontMetrics fm = g.getFontMetrics();
        String s = "T";
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(s, x, y);
        g.dispose();
        return img;
    }
}
