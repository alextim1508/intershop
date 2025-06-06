# intershop

Витрина интернет-магазина

Веб приложение - витрина интернет-магазина на Spring Boot Framework. Включает в себя функциональность  
просмотра товаров интернет-магазина, заполнения корзины и формирования заказа.

## Стек технологий
- **Java 21**
- **Spring Framework**
- **Spring WebFlux для обработки HTTP-запросов**
- **Spring Data R2DBC для работы с базой данных**
- **Postgres база данных для хранения сущностей приложения**
- **Redis база данных для кеширования**
- **Liquibase для миграции базы данных**
- **OpenApi generator для генерации контроллеров RestFull сервиса и клиента к нему**
- **Testcontainers, JUnit 5, Assertj, Mockito для тестирования**
- **Mapstruct для преобразования сущностей базы данных и сущностей передачи данных**
- **WebTestClient для интеграционного тестирования**
- **Thymeleaf для шаблонов html страниц**
- **Spring validation для валидации пользовательских данных**
- **Lombok для генерации шаблонного кода**
- **Docker для запуска контейнеров postgres и linux с запуском jar файла разработанного приложения**

## Запуск приложения

Склонировать репозиторий с кодом и перейти в корневую директорию проекта.

```bash
git clone https://github.com/alextim1508/intershop
```

Запустить docker compose файл.

```bash
docker-compose -f docker-compose.yml up -d
```

![](screenshots/1.jpg)

Открыть в браузере вкладку с адресом.

```bash
http://localhost:8080/
```
Откроется главная страница витрины интернет-магазина

![](screenshots/2.jpg)

Доступен поиск по названию и описанию товаров, сортировка по алфавиту/цене, пагинация.
Для добавления товара в корзину необходимо нажать на кнопку "+" или "В корзину". 
Для перехода в корзину в правом верхнем углу есть соответствующая ссылка.

![](screenshots/3.jpg)

На странице Корзины есть функционал для ее редактирования. Для формирования заказа нужно нажать на кнопку "Купить".

![](screenshots/4.jpg)

Так же в правом верхнем углу есть ссылка на страницу выполненных заказов.

![](screenshots/5.jpg)