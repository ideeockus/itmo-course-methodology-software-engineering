import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from time import sleep

@pytest.fixture()
def driver():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    yield driver
    driver.close()



def test1(driver): #прошел
    driver.get("http://213.171.10.101:8890/apply")
    # шаг 1
    apply_button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.TAG_NAME, "a")))
    link = apply_button.get_attribute("href")
    driver.get(link)

    # ожидаемый результат
    main_title = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.TAG_NAME, "h2")))
    assert main_title.is_displayed()
    assert main_title.text == "ЗАПОЛНИТЕ ВАШИ ДАННЫЕ"


def test2(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_apply")
    # шаг 1
    time = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.ID, "time-slot12")))
    time.click()

    # шаг 2
    fio = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='create_request_form']/div[2]/input[1]")))
    fio.send_keys("Иванов Иван Иванович")

    # шаг 3
    email = driver.find_element(By.XPATH, "//*[@id='create_request_form']/div[2]/input[2]")
    email.send_keys("test@mail.ru")

    # шаг 4
    phone = driver.find_element(By.XPATH, "//*[@id='create_request_form']/div[2]/input[3]")
    phone.send_keys("88005553535")

    # шаг 5
    checkbox = driver.find_element(By.XPATH, "//*[@id='create_request_form']/div[3]/input")
    checkbox.click()

    # шаг 6
    enter = driver.find_element(By.XPATH, "//*[@id='create_request_form']/button")
    enter.click()

    # ожидаемый результат
    alert = WebDriverWait(driver, 5).until(expected_conditions.alert_is_present())
    assert "Ваш персональный токен:" in alert.text
    #sleep(20)
    alert.accept()


def test3(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_profile?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.ID, "pacient_edit_btn")))
    button.click()

    # шаг 2
    fio = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_edit_form']/input[1]")))
    fio.send_keys("Иванов Сергей Петрович")

    # шаг 3
    email = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[2]")
    email.send_keys("test@mail.ru")

    # шаг 4
    phone = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[3]")
    phone.send_keys("88005553535")

    # шаг 5
    home = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[4]")
    home.send_keys("Ул. Пушкина, д. 5")

    # шаг 6
    work = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[5]")
    work.send_keys("Google")

    # шаг 7
    photo = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[6]")
    photo.send_keys("https://www.perfectfit.ru/upload/iblock/484/lrcqjk1sqyw29khbl7mivoi8kemwo37w/skolko_spyat_kotyata_1023x448.webp")

    # шаг 8
    enter = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[8]")
    enter.click()

    sleep(5)
    # ожидаемый результат
    profile_name = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='card']//*[@id='pacient_name']")))
    assert profile_name.text == "Иванов Сергей Петрович"
