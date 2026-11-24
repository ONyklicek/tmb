# TeamsKeepAlive

Malý nástroj v Javě, který udržuje desktopovou relaci aktivní, aby Microsoft Teams nepřepínal stav na **Pryč** (oranžová).

- Čistá Java, **bez externích závislostí** (jen JDK).
- Spustitelný JAR (`java -jar TeamsKeepAlive.jar`).
- Build běží na GitHub Actions.

## Jak to funguje

Jednou za minutu zkontroluje, jestli se pohnula myš. Pokud ne, posune kurzor o 1 px a zpět a stiskne klávesu **F15** (nic nedělá, ale systém ji počítá jako aktivitu). Když u počítače pracuješ sám, nezasahuje.

## Build lokálně

```bash
javac TeamsKeepAlive.java
jar --create --file TeamsKeepAlive.jar --main-class TeamsKeepAlive *.class
java -jar TeamsKeepAlive.jar      # ukončení Ctrl+C
```

Nebo si stáhni hotový `TeamsKeepAlive.jar` z **Actions → poslední běh → Artifacts**.

## Omezení

- Potřebuje **přihlášenou grafickou plochu**. Na zamčené obrazovce ani na headless serveru (Jakarta EE / JBoss běží bez plochy) nefunguje — `java.awt.Robot` by vyhodil `HeadlessException`.
- Na firemním počítači si ověř, že ti to interní pravidla dovolují.
