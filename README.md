# API de Gerenciamento de Coworking e Eventos

Este é um sistema (MVP) desenvolvido em **Spring Boot** para o gerenciamento de espaços de coworking e eventos empresariais. A API permite o cadastro e gerenciamento de salas, bem como o agendamento (reservas) e cancelamento de horários para o uso desses espaços.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **H2 Database** (Banco de dados em memória para desenvolvimento e testes)
- **Lombok** (Redução de código boilerplate)
- **SpringDoc OpenAPI / Swagger UI** (Documentação interativa da API)
- **Maven** (Gerenciamento de dependências e build)

## 📋 Regras de Negócio e Validações

A API implementa as seguintes regras e validações para agendamentos (`AgendamentoValidador`):
- O aluguel/reserva deve ocorrer dentro do **horário comercial (08:00 às 18:00)**.
- O tempo mínimo de aluguel de uma sala é de **1 hora**.
- Os horários de agendamento devem seguir uma **grade padrão de slots de 1 hora** (ex: 08:00 - 09:00, 09:00 - 10:00, etc).
- Não é possível realizar agendamentos no passado.
- O sistema bloqueia agendamentos em horários conflitantes para a mesma sala.
- Tipos de Sala Suportados: `SALA_AUDITORIO_EVENTOS`, `SALA_COLETIVA`, `SALA_PRIVATIVA`.

## 🛠️ Como Executar a Aplicação

### Pré-requisitos
- **Java 17** instalado na máquina.
- Uma IDE de sua preferência (IntelliJ IDEA, VS Code, Eclipse, etc).

### Passos para execução

1. Clone ou baixe este repositório para a sua máquina local.
2. Abra um terminal na raiz do projeto (onde está localizado o arquivo `pom.xml` ou `mvnw`).
3. Execute o comando do Maven Wrapper para baixar as dependências e rodar o projeto:

   **No Windows:**
   ```cmd
   .\mvnw spring-boot:run
   ```
   
   **No Linux/Mac:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. O servidor iniciará por padrão na porta **8080**.

## 📚 Acesso à Documentação e Banco de Dados

Com a aplicação rodando, você pode acessar:

- **Swagger UI (Documentação da API):** http://localhost:8080/swagger-ui.html
  - *Aqui você pode testar todos os endpoints diretamente pelo navegador de forma visual.*
- **H2 Console (Banco de Dados em Memória):** http://localhost:8080/h2-console
  - **JDBC URL:** `jdbc:h2:mem:coworkingdb`
  - **User Name:** `sa`
  - **Password:** *(deixe em branco)*

## 🌐 Endpoints da API

Abaixo está a lista resumida de todos os endpoints disponíveis na aplicação. Para detalhes completos sobre os parâmetros (como paginação, ordenação e filtros) e o corpo das requisições, consulte o Swagger UI.

### 🏢 Salas
- `POST /salas` - Cadastra uma nova sala.
- `GET /salas` - Lista todas as salas (suporta paginação e filtro por nome).
- `GET /salas/{id}` - Retorna os detalhes de uma sala específica.
- `PUT /salas/{id}` - Atualiza as informações (nome, capacidade, tipo) de uma sala.
- `DELETE /salas/{id}` - Remove uma sala do sistema.
- `GET /salas/tipo/{tipoSala}` - Lista salas filtradas pelo tipo (ex: `SALA_PRIVATIVA`, `SALA_COLETIVA`).
- `GET /salas/agenda` - Lista a agenda diária mostrando todas as salas com suas respectivas reservas do dia.
- `GET /salas/horarios-disponiveis` - Retorna os horários (slots de 1 hora) que ainda estão livres para cada sala em uma data específica.

### 📅 Reservas (Agendamentos)
- `POST /reservas/criar-reserva` - Cria uma nova reserva em uma sala para um período específico.
- `GET /reservas/listar-reservas` - Lista os agendamentos realizados de uma sala (suporta paginação).
- `PATCH /reservas/editar-reserva/{id}` - Altera o horário de início e fim de uma reserva existente.
- `PATCH /reservas/deletar-reserva/{id}` - Cancela uma reserva existente (alterando seu status para `CANCELADO`).

## 💡 Exemplos de Uso (cURL)

Aqui estão alguns exemplos de como interagir com a API via linha de comando usando `curl`. Você também pode importar essas requisições em ferramentas como Postman ou Insomnia.

### 1. Criar uma nova Sala
Cadastra um novo espaço de coworking ou evento no sistema.

```bash
curl -X POST "http://localhost:8080/salas" \
     -H "Content-Type: application/json" \
     -d '{
           "nome": "Sala de Reunião Alpha",
           "capacidade": 10,
           "tipoSala": "SALA_PRIVATIVA"
         }'
```

### 2. Criar uma nova Reserva
Cria uma reserva para uma sala específica informando o `salaId` por *Query Parameter*. O corpo da requisição obedece à estrutura validada pela API.

```bash
curl -X POST "http://localhost:8080/reservas/criar-reserva?salaId=1" \
     -H "Content-Type: application/json" \
     -d '{
           "dataInicioReserva": "2023-12-01T10:00:00",
           "dataFimReserva": "2023-12-01T11:00:00"
         }'
```
*(Lembre-se de alterar as datas para valores futuros ao testar, de acordo com as regras de validação).*

### 2. Listar Reservas por Sala e Dia (Com Paginação)
Lista os agendamentos de uma sala. Suporta paginação e ordenação.

```bash
curl -X GET "http://localhost:8080/reservas/listar-reservas?salaId=1&paginaAtual=0&tamanhoPagina=10&direcao=ASC&ordenacao=id" \
     -H "Accept: application/json"
```

### 3. Editar uma Reserva Existente
Atualiza as informações de horário de uma reserva que já foi criada.

```bash
curl -X PATCH "http://localhost:8080/reservas/editar-reserva/1" \
     -H "Content-Type: application/json" \
     -d '{
           "dataInicioReserva": "2023-12-01T14:00:00",
           "dataFimReserva": "2023-12-01T15:00:00"
         }'
```

### 4. Cancelar / Deletar Reserva
Altera o status da reserva para `CANCELADO` (ou remove a reserva conforme a regra de deleção).

```bash
curl -X PATCH "http://localhost:8080/reservas/deletar-reserva/1" \
     -H "Accept: application/json"
```

---

## 🧪 Testes

Para executar a suíte de testes automatizados unitários e de integração, utilize o comando:

```bash
.\mvnw test
```