# Capy Eats

Capy Eats is a food-ordering web application built for the SoftUni Spring Fundamentals individual project. Customers can order from a kitchen and pay on delivery. Kitchen owners manage their menus and process incoming orders from a separate dashboard.

## Features

| Customers | Kitchen owners |
| --- | --- |
| Browse kitchens and available dishes | Create, view, edit, and delete menu items |
| Add dishes to the cart and review the checkout total | Set delivery time and fee, and open or pause ordering |
| Create, edit, and delete saved delivery addresses | View incoming orders and their item quantities and totals |
| Place cash-on-delivery orders and view order history | Mark a new order as preparing or canceled |
| Edit personal information and view total spending | Confirm delivery and record cash collection |

An order starts as **New**. The kitchen can move it to **Preparing** or **Canceled**. Once preparation starts, the next status is **Delivered**. Confirming delivery also records the cash payment. Customer spending totals include delivered cash orders with a recorded payment.

## Tech stack

- **Java 17** and **Maven**
- **Spring Boot 3.4.0**, Spring MVC, Spring Security, and Bean Validation
- **Thymeleaf**, HTML, CSS, and JavaScript
- **Spring Data JPA**, Hibernate, and **MySQL**

Capy Eats runs as a single Spring Boot application. MVC controllers handle requests and render Thymeleaf templates. Services implement the business rules, and JPA repositories manage persistence. Spring Security provides session-based authentication and role checks. Passwords are hashed with BCrypt, and entities use UUID primary keys.

## Integrations

The application connects to MySQL through MySQL Connector/J. Checkout uses cash on delivery. Running the application requires no payment provider account or payment API keys.

## Run locally

### Prerequisites

- JDK 17 or newer
- Maven
- A running MySQL server and a database user with permission to create and update tables in the application schema

### Setup

1. Clone the repository.

   ```bash
   git clone https://github.com/denislav999/capy-eats.git
   cd capy-eats
   ```

2. Create a MySQL schema using the MySQL client or your database IDE.

   ```sql
   CREATE DATABASE IF NOT EXISTS capy_eats_softuni CHARACTER SET utf8mb4;
   ```

3. Set the datasource environment variables in the shell where you will start the app. Use your own MySQL credentials.

   ```bash
   export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/capy_eats_softuni'
   export SPRING_DATASOURCE_USERNAME='your_mysql_user'
   export SPRING_DATASOURCE_PASSWORD='your_mysql_password'
   mvn spring-boot:run
   ```

4. Open [http://localhost:8080](http://localhost:8080). Hibernate creates the tables in a fresh schema. Create the demo accounts using the signup pages.

For a clean evaluation run, use a fresh schema. An older development schema may still have a MySQL `order_status` enum that predates `DELIVERED`.

## Demo walkthrough

1. **Kitchen owner.** Register at `/signup-kitchen`, sign in, and open **Kitchen Settings**. Set a delivery time and fee, then enable **Accept orders**. In **Menu**, add a dish and edit its name or price. Add and delete a second dish to demonstrate menu CRUD.
2. **Customer.** Register a separate account at `/signup` and sign in. Save a delivery address in **Profile → Addresses**, open the kitchen from the home page, and add an available dish to the cart. Complete checkout with **Cash on delivery** and check **Order History** for the new order.
3. **Kitchen owner.** Open **Kitchen Orders**. Find the order and save **Preparing**, then save **Delivered · cash collected**.
4. **Customer.** Reload **Order History** to see **Delivered**, then open **Profile** to confirm the amount appears in **Total Spent**.
