1. Установка базовых компонентов
   Обновление списка доступных пакетов (репозиториев).
~~~bash
sudo apt update

~~~
> Установка:

- `openjdk-17-jdk` — JDK 17, чтобы запускать Java-приложения;
- `nginx` — web-сервер, будет работать как обратный прокси перед Tomcat;
- `postgresql` — база данных PostgreSQL.
~~~bash
sudo apt install -y openjdk-17-jdk nginx postgresql

~~~

2. Установка Apache Tomcat
   Переход в директорию для установки сторонних программ.
~~~bash
cd /opt

~~~
Скачиваем архив с Tomcat 10.1.24 (можно взять другую версию, если нужно).
~~~bash
sudo wget https://downloads.apache.org/tomcat/tomcat-10/v10.1.24/bin/apache-tomcat-10.1.24.tar.gz

~~~
Распаковываем архив.
~~~bash
sudo tar -xzf apache-tomcat-10.1.24.tar.gz

~~~
Переименовываем папку в более короткое имя (`/opt/tomcat`).
~~~bash
sudo chown -R $USER:$USER tomcat

~~~
Меняем владельца папки на текущего пользователя — чтобы можно было управлять без `sudo`.
~~~bash
sudo chown -R $USER:$USER tomcat

~~~
3. Настройка systemd для Tomcat
   Создаём юнит-файл для запуска Tomcat как системной службы.
~~~bash
sudo nano /etc/systemd/system/tomcat.service

~~~

Содержимое:
Настройка переменных среды и путей. Обратить внимание на пользователя `ubuntu` — если у тебя другой пользователь, нужно заменить.
~~~ini
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

User=ubuntu
Group=ubuntu

[Install]
WantedBy=multi-user.target

~~~
Принудительная перезагрузка `systemd`, чтобы он увидел новый юнит.
~~~bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload

~~~
Автоматический запуск Tomcat при старте системы.
~~~bash
sudo systemctl enable tomcat

~~~
Запуск Tomcat сейчас.
~~~bash
sudo systemctl start tomcat

~~~
4. Настройка PostgreSQL и схем
   Переход в консоль PostgreSQL от имени пользователя `postgres`.
~~~bash
sudo -u postgres psql

~~~
Внутри psql:
Создание нового пользователя БД.
~~~bash
CREATE USER app_user WITH PASSWORD 'strongpassword';

~~~
Создание БД, где будут храниться все схемы проекта.
~~~bash
CREATE DATABASE transactions_system OWNER app_user;

~~~
Подключаемся к новой БД.
~~~bash
\c transactions_system

~~~
Создаём три схемы — каждая под отдельный модуль.
~~~bash
CREATE SCHEMA ts_accounts AUTHORIZATION app_user;
CREATE SCHEMA ts_transactions AUTHORIZATION app_user;
CREATE SCHEMA ts_reports AUTHORIZATION app_user;

~~~

5. Настройка NGINX как reverse proxy
   Создаём конфигурационный файл nginx для проекта.
~~~bash
sudo nano /etc/nginx/sites-available/transactions_system

~~~
Пример содержимого:
~~~bash
server {
    listen 80;
    server_name your.domain.com;

    location / {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

~~~
Прокидываем запросы от NGINX на Tomcat, который работает на `localhost:8080`.

Включаем конфигурацию (символическая ссылка в `sites-enabled`).
~~~bash
sudo ln -s /etc/nginx/sites-available/transactions_system /etc/nginx/sites-enabled/

~~~
Проверяем конфигурацию на ошибки.
~~~bash
sudo nginx -t

~~~
Перезапускаем NGINX, чтобы применить конфигурацию.
~~~bash
sudo systemctl reload nginx

~~~

6. Подключение приложения
   Собранный `.war` нужно скопировать в папку:
~~~bash
/opt/tomcat/webapps/

~~~

7. Настройка схемы в `application.yml`
   Пример:
   Указывает Hibernate, с какой схемой работать.
~~~bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/transactions_system
    username: app_user
    password: strongpassword
  jpa:
    properties:
      hibernate.default_schema: ts_accounts

