# User Service (Spring Boot + JWT + PostgreSQL)

Микросервис для управления пользователями с аутентификацией и авторизацией через **JWT токены**.  
Поддерживает авторизацию и аутентификацию, имеются админские эндпоинты.

---

## 🚀 Возможности

- Регистрация и вход по логину/паролю
- Авторизация через JWT
- Роли пользователей: **USER**, **ADMIN**
- Эндпоинты:
  - `POST /api/v1/auth/signup` — регистрация
  - `POST /api/v1/auth/login` — вход
  - `GET /api/v1/users/me` — получить текущего пользователя
  - `PUT /api/v1/users/me` — обновить текущего пользователя
  - `GET /api/v1/users/admin/**` — админские методы (список всех пользователей, получить по id и т.д.)
- Глобальный обработчик ошибок
- Валидация входных данных
- Unit и интеграционные тесты

---

## ⚙️ Запуск проекта

### 1. Через Docker Compose (рекомендуется)
```bash
# Скопировать и заполнить переменные окружения
cp .env.example .env

# Запуск сервисов
docker-compose up --build
```

- сервис будет доступен на: **http://localhost:8081**  
- PostgreSQL поднимется на порту **5433**

---

### 2. Локально в IntelliJ IDEA
1. Установить PostgreSQL (создать базу `userservice_db`).  
2. Заполнить переменные в `.env` (см. ниже).  
3. Запустить `UserServiceApplication`.  

---

## 🔑 Файл `.env.example`

```env
# Database
DB_USER=your_username
DB_PASSWORD=your_password
DB_URL=jdbc:postgresql://localhost:5432/your_db_name

# JWT
JWT_SECRET=your_super_secret_key
JWT_EXPIRATION=3600
```

Создай файл `.env` на основе этого примера и подставь свои значения.

---

## 🛠️ Используемый стек
- **Java 17**
- **Spring Boot 3**
- **Spring Security (JWT)**
- **Spring Data JPA + PostgreSQL**
- **Lombok, ModelMapper**
- **JUnit5, Mockito**
- **Docker, Docker Compose**

---

## ✅ Тестирование
Запустить тесты:
```bash
./mvnw test
```

---

## 📂 Структура проекта
```
src/
 └── main/java/com/amazingshop/personal/userservice
      ├── config        # Конфигурация Web + Security 
      ├── controllers   # REST контроллеры + GlobalException
      ├── dto           # объекты для запросов/ответов
      ├── enums         # enum
      ├── models        # Сущности
      ├── repositories  # доступ к БД
      ├── security      # JWT и конфигурация
      ├── services      # бизнес-логика
      └── util          # валидация, ошибки, ответы
```

---

## 🔑 Пример запроса

### Регистрация
```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "username": "newUser",
  "password": "password123",
  "email": "user@example.com"
}
```

### Успешный ответ
```json
{
  "token": "jwt-token",
  "expiresIn": 3600,
  "type": "Bearer",
  "username": "newUser"
}
```
