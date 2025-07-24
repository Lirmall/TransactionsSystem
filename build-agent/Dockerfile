FROM ubuntu:20.04
LABEL authors="Andrey K."

# Отключение интерактивного режима подтверждения и настройки
ENV DEBIAN_FRONTEND=noninteractive

# Обновление системы и установка зависимостей
RUN apt-get update && \
     apt-get install -y \
            curl \
            python3 \
            python3-pip \
            ansible \
            docker.io \
            nodejs \
            npm \
            openjdk-17-jdk \
            maven \
            git && \
        apt-get clean && \
        rm -rf /var/lib/apt/lists/*

# Создаём пользователя azp и группу azp
RUN groupadd azp && useradd -m -g azp azp

WORKDIR /azp

ARG AGENT_VERSION=3.242.1

ARG AZP_AGENTPACKAGE_URL=https://download.agent.dev.azure.com/agent/${AGENT_VERSION}/vsts-agent-linux-x64-${AGENT_VERSION}.tar.gz
RUN curl -LsS "$AZP_AGENTPACKAGE_URL" | tar -xz

COPY ./start.sh .
RUN chmod +x start.sh

# Меняем владельца каталога /azp на пользователя azp
RUN chown -R azp:azp /azp

# Переключаемся на пользователя azp
USER azp

ENTRYPOINT [ "./start.sh" ]
