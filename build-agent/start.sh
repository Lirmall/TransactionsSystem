#!/bin/bash
set -e  # Немедленно прекратить работу если возникла ошибка

# Проверка обязательных переменных окружения для корректной работы агента
if [ -z "$AZP_URL" ] || [ -z "$AZP_TOKEN" ] || [ -z "$AZP_POOL" ]; then
  echo 1>&2 "Error: AZP_URL, AZP_TOKEN and AZP_POOL environment variables must be set."
  echo 1>&2 "Example: docker run -e AZP_URL=... -e AZP_TOKEN=... -e AZP_POOL=... <image>"
  exit 1
fi

# Функция очистки, вызывается при остановке контейнера для корректного удаления агента
cleanup() {
  echo "Cleaning up: removing agent from the pool..."
  ./config.sh remove --unattended --auth pat --token "$AZP_TOKEN"
}

# Назначение обработчиков сигналов SIGINT и SIGTERM для вызова cleanup
trap 'cleanup; exit 130' INT
trap 'cleanup; exit 143' TERM

# Конфигурация агента с параметрами из переменных окружения
echo "Configuring Azure Pipelines agent..."

./config.sh --unattended \
  --agent "${AZP_AGENT_NAME:-$(hostname)}" \
  --url "$AZP_URL" \
  --auth pat \
  --token "$AZP_TOKEN" \
  --pool "$AZP_POOL" \
  --acceptTeeEula

# Запуск агента
echo "Starting agent..."
./run.sh
