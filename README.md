# Full Stack Trading Platform

**Spring Boot (Java) + React + Tailwind CSS**

A production-oriented full-stack trading platform for cryptocurrencies. Built to teach and demonstrate real-world engineering: secure authentication (incl. 2FA), wallet and portfolio management, buy/sell flows, bank withdrawals, transaction history, search, and a simple AI chatbot integrated with Gemini and CoinGecko APIs.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Key Features](#key-features)
3. [Technology Stack](#technology-stack)
4. [Architecture](#architecture)
5. [Getting Started (Local)](#getting-started-local)

   * Backend
   * Frontend
6. [Environment Variables (example)](#environment-variables-example)
7. [Database & Migrations](#database--migrations)
8. [API Endpoints (high level)](#api-endpoints-high-level)
9. [Security Notes](#security-notes)
10. [Deployment](#deployment)
11. [Testing](#testing)
12. [Contributing](#contributing)
13. [License](#license)

---

## Project Overview

This project simulates a crypto trading platform where users can:

* Register / Login (with 2FA option)
* Add balance to wallet (simulate payment with Stripe/Razorpay)
* Buy / Sell supported cryptocurrencies (price via CoinGecko)
* Transfer between wallets and withdraw to bank
* View transaction, wallet, and withdrawal history
* Interact with an AI ChatBot for crypto data via Gemini API

It is ideal as a resume project demonstrating backend design, authentication, payment integration, real-time data consumption, and modern frontend UX.

---

## Key Features

* Authentication: JWT + Spring Security + optional 2FA (TOTP)
* PKCE-ready OAuth client support for future integrations
* Wallet management & internal transfers
* Buy/Sell flows with order validation and balance checks
* Transaction ledger with searchable history
* Integration with CoinGecko for real-time pricing
* AI ChatBot using Gemini + CoinGecko
* Payment integrations: Stripe & Razorpay (sandbox)
* Admin dashboard endpoints for coin management and KYC placeholders

---

## Technology Stack

**Backend**

* Java 17+ (Spring Boot)
* Spring Security, Spring Data JPA
* MySQL (or MariaDB)
* Flyway for DB migrations
* Java Mail Sender (password reset)
* Maven

**Frontend**

* React (v18+)
* Tailwind CSS
* Redux (RTK recommended)
* React Router v6
* Axios
* shadcn/ui components

**Dev / Ops**

* Docker + Docker Compose
* Optional Kubernetes manifests for production

---

## Architecture

* **Client (React)** calls a **REST API** (Spring Boot). Sensitive operations go through backend which talks to:

  * MySQL DB
  * External APIs: CoinGecko, Gemini, Payment Gateways
  * Email provider for notifications and password reset

Authentication flow uses JWT access tokens and refresh tokens (stored securely, refresh via http-only cookie recommended). 2FA uses TOTP (Google Authenticator compatible).

---

## Getting Started (Local)

### Prerequisites

* Java 17+
* Maven 3.8+
* Node 18+
* Docker & Docker Compose (recommended)
* MySQL locally or via Docker

### Backend (Spring Boot)

1. Copy example env file: `cp .env.backend.example .env` and fill values.
2. Build & run with Maven:

```bash
# build
mvn clean package -DskipTests

# run
java -jar target/trading-platform-backend-0.0.1-SNAPSHOT.jar --spring.config.location=classpath:/,file:./application.yml
```

or using Docker Compose:

```bash
docker compose up --build
```

### Frontend (React)

1. Go to `frontend/` folder.
2. Install dependencies:

```bash
npm install
```

3. Start dev server:

```bash
npm run dev
# or
npm start
```

Configure the frontend `.env` to point to backend API base URL.

---

## Environment Variables (example)

`.env.backend.example`

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/tradingdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=replace_with_strong_secret
JWT_EXPIRATION_MS=3600000
REFRESH_TOKEN_EXPIRY_MS=2592000000
COINGECKO_API=https://api.coingecko.com/api/v3
GEMINI_API_KEY=your_gemini_key
GEMINI_API_SECRET=your_gemini_secret
STRIPE_SECRET_KEY=sk_test_xxx
RAZORPAY_KEY=rzp_test_xxx
EMAIL_HOST=smtp.example.com
EMAIL_USER=notify@example.com
EMAIL_PASS=yourpass
```

`.env.frontend.example`

```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_ENV=development
```

---

## Database & Migrations

Use Flyway for schema migrations. Example migration files are stored under `src/main/resources/db/migration`.

Basic entity tables: `users`, `wallets`, `transactions`, `orders`, `withdrawals`, `refresh_tokens`, `audit_events`.

---

## API Endpoints (high level)

* `POST /api/auth/register` - register user
* `POST /api/auth/login` - authenticate and receive JWT + refresh cookie
* `POST /api/auth/2fa/enable` - enable TOTP
* `POST /api/wallet/deposit` - add balance (trigger payment flow)
* `POST /api/wallet/transfer` - wallet-to-wallet transfer
* `POST /api/trade/buy` - buy crypto
* `POST /api/trade/sell` - sell crypto
* `GET /api/portfolio` - current holdings
* `GET /api/coins/search?q=btc` - coin search via CoinGecko
* `POST /api/ai/chat` - AI ChatBot endpoint (server proxies to Gemini)

---

## Security Notes

* Store JWT secret and API keys in secure vault for production.
* Use HTTPS in production. Do not store access tokens in localStorage — prefer httpOnly cookies for refresh tokens.
* Rate-limit critical endpoints (login, ai-chat).
* Validate and sanitize all inputs to avoid injection issues.

---

## Deployment

* `docker-compose.yml` for quick deploy (app + db + reverse proxy).
* For production, create Kubernetes manifests and use a managed DB. Use ingress controller and cert-manager for TLS.

---

## Testing

* Backend unit & integration tests with JUnit and MockMvc.
* Frontend tests with React Testing Library.

---

## Contributing

1. Fork the repo
2. Create a feature branch
3. Open a PR with clear description and tests

---

## Useful Commands

```bash
# backend
mvn test
mvn spring-boot:run

# frontend
npm install
npm run build
npm run dev
```

---

## License

MIT. Feel free to reuse and adapt for educational or portfolio purposes.


