from faker import Faker
from tqdm import tqdm
import psycopg2
import logging


logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
)

# Инициализация Faker
fake = Faker()

# Установите соединение с вашей базой данных
conn = psycopg2.connect(
    dbname='mpi_db',
    user='mpi_user',
    password='pass123',
    host='localhost',
    port='5434',
)
cur = conn.cursor()


# Функция для вставки данных в таблицу Patients
def insert_patients(n):
    logging.info('synth patients')
    for _ in tqdm(range(n)):
        name = fake.name()
        phone = fake.phone_number()[:20]
        email = fake.email()
        appointment_date = fake.date_time_this_decade()
        age = fake.random_int(min=0, max=100)
        address = fake.address()
        state = 'Stage1'
        try:
            cur.execute(
                "INSERT INTO Patients (name, phone, email, appointment_date, age, address, state) VALUES (%s, %s, %s, %s, %s, %s, %s)",
                (name, phone, email, appointment_date, age, address, state))
            conn.commit()
        except psycopg2.Error as e:
            logging.warning('failed insert into patients %s', e)
            conn.rollback()
    # conn.commit()


# Функция для вставки данных в таблицу Equipment
def insert_equipment(n):
    logging.info('synth equipment')
    for _ in tqdm(range(n)):
        name = fake.word().capitalize()
        type_ = fake.word().capitalize()
        status = 'Available'
        location = fake.city()
        maintenance_date = fake.date_this_year()
        service_life = fake.random_int(min=1, max=10)
        try:
            cur.execute(
                "INSERT INTO Equipment (name, type, status, location, maintenance_date, service_life) VALUES (%s, %s, %s, %s, %s, %s)",
                (name, type_, status, location, maintenance_date, service_life))
            conn.commit()
        except psycopg2.Error as e:
            logging.warning('failed insert into equipment %s', e)
            conn.rollback()


# # Функция для вставки данных в таблицу Doctors
# def insert_doctors(n):
#     for _ in range(n):
#         name = fake.name()
#         specialty = fake.job()
#         cur.execute("INSERT INTO Doctors (name, specialty) VALUES (%s, %s)",
#                     (name, specialty))
#     conn.commit()

# Вызов функций
insert_patients(1_000_000)
insert_equipment(1_000_000)
# insert_doctors(10)

# Закрытие соединения с базой данных
cur.close()
conn.close()
