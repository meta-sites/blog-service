from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
import time
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options

chrome_options = Options()
chrome_options.add_argument("--headless")
chrome_options.add_argument("--disable-gpu")
driver = webdriver.Chrome(options=chrome_options)

driver.get("http://192.168.1.1")
time.sleep(2)
username_input = driver.find_element(By.CSS_SELECTOR, "#username")
username_input.send_keys("admin")

password_input = driver.find_element(By.CSS_SELECTOR, "#password")
password_input.send_keys("719AE35686")

submiit_button = driver.find_element(By.CSS_SELECTOR, "#buttoncolor > td > input")
submiit_button.click()

time.sleep(10)
frame_name_or_id = "main"
driver.switch_to.frame(frame_name_or_id)
ip = driver.find_element(By.CSS_SELECTOR, "#block1 > table:nth-child(4) > tbody > tr:nth-child(2) > td:nth-child(3)")
print(ip.text)
driver.quit()