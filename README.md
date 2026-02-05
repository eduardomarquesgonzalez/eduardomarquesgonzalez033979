# 📝 Dados da Inscrição

* **Candidato:** Eduardo Marques Gonzalez
* **Vaga:** Desenvolvedor Full Stack
* **Tecnologias:** Java, Spring Boot, React, Docker
* **Projeto:** Fullstack para o  processo seletivo SEPLAG-2026
* **Link do do Projeto:** https://github.com/eduardomarquesgonzalez/eduardomarquesgonzalez033979

# Plataforma de Artistas e Álbuns – Full Stack

## Visão Geral

Este projeto é uma **aplicação Full Stack** desenvolvida como parte de processo seletivo/avaliação técnica, com foco em boas práticas de arquitetura, organização de código e integração entre frontend e backend.

A aplicação permite:

* Cadastro e listagem de **artistas**
* Associação e gerenciamento de **álbuns**
* Upload de **capas de álbuns** utilizando **MinIO**
* Autenticação via **JWT**
* Interface web moderna e responsiva

Toda a solução é **100% containerizada**, permitindo execução sem necessidade de IDE.

---

## 🏗️ Arquitetura da Solução

A arquitetura segue um modelo **SPA + API REST**, com serviços desacoplados e comunicação via Docker Network.

```
┌──────────────┐
│   Frontend   │  React + Vite + Tailwind
│  (Nginx)     │  Porta 8080
└───────┬──────┘
        │ HTTP (REST)
┌───────▼──────┐
│   Backend    │  Spring Boot + JWT
│   API        │  Porta 8080 (interna)
└───────┬──────┘
        │
 ┌──────▼──────┐   ┌──────────────┐
 │ PostgreSQL  │   │    MinIO     │
 │ Banco Dados │   │ Object Store │
 └─────────────┘   └──────────────┘
```

### Tecnologias Utilizadas

**Frontend**

* React 18
* Vite
* TypeScript
* Tailwind CSS
* RxJS (Facade / Store)

**Backend**

* Java 21
* Spring Boot
* Spring Security + JWT
* Spring Data JPA
* Flyway

**Infraestrutura**

* Docker / Docker Compose
* PostgreSQL 16
* MinIO (S3-compatible)
* Nginx

---

## 📂 Estrutura dos Containers

| Serviço       | Descrição                    | Porta          |
| ------------- | ---------------------------- | -------------- |
| frontend      | SPA React servida via Nginx  | 8080           |
| backend       | API REST Spring Boot         | 8080 (interna) |
| postgres      | Banco de dados PostgreSQL    | 5433           |
| minio         | Armazenamento de imagens     | 9000           |
| minio-console | Console administrativo MinIO | 9001           |

---

## 🔐 Autenticação e Segurança

* Autenticação baseada em **JWT**
* Tokens armazenados no frontend
* Rotas protegidas via `AuthGuard`
* Backend configurado como **stateless**

---

## 🧪 Funcionalidades Disponíveis

### Artistas

* Listagem com paginação
* Busca por nome
* Ordenação ascendente/descendente
* Detalhamento de artista

### Álbuns

* Cadastro e edição
* Associação com artistas
* Upload de capa (multipart/form-data)
* Listagem por artista

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* Docker
* Docker Compose

---

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/eduardomarquesgonzalez/eduardomarquesgonzalez033979
cd projeto
```

---

### 2️⃣ Subir todos os serviços

```bash
docker-compose up -d --build
```



### 3️⃣ Acessos

* 🌐 **Aplicação Web**: [http://localhost:8080](http://localhost:80)
* ⚙️ **API Backend**: [http://localhost:8080/api/v1](http://localhost:8080/api/v1)
* 🪣 **MinIO Console**: [http://localhost:9001](http://localhost:9001)
* 🗄️ **PostgreSQL**: localhost:5433

Credenciais MinIO padrão:

```
Usuário: minioadmin
Senha:  minioadmin
```

Credenciais Acesso a aplicação:
PERFIL - ADMIN
```
Usuário: admin
Senha:  admin123
```

PERFIL - USER
```
Usuário: user
Senha:  user123
```

---

## 🧪 Como Testar

### Testes Manuais

* Utilizar a interface web para:

    * Criar usuários
    * Login
    * Cadastrar artistas
    * Criar álbuns com upload de capa

### Testes de API

* Postman / Insomnia
* Base URL:

```
http://localhost:8080/api/v1
```

---

## 📦 Migrações de Banco

* Gerenciadas via **Flyway**
* Executadas automaticamente ao subir o backend

---

 **Qualquer dúvida ou ajuste adicional, fico à disposição.**
