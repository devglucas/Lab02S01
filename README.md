# Sistema de Aluguel de Carros — CRUD de Cliente

Aplicação web (Spring Boot + Thymeleaf) que implementa o **CRUD completo de Cliente**
do Sistema de Aluguel de Carros, seguindo a modelagem em `docs/` (diagramas de casos
de uso, classes, pacotes e componentes).

Escopo desta entrega: **apenas Cliente** (criar, listar, detalhar, editar, excluir).
Não inclui os demais CRUDs, autenticação completa nem os fluxos de pedido/contrato.

## Stack

- Java 17+ (testado em Java 21)
- Spring Boot 3.2 — Spring MVC, Spring Data JPA, Bean Validation
- Thymeleaf (páginas dinâmicas)
- H2 em memória (perfil `dev`, padrão) / MySQL (perfil `prod`)
- Maven

## Arquitetura (MVC em camadas)

Espelha a Visão Lógica dos diagramas de pacotes/componentes, com dependências em
sentido único **Web → Serviços → Domínio → Persistência**:

```
com.aluguel.carros
├── controller   # Camada de apresentação (thin). Não acessa repositórios.
├── service      # Regras de negócio (Gestão de cadastro): CPF único, máx. 3 vínculos
├── domain       # Entidades JPA. Herança Usuario(abstrata) <|-- Cliente
├── repository   # Persistência (Spring Data JPA)
├── dto          # Objetos de formulário/transporte (ClienteForm, VinculoForm)
└── config       # Seed de dados (perfil dev)
```

### Modelo (conforme diagrama de classes)

- `Usuario` (abstrata, herança JOINED): `email`, `senha`
- `Cliente` **extends** `Usuario`: `rg`, `cpf` (único), `nome`, `profissao`
- `Endereco` (1:1): `logradouro`, `numero`, `cidade`, `estado`, `cep`
- `EntidadeEmpregadora`: `nome`, `cnpj`
- `VinculoEmpregaticio` (classe de associação Cliente–EntidadeEmpregadora): `rendimento`
  — **no máximo 3 vínculos por cliente**

## Como rodar (perfil dev / H2 — recomendado)

Pré-requisitos: JDK 17+ e Maven.

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080` já com dois clientes de exemplo (seed).

### URLs

| Função            | URL                                  |
|-------------------|--------------------------------------|
| **Lista de clientes** | http://localhost:8080/clientes   |
| Novo cliente      | http://localhost:8080/clientes/novo  |
| Detalhe           | http://localhost:8080/clientes/{id}  |
| Editar            | http://localhost:8080/clientes/{id}/editar |
| Console do H2     | http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:aluguel`, user `sa`, sem senha) |

A raiz `/` redireciona para `/clientes`.

## Build e testes

```bash
mvn clean package     # compila e empacota o .jar
mvn test              # executa os testes unitários do ClienteService
```

Os testes cobrem as regras de negócio: **CPF único** e **limite de 3 empregadores**,
além de criação/edição/exclusão e casos de "não encontrado".

## Rodar com MySQL (perfil prod)

Requer um MySQL acessível. Ajuste credenciais por variáveis de ambiente
(ou edite `src/main/resources/application-prod.properties`):

```bash
export DB_HOST=localhost DB_PORT=3306 DB_NAME=aluguel_carros DB_USER=root DB_PASSWORD=root
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

O schema é criado/atualizado automaticamente (`ddl-auto=update`).
O banco `aluguel_carros` é criado se não existir (`createDatabaseIfNotExist=true`).
