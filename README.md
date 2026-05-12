# Кейс 1. Тренажёр аналитики

Приложение-тренажёр, который позволит аналитикам отрабатывать практические навыки в безопасной и контролируемой среде.

Приложение имитирует реальную работу аналитика: пользователи проходят задания разных типов, система фиксирует результаты, начисляет баллы и ведёт прогресс обучения.

В системе реализовано три вида заданий:
* тестовые задания (один или несколько правильных ответов);
* задания на поиск ошибок;
* открытые задания (самостоятельная проработка артефактов).

## Состав команды

* Цылев Артем
* Вартик Дмитрий
* Монич Никита
* Трошина Анастасия
* Кулачкова Анастасия
* Малнар Иван

## Инструкция по запуску
Убедиться, что докер запущен локально и у него есть возможность скачивать образы.
```sh
docker-compose -f compose-devservices.yml up --build
```

## Swagger
swagger-ui http://localhost:8080/api/q/swagger-ui/

команды для генерации закрытого и открытого ключа
```sh
openssl genrsa -out privateKey.pem 2048
openssl rsa -in privateKey.pem -pubout -out publicKey.pem
```

## Тестирование
### Тестовые данные
Команда подготовила небольшое количество трейнеров с заданиями, типы которых указаны в требованиях.<br>
Идентификаторы трейнеров (можно получить содержимое трейнера по спец. ручке через сваггер):
1. SQL trainer #1: bcc6f453-e4e4-49fc-b1e3-ca687d3f20a6
2. Test trainer #2: 34f097a9-1542-4a43-8b30-bef905d4f925
3. Business analyses trainer #3: 8d3d4a57-c179-4f34-afc5-6a4e272fc26b

### Ход тестирования
<details>
  
#### 1. Получить список имеющихся тренажеров
<img width="384" height="55" alt="image" src="https://github.com/user-attachments/assets/1e5ab706-b56c-413e-ac77-94007754da3d" />

**Формат ответа:**

<img width="398" height="296" alt="image" src="https://github.com/user-attachments/assets/35af865a-75b9-4c98-9a5c-2bc47a2ac205" />


#### 2. Получить конкретный тренажер по ID
<img width="460" height="56" alt="image" src="https://github.com/user-attachments/assets/60fa2120-ee7d-43db-a318-6987d7a3f250" />

**Формат ответа:**

<img width="1106" height="411" alt="image" src="https://github.com/user-attachments/assets/9762132c-c360-49ba-a6bd-5cc83e6c0400" />


#### 3. Получить выбранную задачу из списка задач тренажера
<img width="637" height="87" alt="image" src="https://github.com/user-attachments/assets/a4eff99d-30e9-43e1-9833-f204379e6e0b" />

**Формат ответа:**

<img width="1114" height="214" alt="image" src="https://github.com/user-attachments/assets/a6dfa729-8acf-4efd-ac02-58921eccb37a" />

#### 4. Отправить ответ на выбранный вопрос из списка вопросов тренажера
<img width="680" height="106" alt="image" src="https://github.com/user-attachments/assets/382753b3-a949-4092-af5a-7693e6a18f88" />

**Формат ответа:**

<img width="335" height="77" alt="image" src="https://github.com/user-attachments/assets/61185f2a-37b3-4c3f-9fc8-818ea1f07dcc" />
<img width="371" height="87" alt="image" src="https://github.com/user-attachments/assets/cbfb38fb-f63d-40cb-9ee6-a2a62c0f0d0f" />

</details>

## Схема БД
### Диаграмма
<details>
  <img width="993" height="859" alt="postgres - public" src="https://github.com/user-attachments/assets/8ca8b101-bca7-4049-9e96-ae4ba55a07d5" />
</details>

### Основные сущности
- **`users`** - учетные записи.<br>
  Хранит `id` (UUID), ФИО, уникальный `email`, хеш пароля и системную роль (`app_role`).
- **`trainers`** - логические контейнеры для заданий.<br>
  Поля: `id`, `name`, `created_by` (ссылка на `users`), `created_at`. 
- **`tasks`** - конкретное упражнение.<br>
  Содержит `id`, тип (`task_type`), автора и `config` (JSONB). Конфиг может содержать произвольное кол-во полей, напр.: вопрос, эталонный ответ, макс. баллы, штраф за ошибку и лимит попыток.
