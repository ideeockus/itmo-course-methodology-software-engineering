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


def test12(driver): #прошел
    driver.get("http://213.171.10.101:8890/auth_form")
    # шаг 1
    login = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div/div[2]/div/form/div[1]/div[2]/input")))
    login.send_keys("test_caller")

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


def test13(driver): #прошел
    driver.get("http://213.171.10.101:8890/main_menu")
    # шаг 1
    patient_list = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.LINK_TEXT, "Список пациентов")))
    patient_list.click()
    sleep(5)

    # шаг 2
    img = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div/div[4]/div/img")))
    img.click()

    # ожидаемый результат
    profile_content = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='legend_wrapper']")))
    assert profile_content.is_displayed()


# def test14(driver): #прошел
#     driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
#     # шаг 1
#     contact = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "#list_wrapper > div:nth-child(2)")))
#     sleep(10)
#     contact.click()
#
#     # ожидаемый результат
#     assert contact.get_attribute("class") == "red_card"