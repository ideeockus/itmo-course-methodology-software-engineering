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


def test18(driver): #прошел
    driver.get("http://213.171.10.101:8890/main_menu")
    # шаг 1
    equipment_list = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.LINK_TEXT, "Оборудование")))
    equipment_list.click()

    # ожидаемый результат
    title_content = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div[2]/div/h3")))
    assert title_content.text == "Основное оборудование для процедур"

# def test19(driver): #прошел
#     driver.get("http://213.171.10.101:8890/technic_profile")
#
#     # шаг 1
#     button = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='equipmentTable']/thead/tr[2]/td[8]/button")))
#     button.click()
#
#     # шаг 2
#     select = Select(WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='edit-status']"))))
#     select.select_by_value("Busy")
#
#     # шаг 3
#     enter = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='saveButton']")))
#     enter.click()
#
#     # ожидаемый результат
#     title_content = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div[2]/div/h3")))
#     assert title_content.text == "Основное оборудование для процедур"