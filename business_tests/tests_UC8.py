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


def test24(driver): #прошел
    driver.get("http://213.171.10.101:8890/auth_form")
    # шаг 1
    login = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div/div[2]/div/form/div[1]/div[2]/input")))
    login.send_keys("test_manager")

    # шаг 2
    password = driver.find_element(By.XPATH, "//html/body/div/div[2]/div/form/div[2]/div[2]/input")
    password.send_keys("12345678")

    # шаг 3
    enter = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.TAG_NAME, "a")))
    link = enter.get_attribute("href")
    driver.get(link)

    # ожидаемый результат
    main_title = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.TAG_NAME, "h1")))
    assert main_title.is_displayed()
    assert main_title.text == "Anamnesia"


def test25(driver): #прошел
    driver.get("http://213.171.10.101:8890/main_menu")
    # шаг 1
    link = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.LINK_TEXT, "Статистика")))
    link.click()

    # ожидаемый результат
    main_title = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.TAG_NAME, "h1")))
    assert main_title.is_displayed()
    assert main_title.text == "Статистика"