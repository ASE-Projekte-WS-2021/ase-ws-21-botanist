# Android environment
# Appium API by https://appium.io/docs/en/about-appium/api/
import unittest
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
from appium.webdriver.common.touch_action import TouchAction
import os
import time

desired_caps = dict(
    platformName='Android',
    platformVersion='9',
    automationName='uiautomator2',
    deviceName='9WV4C18930036879',
    app=os.path.abspath('../app/build/outputs/apk/debug/app-debug.apk'),
    noReset=True
)


print(os.path.abspath('../app/build/outputs/apk/debug/app-debug.apk'))
driver = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)
driver.implicitly_wait(20)

time.sleep(5)

# Test: Starten der Anwendung, Bedienen der Menüelemente
# Anschließend PlantFragment öffnen
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchButton').click()
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='infoButton').click()
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchResultItem').click()
time.sleep(0.5)
driver.press_keycode(4) #back button press
time.sleep(1)

# Test: Suchbegriff eingeben, erstes Ergebnis anklicken
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchBar').click()
driver.press_keycode(33)  # keycode e
driver.press_keycode(46)  # keycode r
driver.press_keycode(37)  # keycode i
driver.press_keycode(31)  # keycode c
driver.press_keycode(29)  # keycode a
driver.press_keycode(66)  # keycode enter
time.sleep(1)

# Test: Standort im Suchergebnis anklicken, auf Karte anzeigen lassen
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchResultItem').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='alternativeLocationButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='areaButton').click()
time.sleep(1)

# Test: Karte verschieben, um Standort besser anzuzeigen
# deviceSize = driver.get_window_size()

# startX = deviceSize['width']/9
# endX = deviceSize['width']*7/9
# startY = deviceSize['height']/2
# endY = deviceSize['height']/2

# actions = TouchAction(driver)
# actions.long_press(None, startX, startY).move_to(None, endX, endY).release().perform()
# time.sleep(2)

# Test: Aktuellen Drawer öffnen, Favorit setzen
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawer').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawerPlants').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='favoritButton').click()
time.sleep(0.5)
driver.press_keycode(4) #back button press
time.sleep(1)

# Test: Favoritenliste und andere Tabs einsehen
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawer').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawerFavorit').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawerPlants').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='drawerArea').click()
time.sleep(0.5)
driver.press_keycode(4) #back button press

# Test: Infos ansehen (beide Seiten)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='infoButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='infoNavButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='infoNavButton').click()
time.sleep(0.5)

# Test: Map ansehen, MapMarker togglen
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='mapButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='mapMarkerButton').click()
time.sleep(0.5)
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='mapMarkerButton').click()
