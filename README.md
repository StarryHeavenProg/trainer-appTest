# Кейс 1. Тренажёр аналитики

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
- `task_types`: SINGLE_CHOICE, MULTIPLE_CHOICE, ERROR_FINDING, OPEN_ANSWER, SQL_QUERY
- `task_attempts_status`: REVIEW, COMPLETED, FAILED



