import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from time import sleep

@pytest.fixture()
def driver():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    yield driver
    driver.close()

def test10(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_scan_btn']")))
    button.click()

    # ожидаемый результат
    progressbar = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/dialog")))
    assert progressbar.is_displayed()


def test11(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_scan_btn']")))
    button.click()

    close_button = WebDriverWait(driver, 400).until(
        expected_conditions.element_to_be_clickable((By.XPATH, "/html/body/dialog/button")))
    close_button.click()

    # ожидаемый результат
    erase_button = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='patient_mem_erase_btn']")))
    assert erase_button.is_displayed()