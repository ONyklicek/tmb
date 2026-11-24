# TeamsKeepAlive

Malý nástroj v Javě, který udržuje desktopovou relaci aktivní, aby Microsoft Teams nepřepínal stav na **Pryč** (oranžová).

- Čistá Java, **bez externích závislostí** (jen JDK).
- Spustitelný JAR (`java -jar Kel.jar`).
- Build běží na GitHub Actions.

## Jak to funguje

Jednou za minutu zkontroluje, jestli se pohnula myš. Pokud ne, posune kurzor o 1 px a zpět a stiskne klávesu **F15** (nic nedělá, ale systém ji počítá jako aktivitu). Když u počítače pracuješ sám, nezasahuje.

## Spuštění

- **Dvojklik na `Kel.jar`** — běží na pozadí a ukáže **ikonu v system tray** (lišta u hodin); pravý klik na ikonu → **Konec**.
- **`run.bat`** — spustí program v okně konzole (uvidíš výpis, ukončení Ctrl+C). `Kel.jar` a `run.bat` musí být ve stejné složce.

Pokud dvojklik na `.jar` otevře WinRAR/7-Zip místo spuštění, nemáš `.jar` svázaný s Javou — použij `run.bat`.

## Build lokálně

```bash
javac --release 8 TeamsKeepAlive.java
jar --create --file Kel.jar --main-class TeamsKeepAlive *.class
java -jar Kel.jar      # ukončení Ctrl+C
```

JAR cílí na **Javu 8**, takže poběží na Javě 8 i novější.

Nebo si stáhni hotový `Kel.jar` z **Actions → poslední běh → Artifacts** nebo z **Releases**.

## Omezení

- Potřebuje **přihlášenou grafickou plochu**. Na zamčené obrazovce ani na headless serveru (Jakarta EE / JBoss běží bez plochy) nefunguje — `java.awt.Robot` by vyhodil `HeadlessException`.
- Na firemním počítači si ověř, že ti to interní pravidla dovolují.