- **`tasks_trainers`** - задача в конкретном тренажере.<br>
- **`task_attempts`** - история прохождений.<br>
  Фиксирует результат попытки пользователя: `user_answer` (JSONB), набранные `points`, `status` и временную метку. Привязывается к конкретной связке задание-тренажер.

### Связи
- `trainers` ↔ `tasks`: связь Many-to-Many, реализуется через таблицу `tasks_trainers` (`task_id`, `trainer_id` + уникальность пары). При удалении сущностей срабатывает каскадное удаление из смежной таблицы.
- `users` → `task_attempts`: One-to-Many. Один пользователь может иметь множество попыток.
- `tasks_trainers` → `task_attempts`: One-to-Many. Попытка привязывается к конкретному вхождению задания в тренажер.

### Перечисления
- `user_roles`: APP_USER, APP_EXPERT, APP_ADMIN
- `task_types`: SINGLE_CHOICE, MULTIPLE_CHOICE, ERROR_FINDING, OPEN_ANSWER
- `task_attempts_status`: REVIEW, COMPLETED, FAILED


### Формат конфигурации задач

Ниже представлены примеры конфигурации задач

#### SINGLE_CHOICE (Одиночный выбор)
```json
{
  "question": "Какой вариант правильный?",
  "answerChoices" : [
    {    
      "ordinal" : 1,
      "choice" : "Вариант 1"
    },
    {    
      "ordinal" : 2,
      "choice" : "Вариант 2"
    }
  ],
  "expectedOrdinal": 1,
  "points": 2,
  "mistakeCost": 2,
  "maxAttempts": 1
}
```

#### MULTIPLE_CHOICE (Несколько правильных ответов)

```json
{
  "question": "Какой вариант правильный?",
  "answerChoices" : [
    {    
      "ordinal" : 1,
      "choice" : "Вариант 1"
    },
    {    
      "ordinal" : 2,
      "choice" : "Вариант 2"
    }
  ],
  "expectedOrdinal": [1, 2],
  "points": 4,
  "mistakeCost": 2,
  "maxAttempts": 2
}
```

#### ERROR_FINDING (Поиск ошибок в выражении)

```json
{
  "question": "Найдите ошибку в выражении в ответ запишите одно слово, которое должно быть заменено",
  "answer": "Эталонный ответ",
  "points": 5,
  "mistakeCost": 2,
  "maxAttempts": 2
}
```

#### OPEN_ANSWER (Открытые задания)

```json
{
  "question": "Вопрос с открытым ответом",
  "context": "Описание задания",
  "answer": "",
  "points": 8,
  "mistakeCost": 3,
  "maxAttempts": 2
}
```

#### Примечания
* Для OPEN_ANSWER ответ отправляется на проверку эксперту (статус REVIEW), до его проверки points=0
* Остальные типы задач проверяются автоматически
* При повторных попытках применяется штраф, если был дан правильный ответ: points = maxPoints - mistakeCost * (attemptsCount - 1)
* При неверном ответе выставляется points=0


## Структура проекта

```
src/main
├── java
│   └── ru
│       └── mephi
│           └── trainer
│               ├── config                 # Конфигурация OpenAPI и Swagger
│               ├── entity                 # Сущности
│               │   └── enums              # Перечисления 
│               ├── exception              # Кастомные исключения 
│               ├── mapper                 # Мапперы
│               ├── models                 # Модели
│               ├── repository             # Panache репозитории для работы с БД
│               ├── rest
│               │   ├── api                # OpenAPI интерфейсы
│               │   ├── controller         # REST контроллеры
│               │   ├── dto                # Data Transfer Objects
│               │   │   ├── request        # DTO для запросов
│               │   │   └── response       # DTO для ответов
│               │   └── handler            # Обработчик
│               └── service                # Бизнес-логика
└── resources
    └── db
        └── changelog                      # Миграции Liquibase
            └── versions                   # SQL скрипты миграций
```


## Пример использования сервиса

### Запрос для проверки заданий

```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer_id}/tasks/{task_id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "1"
}'
```

```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer_id}/tasks/{task_id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "[1, 2]"
}'
```

```
curl -X 'POST' \
  'http://localhost:8080/api/trainers/{trainer_id}/tasks/{task_id}/submit' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer {token}' \
  -d '{
  "userAnswer": "Текст ответа"
}'
```

Примечания
* {trainer_id} — UUID тренажёра
* {task_id} — UUID задачи
* {token} — JWT токен, полученный при входе
