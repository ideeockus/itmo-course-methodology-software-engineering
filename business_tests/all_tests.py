import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from time import sleep
from selenium.webdriver.support.ui import Select

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


def test4(driver): #прошел
    driver.get("http://213.171.10.101:8890/auth_form")
    # шаг 1
    login = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div/div[2]/div/form/div[1]/div[2]/input")))
    login.send_keys("test_doctor")

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


def test5(driver): #прошел
    driver.get("http://213.171.10.101:8890/main_menu")
    # шаг 1
    patient_list = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.LINK_TEXT, "Список пациентов")))
    patient_list.click()

    # шаг 2
    sleep(5)
    img = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.CSS_SELECTOR, "body > div > div.view-2 > div:nth-child(1) > img")))
    img.click()

    # шаг 3
    button = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_edit_btn']")))
    button.click()

    # ожидаемый результат
    main_title = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_edit_form']/h2")))
    assert main_title.is_displayed()
    assert main_title.text == "Редактирование карточки"


def test6(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.ID, "pacient_edit_btn")))
    button.click()

    # шаг 2
    fio = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_edit_form']/input[1]")))
    fio.clear()
    fio.send_keys("Зубенко Михаил Петрович")

    # шаг 3
    email = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[2]")
    email.clear()
    email.send_keys("test@mail.ru")

    # шаг 4
    phone = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[3]")
    phone.clear()
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
    assert profile_name.text == "Зубенко Михаил Петрович"


def test7(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='contact_plus']")))
    button.click()

    # ожидаемый результат
    contact_title = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='contact_add_form']/h2")))
    assert contact_title.text == "Добавление контактного лица"


# def test8(driver): #прошел
#     driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
#     # шаг 1
#     button = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='contact_plus']")))
#     button.click()
#
#     # шаг 2
#     fio = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='contact_add_form']/input[1]")))
#     fio.send_keys("Дмитриев Дмитрий Дмитриевич")
#
#     # шаг 3
#     home_phone = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[2]")
#     home_phone.send_keys("5105510")
#
#     # шаг 4
#     home_adress = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[3]")
#     home_adress.send_keys("Салова 61")
#
#     # шаг 5
#     work_phone = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[4]")
#     work_phone.send_keys("89999467927")
#
#     # шаг 6
#     work = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[5]")
#     work.send_keys("Google")
#
#     # шаг 7
#     photo = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[6]")
#     photo.send_keys(
#         "https://www.perfectfit.ru/upload/iblock/484/lrcqjk1sqyw29khbl7mivoi8kemwo37w/skolko_spyat_kotyata_1023x448.webp")
#
#     # шаг 8
#     enter = driver.find_element(By.XPATH, "//*[@id='contact_add_form']/input[7]")
#     enter.click()
#
#     # ожидаемый результат
#     contact_title = WebDriverWait(driver, 5).until(
#         expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='list_wrapper']/div[4]/h3")))
#     assert contact_title.text == "Дмитриев Дмитрий Дмитриевич"


def test9(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.ID, "pacient_edit_btn")))
    button.click()

    # шаг 2
    select = Select(WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='pacient_edit_form']/select"))))
    select.select_by_value("Stage2")

    # шаг 3
    datetime = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[7]")
    datetime.send_keys("2023-06-25T14:30")

    # шаг 4
    enter = driver.find_element(By.XPATH, "//*[@id='pacient_edit_form']/input[8]")
    enter.click()

    # ожидаемый результат
    next_visit = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='next_visit']")))
    assert next_visit.text == "2024-03-19T16:00:00"


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

def test15(driver): #прошел
    driver.get("http://213.171.10.101:8890/auth_form")
    # шаг 1
    login = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/div/div[2]/div/form/div[1]/div[2]/input")))
    login.send_keys("test_erasier")

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


def test16(driver): #прошел
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


def test17(driver): #прошел
    driver.get("http://213.171.10.101:8890/patient_card_view?token=13de5ef2-776e-4765-b6fd-acc2d4122cf1")
    # шаг 1
    button = WebDriverWait(driver, 5).until(
        expected_conditions.presence_of_element_located((By.XPATH, "//*[@id='patient_mem_erase_btn']")))
    button.click()

    # ожидаемый результат
    progressbar = WebDriverWait(driver, 10).until(
        expected_conditions.presence_of_element_located((By.XPATH, "/html/body/dialog")))
    assert progressbar.is_displayed()


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