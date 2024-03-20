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


def test20(driver): #прошел
    driver.get("http://213.171.10.101:8890/main_menu")
    # шаг 1
    equipment_list = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.LINK_TEXT, "Оборудование")))
    equipment_list.click()

    # шаг 2
    plus_button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='add_new_equipment']")))
    plus_button.click()

    # ожидаемый результат
    form = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='editForm']")))
    assert form.is_displayed()

# def test21(driver): #прошел
#     driver.get("http://213.171.10.101:8890/technic_profile")
#     # шаг 1
#     plus_button = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='add_new_equipment']")))
#     plus_button.click()
#
#     # шаг 2
#     name_eq = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='edit-name']")))
#     name_eq.send_keys("Провод")
#
#     # шаг 3
#     type_eq = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='edit-type']")))
#     type_eq.send_keys("Проводочек")
#
#     # шаг 4
#     select = Select(WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='edit-status']"))))
#     select.select_by_value("Available")
#
#     # шаг 5
#     location_eq = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='edit-location']")))
#     location_eq.send_keys("A1")
#
#     # ожидаемый результат
#     form = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='editForm']")))
#     assert form.is_displayed()