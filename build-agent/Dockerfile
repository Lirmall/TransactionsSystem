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
            npm && \
        apt-get clean && \
        rm -rf /var/lib/apt/lists/*

WORKDIR /azp

ARG AGENT_VERSION=3.242.1

ARG AZP_AGENTPACKAGE_URL=https://vstsagentpackage.azureedge.net/agent/${AGENT_VERSION}/vsts-agent-linux-x64-${AGENT_VERSION}.tar.gz
RUN curl -LsS "$AZP_AGENTPACKAGE_URL" | tar -xz

COPY ./start.sh .
RUN chmod +x start.sh

ENTRYPOINT [ "./start.sh" ]
