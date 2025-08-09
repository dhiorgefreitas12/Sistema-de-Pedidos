# Sistema de Pedidos — Spring Boot + JPA + PostgreSQL + React

Aplicação full‑stack para **gestão de pedidos** com aprovação por **limite de crédito nos últimos 30 dias**.

- **Backend:** Spring Boot 3, JPA/Hibernate, PostgreSQL
- **Frontend:** React + Vite + TypeScript + Material UI
- **Comunicação:** REST (JSON)
- **Testes:** JUnit 5 + Spring Boot Test (✅ testes unitários implementados no backend — ver seção “Testes”)

---

## 🧱 Arquitetura & Pastas

```
sistema-de-pedidos/
├─ backend/                       # Spring Boot
│  ├─ src/main/java/...           
│  ├─ src/main/resources/
│  │  ├─ application.properties   
│  │  └─ application-example.properties
│  ├─ src/test/java/...           # ✅ testes unitários
│  ├─ pom.xml
├─ frontend/                      # React + Vite
│  ├─ src/...
│  ├─ .env                        
│  ├─ .env.example
│  ├─ package.json
└─ README.md                      
```

---

## ✅ Requisitos

- **JDK 17+**
- **Node 18+**
- **PostgreSQL 14+**

---

## 🚀 Configuração Rápida

---

#### 1) Banco de Dados
```sql
CREATE DATABASE pedidos;
CREATE USER admin WITH ENCRYPTED PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE pedidos TO admin;
```

#### 2) Backend
```bash
cd backend
# copie o exemplo e ajuste conforme seu ambiente
cp src/main/resources/application-example.properties src/main/resources/application.properties
./mvnw spring-boot:run   # ou: mvn spring-boot:run
```
- Sobe em **http://localhost:8080**

#### 3) Frontend
```bash
cd frontend
cp .env.example .env     # ajuste VITE_API_URL se necessário
npm i
npm run dev
```
- Acesse **http://localhost:5173**

---

## 🔧 Variáveis de Ambiente

### Backend (`backend/src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pedidos
spring.datasource.username=admin
spring.datasource.password=admin

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Frontend (`frontend/.env`)
```env
VITE_API_URL=http://localhost:8080
```

---

## 🧪 Testes

O backend possui **testes unitários** com **JUnit 5** e **Spring Boot Test** cobrindo as regras críticas (ex.: aprovação/rejeição por limite de crédito em 30 dias).  
Para rodar:

```bash
cd backend
./mvnw test   # ou: mvn test
```


