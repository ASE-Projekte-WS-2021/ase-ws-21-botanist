<img src="Botanist_Logo_g.svg" width="400"></img>
# UR Botanist 
## ...und der Besuch im Botanischen Garten

_Unser namenloser Nutzer sucht gerne den botanische Garten auf. Bei seinem Besuch an der Universität Regensburger nutzt er sie er/sie die Gelegenheit und geht diesem Hobby nach, denn auf dem Gelände befindet sich solch einer. Im botanischen Garten findet der Nutzer eine Pflanze, die ihm/ihr gut gefällt. Er/sie möchte mehr über die Pflanze erfahren und öffnet die UR Botanist App. Dort findet der Nutzer heraus, dass man sich gerade in der europäischen Abteilung befindet. Da die Person noch mehr wissen möchte sucht er nach der Pflanze in der App und findet noch Lebensform und weitere Standorte heraus. Er/sie überlegt sich die Pflanze für den eigenen Garten anzuschaffen, ist sich aber unsicher welche benachbarten Pflanzen am besten wären. Die Person erinnert sich an die Standorte der Pflanze und um einen besseren Vergleich zu haben sucht diese mit Hilfe in der Anwendung dargestellten Karte auf. Danach schließt er zufrieden die App (und genießt weiter den Garten)._

---

Das beschreibt in etwa den Nutzen der Anwendung. Es handelt sich um einen kleinen Begleiter, der den Besuch im Botanischen Garten der Uni Regensburg durch seinen Nutzen bereichert. Er ist wie ein kleines Nachschlagewerk und erlaubt es dem Nutzer die Lieblinge der hiesigen Flora zu vermerken. In keiner Hinsicht jedoch darf die App vom Gartenbesuch ablenken. Das würde das Erlebnis schmälern und somit hätte die Anwendung ihren Zweck verfehlt. Deshalb heißt das Motto: _**Klein** und **handlich**, aber immer zur Stelle._

---

### Manuelles Bauen auf Basis des Repositorys:
Benötigt eine Internet Verbindung zum Download der Dependencies.

Keine speziellen Vorbereitung nötig.

Beim mehrmaligem bauen kann es zu einem Build-Error "Attempt to recreate a file for type io.realm.com_example_urbotanist_drawerfragments_plant_PlantRealmProxy" kommen, der bei erneutem Build Versuch jedoch nicht mehr auftritt.

Bauen einer Debug-App:
gradlew assembleDebug

Bauen einer Release-App:
gradlew assembleRelease

## Checkstyle

gradlew checkstyle

## Appium App Testing

Starten des Appium Servers über die Appium Server GUI oder per Terminal.

Im Python Skript angleichen: 
desiredCaps dictionary:

platFormVersion: Android-Version des Geräts für den Test

deviceName: ID des Geräts (ID des zum Debugging angeschlossenen Geräts setzt Installation von ADB voraus, ID kann danach in einer Konsole via "adb devices" abgerufen werden).

Anschließend kann das Skript (ase-ws-21-botanist\appium-automated-testing\test.py) gestartet werden.

---

### Arbeitsverteilung im Team:
**Teambeschreibung**

5 Studenten im Masterkurs Advanced Software Engineering der Medieninformatik.

**Aufgabenverteilung bzw. Zuständigkeitsbereiche:**

Martina und Polina: Automated Testing, Datenbank (Suchfunktionalität, Realmintegrierung), Onboarding

Sabrina: Karte (Marker, Highlighting, Positionsbestimmung)

Anton: Design (Entwürfe und xml-Design), Splashscreen

Johannes: Technisches Design, Architektur Umsetzung, Github Actions, Hilfe für alle


Aber auf Absprache haben alle allen geholfen.

---

### Link einfügen
