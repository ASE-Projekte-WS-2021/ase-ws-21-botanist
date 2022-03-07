# Android environment
import unittest
from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
import os
import time

desired_caps = dict(
    platformName='Android',
    platformVersion='11',
    automationName='uiautomator2',
    deviceName='emulator-5554',
    app=os.path.abspath('../app/build/outputs/apk/debug/app-debug.apk'),
    noReset=True
)


print(os.path.abspath('../app/build/outputs/apk/debug/app-debug.apk'))
driver = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)
driver.implicitly_wait(20)

time.sleep(10)

el = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='searchButton')
el.click()
