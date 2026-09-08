# AGENTS.md

## Project Context

Este projeto é um agregador de vagas de tecnologia.

O sistema NÃO recebe candidaturas diretamente.

Toda vaga deve apontar para sua fonte original.

## Architecture

Use modular monolith.

Não introduza microsserviços sem necessidade explícita.

Backend:

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL

Frontend:

- React
- TypeScript
- Tailwind CSS
- tokens de design centralizados em `frontend/src/styles/tokens.css`

## Backend Rules

Controllers:

- não possuem regra de negócio
- apenas recebem requests e retornam responses

Application:

- contém casos de uso
- define portas de entrada e saída
- não depende de Spring, JPA ou adapters

Domain:

- contém regras de negócio
- não depende de frameworks

Adapters:

- integrações externas
- banco de dados
- collectors
- API HTTP

Config:

- realiza a composição de dependências
- conecta portas da aplicação aos adapters

Direção obrigatória das dependências:

```text
adapter → application → domain
```

O domínio nunca depende das camadas externas.

## Collector Rules

Todo collector deve implementar uma interface comum.

Exemplo:

`JobCollector`

Collectors iniciais:

- `GreenhouseJobCollector`
- `LeverJobCollector`
- `AshbyJobCollector`

Collectors nunca devem persistir entidades diretamente.

Fluxo obrigatório:

```text
fetch
→ map
→ normalize
→ deduplicate
→ persist
```

## Job Normalization

Toda vaga deve ser convertida para o modelo interno antes de ser salva.

Campos mínimos:

- `externalId`
- `source`
- `sourceUrl`
- `company`
- `title`
- `description`
- `seniority`
- `workModel`
- `location`
- `publishedAt`
- `discoveredAt`
- `lastSeenAt`
- `status`

## Deduplication

Nunca assumir que `externalId` é globalmente único.

Uma vaga pode aparecer em múltiplas fontes.

Priorizar:

- `source` + `externalId`
- company
- normalized title
- location
- fingerprint

## Code Style

Prefer:

- código simples
- classes pequenas
- nomes explícitos
- composição
- records para DTOs quando apropriado

Avoid:

- abstrações prematuras
- classes genéricas desnecessárias
- herança sem necessidade
- lógica em controllers
- lógica de negócio em repositories

## Testing

Toda regra de negócio nova deve possuir testes.

Prioridade:

1. unit tests
2. integration tests
3. collector contract tests

## Database

Use Flyway para migrations.

Nunca usar Hibernate `ddl-auto` para alterar schema em produção.

## API

Endpoints REST devem seguir:

- `/api/v1/jobs`
- `/api/v1/companies`
- `/api/v1/technologies`

## Important

Antes de implementar uma feature:

1. entender o domínio
2. identificar módulo responsável
3. verificar se já existe abstração equivalente
4. implementar a menor solução necessária
5. adicionar testes
6. evitar refatorações não relacionadas
