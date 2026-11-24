import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Udrzuje desktopovou relaci "aktivni", aby Teams neprepnul stav na Pryc (oranzova).
 *
 * Potrebuje bezici grafickou relaci (prihlaseneho uzivatele s plochou).
 * NEbezi na headless serveru (Jakarta EE / JBoss bezi bez plochy -> HeadlessException).
 *
 * Build:  javac TeamsKeepAlive.java && jar --create --file TeamsKeepAlive.jar --main-class TeamsKeepAlive *.class
 * Run:    java -jar TeamsKeepAlive.jar      (ukonceni Ctrl+C)
 */
public class TeamsKeepAlive {

    private static final long INTERVAL_MS = 60_000L;

    public static void main(String[] args) throws InterruptedException {
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

        Point last = MouseInfo.getPointerInfo().getLocation();
        System.out.println("Bezi... ukonceni Ctrl+C");

        while (true) {
            Thread.sleep(INTERVAL_MS);
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
}
