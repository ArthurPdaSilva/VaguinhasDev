# VaguinhasDev

## Visão Geral

Plataforma de agregação de vagas de tecnologia que centraliza oportunidades publicadas em diferentes ATS e portais de empresas.

## Objetivo

Facilitar a descoberta de vagas de tecnologia através de:

- busca centralizada
- filtros por senioridade
- modelo de trabalho
- localização
- tecnologias
- data de publicação

## Fontes de Vagas

Inicialmente:

- Greenhouse
- Lever
- Ashby

Futuramente:

- Gupy
- Workday
- outros ATS e páginas públicas de carreiras

## Arquitetura

- Frontend: React + TypeScript + Tailwind CSS
- Backend: Java 21 + Spring Boot, organizado com Clean Architecture
- Banco: PostgreSQL

No backend, cada módulo é dividido em:

- `domain/model`: modelo e regras sem dependências de frameworks
- `application/port/in`: contratos dos casos de uso
- `application/port/out`: contratos exigidos da infraestrutura
- `application/service`: implementação dos casos de uso
- `adapter/in`: entradas HTTP
- `adapter/out`: persistência e integrações
- `config`: composição das dependências

No frontend, cores, tipografia, espaçamentos, containers, sombras e breakpoints ficam centralizados em `frontend/src/styles/tokens.css`.

## Principais Módulos

- companies
- jobs
- collectors
- normalization
- deduplication
- search
- users
- notifications

## Fluxo de Coleta

```text
Fonte externa
→ Collector
→ Normalização
→ Deduplicação
→ Persistência
→ Busca
```

## Estado da Coleta

A API ainda não busca oportunidades externas. O endpoint de listagem, o modelo e a persistência estão prontos, mas os collectors Greenhouse, Lever e Ashby ainda constam no roadmap. Até que um collector e suas empresas/fontes sejam configurados, `/api/v1/jobs` retorna uma lista vazia.

O Flyway é executado automaticamente pela API durante a inicialização e aplica as migrations de `backend/src/main/resources/db/migration` antes da validação do schema pelo Hibernate.

## MVP

- Cadastro de empresas/fontes
- Coleta automática de vagas
- Greenhouse Collector
- Lever Collector
- Ashby Collector
- Deduplicação
- Busca
- Filtros
- Redirecionamento para candidatura original

## Modelo de Vaga

O modelo interno possui os campos:

- identificação: `externalId`, `source`, `sourceUrl`
- conteúdo: `company`, `title`, `description`
- classificação: `seniority`, `workModel`, `location`
- datas: `publishedAt`, `discoveredAt`, `lastSeenAt`
- controle: `status`, `fingerprint`

O par `source` + `externalId` é único. O `fingerprint` apoia a deduplicação entre fontes.

## Rodando o Projeto

Pré-requisitos:

- Java 21
- Node.js 22+
- Docker com Docker Compose

Para construir e iniciar frontend, backend e PostgreSQL:

```bash
docker compose up --build
```

O frontend estará em `http://localhost:5173` e a API em `http://localhost:8080/api/v1/jobs`.

Para desenvolvimento local, inicie apenas o PostgreSQL:

```bash
docker compose up -d postgres
```

Depois, inicie o backend:

```bash
cd backend
./mvnw spring-boot:run
```

Em outro terminal, inicie o frontend:

```bash
cd frontend
npm install
npm run dev
```

Para executar as verificações:

```bash
cd backend && ./mvnw test
cd frontend && npm run lint && npm test && npm run build
```

## CI/CD

O workflow de integração contínua executa, em pushes e pull requests para `main`:

- testes e empacotamento do backend
- Biome, testes e build do frontend
- validação do Docker Compose
- build das imagens Docker do backend e frontend

Após o CI da `main` concluir com sucesso, o workflow de entrega publica no Docker Hub:

- `<DOCKER_USERNAME>/vaguinhasdev-backend:latest`
- `<DOCKER_USERNAME>/vaguinhasdev-backend:<commit-sha>`
- `<DOCKER_USERNAME>/vaguinhasdev-frontend:latest`
- `<DOCKER_USERNAME>/vaguinhasdev-frontend:<commit-sha>`

Configure os seguintes secrets no repositório GitHub:

- `DOCKER_USERNAME`: usuário ou organização do Docker Hub
- `DOCKER_ACCESS_TOKEN`: token de acesso do Docker Hub com permissão de escrita

## Roadmap

- [x] Estrutura inicial do backend e frontend
- [x] Modelo interno e listagem de vagas
- [ ] Cadastro de empresas e fontes
- [ ] Collectors Greenhouse, Lever e Ashby
- [ ] Normalização e deduplicação
- [ ] Busca e filtros avançados
- [ ] Usuários e notificações
