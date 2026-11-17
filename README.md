# SmartStock AI

**AI-Powered Desktop Inventory Management System**

SmartStock AI is a Java-based desktop application for managing products,
inventory, suppliers, sales, analytics, and reports. It also integrates
an AI insights feature that analyzes available inventory and sales data
through the Groq API.

> **Development status:** The core application completed Maven `clean`,
> `test`, and `package` successfully and was manually tested across its
> main modules during development.

## Project Information

  -----------------------------------------------------------------------
  Item                                Details
  ----------------------------------- -----------------------------------
  Project Title                       SmartStock AI

  Project Type                        Desktop Inventory Management System

  Department                          Computer Science and Engineering
                                      (CSE)

  Course ID and Course Name           **\[Enter official course ID and
                                      course name\]**

  Subject                             APP --- **\[Confirm official
                                      subject name\]**

  Academic Year                       **\[Enter academic year\]**

  Semester                            **\[Enter semester\]**

  Section                             **\[Enter section\]**

  Institution                         **\[Enter institution name\]**
  -----------------------------------------------------------------------

### Team Members

Replace the placeholders with the exact details required by your
department.

  --------------------------------------------------------------------------
  Full Name      Register       Department     Section        Academic Year
                 Number                                       
  -------------- -------------- -------------- -------------- --------------
  \[Team Leader  \[Register     CSE            \[Section\]    \[Academic
  Name\]         Number\]                                     Year\]

  \[Member 2     \[Register     CSE            \[Section\]    \[Academic
  Name\]         Number\]                                     Year\]

  \[Member 3     \[Register     CSE            \[Section\]    \[Academic
  Name\]         Number\]                                     Year\]

  \[Member 4     \[Register     CSE            \[Section\]    \[Academic
  Name\]         Number\]                                     Year\]
  --------------------------------------------------------------------------

### Mentor

-   **Mentor's Full Name:** \[Enter mentor name\]
-   **Designation:** \[Enter designation, if applicable\]

------------------------------------------------------------------------

## Problem Statement

Businesses need reliable ways to maintain product information, monitor
stock, record sales, and understand inventory performance. Manual
records or disconnected spreadsheets can make these activities difficult
to coordinate.

SmartStock AI centralizes product, supplier, inventory, sales,
analytics, and reporting operations in a desktop application. Its AI
insights module analyzes available inventory and sales data and presents
structured analysis to assist an inventory manager.

## Objectives

-   Manage product information centrally.
-   Maintain supplier information.
-   View and adjust inventory quantities.
-   Identify low-stock and out-of-stock products.
-   Record sales and update stock after successful transactions.
-   View sales and inventory analytics.
-   Generate inventory, sales, and business reports.
-   Generate AI-assisted insights from available inventory and sales
    data.
-   Demonstrate Java OOP, Swing, JDBC, MySQL, exception handling, and
    background processing.

## Features

### Dashboard

Displays an overview of inventory and business information, including
total products, low-stock products, out-of-stock products, inventory
value, and stock alerts.

### Product Management

Supports adding, editing, deleting, searching, and refreshing product
records. Product information includes product ID, name, category, price,
quantity, reorder level, and supplier ID.

### Inventory Management

Displays product stock, reorder levels, unit prices, stock values, and
stock status. Supports searching, filtering, stock adjustment, and
refreshing inventory information.

### Supplier Management

Provides supplier record management, including adding, editing,
deleting, searching, and refreshing supplier information.

### Sales Management

Supports product selection, quantity entry, sale total calculation, sale
creation, and sales history. The sale workflow checks stock availability
and updates product quantity after a successful transaction.

### Analytics

Displays business and inventory metrics such as total products, total
sales, total revenue, inventory value, low-stock information, and
revenue visualization.

### AI Insights

Integrates with the Groq API to analyze supplied inventory and sales
information. Generated analysis is organized into sections such as
Executive Summary, Stock Risks, Product Demand, Sales Performance,
Replenishment Priority, Recommended Actions, and Next Check.

AI output depends on the data supplied to the service and should be
treated as decision support, not an autonomous business decision.

### Reports

Provides Inventory Report, Sales Report, and Full Business Report
generation.

### Settings

Provides an application settings interface and database connection
testing.

## Technology Stack

  Technology              Usage
  ----------------------- ----------------------------------
  Java 17                 Core application development
  Java Swing and AWT      Desktop graphical user interface
  FlatLaf                 Swing look and feel
  MySQL                   Relational database
  JDBC                    Java-to-MySQL connectivity
  Maven                   Dependency and build management
  Groq API                External AI inference
  `openai/gpt-oss-120b`   AI model used through Groq
  `SwingWorker`           Background processing
  JUnit 5                 Unit testing
  IntelliJ IDEA           Development environment

## Architecture

The application uses a layered architecture to separate user
interaction, business logic, database access, and data representation.

``` text
User
  |
  v
Java Swing GUI
  |
  v
Service Layer
  |
  v
DAO Layer
  |
  v
JDBC
  |
  v
MySQL Database
```

The AI workflow is integrated separately:

``` text
AI Insights Panel
       |
       v
Inventory AI Service
       |
       v
Inventory and Sales Data
       |
       v
Groq API
       |
       v
AI-Generated Analysis
       |
       v
AI Insights Panel
```

## Package Structure

``` text
com.smartstock
├── Main.java
├── ai
│   ├── AIInsightsPanel.java
│   └── InventoryAIService.java
├── dao
│   ├── ProductDAO.java
│   ├── ProductDAOImpl.java
│   ├── SaleDAO.java
│   ├── SaleDAOImpl.java
│   ├── SupplierDAO.java
│   └── SupplierDAOImpl.java
├── exception
│   ├── DatabaseException.java
│   └── ValidationException.java
├── gui
│   ├── AnalyticsPanel.java
│   ├── DashboardPanel.java
│   ├── InventoryPanel.java
│   ├── MainFrame.java
│   ├── ProductPanel.java
│   ├── ReportsPanel.java
│   ├── SalesPanel.java
│   ├── SettingsPanel.java
│   ├── SupplierPanel.java
│   └── Theme.java
├── model
│   ├── Product.java
│   ├── ProductSalesSummary.java
│   ├── Sale.java
│   ├── SaleItem.java
│   └── Supplier.java
├── report
│   ├── Report.java
│   ├── InventoryReport.java
│   └── SalesReport.java
├── service
│   ├── ProductService.java
│   ├── ProductServiceImpl.java
│   ├── ReportService.java
│   ├── ReportServiceImpl.java
│   ├── SaleService.java
│   ├── SaleServiceImpl.java
│   ├── SupplierService.java
│   └── SupplierServiceImpl.java
└── util
    └── DatabaseConnection.java
```

Test source files are maintained under `src/test/java`.

## Database

**Database name:** `smartstock`

Known database tables:

-   `users`
-   `suppliers`
-   `products`
-   `sales`
-   `sale_items`
-   `inventory_history`

Known `products` columns:

  Column            Type
  ----------------- ----------------------------------
  `product_id`      INT, primary key, auto-increment
  `product_name`    VARCHAR(100), not null
  `category`        VARCHAR(50), not null
  `price`           DECIMAL(10,2), not null
  `quantity`        INT, not null
  `reorder_level`   INT, not null
  `supplier_id`     INT, nullable

Known sales fields include `sale_id`, `sale_date`, and `total_amount`.
Known sale item fields include `sale_item_id`, `sale_id`, `product_id`,
`quantity`, and `unit_price`.

> Refer to the actual SQL schema before documenting additional columns,
> keys, or constraints in a final ER diagram.

## Important Implementation Details

### Sales Transaction

The sales implementation uses database transaction handling. It checks
available stock, records the sale and sale item, updates product
quantity, and commits the transaction when successful. If an operation
fails, the transaction is rolled back to avoid leaving a partially
completed sale. The implementation uses row locking with `FOR UPDATE`
while processing relevant stock.

### Object-Oriented Programming

The project demonstrates:

-   **Classes and objects:** Product, Supplier, Sale, SaleItem, and
    other application classes.
-   **Encapsulation:** Model objects hold data and expose methods to
    access or modify it.
-   **Inheritance:** `InventoryReport` and `SalesReport` extend the
    `Report` base class.
-   **Polymorphism:** A `Report` reference can refer to different
    concrete report objects.
-   **Abstraction and interfaces:** DAO and service interfaces define
    operations separately from their implementations.

### Exception Handling

Custom exceptions include `DatabaseException` and `ValidationException`.

### Background Processing

`SwingWorker` is used for longer-running operations such as AI requests
and report generation so that the Swing user interface remains
responsive.

## Requirements

-   Java Development Kit (JDK) 17
-   MySQL Server
-   IntelliJ IDEA or another Java IDE that supports Maven
-   Internet access for Groq AI requests
-   A valid Groq API key to use AI Insights

## Configuration

The AI API key is supplied through the environment variable:

``` text
GROQ_API_KEY
```

Do not commit API keys, database passwords, or other secrets to this
public repository.

The application connects to the MySQL database named `smartstock`.
Configure the local database connection in the project's database
connection configuration before running the application.

## Build and Run

1.  Install JDK 17 and MySQL.
2.  Create/configure the `smartstock` database using the project's
    actual database schema.
3.  Open the project folder containing `pom.xml` in IntelliJ IDEA.
4.  Configure the database connection.
5.  Set `GROQ_API_KEY` if AI Insights is to be used.
6.  Reload the Maven project.
7.  Run `clean`, `test`, and `package` from the Maven Lifecycle.
8.  Run `com.smartstock.Main` from the IDE.

The project completed Maven `clean`, `test`, and `package` successfully
during development.

## Testing

JUnit 5 tests are maintained under `src/test/java`, including:

-   `ProductServiceTest`
-   `ReportServiceTest`

The application was also manually tested across Dashboard, Products,
Inventory, Suppliers, Sales, Analytics, AI Insights, Reports, and
Settings during development.

## Limitations and Scope

-   The current application is a desktop application.
-   MySQL is required for persistent application data.
-   AI Insights requires internet connectivity and a valid Groq API key.
-   AI output is based on the information supplied to the AI service and
    may require human review.
-   Cloud deployment, mobile access, automated purchasing, and advanced
    forecasting are not part of the documented current implementation.

## Future Enhancements

Possible future improvements include role-based authentication, cloud or
multi-user deployment, barcode/QR-code scanning, purchase order
management, automated low-stock notifications, advanced demand
forecasting, additional report export formats, and more comprehensive
inventory movement tracking.

These are proposed enhancements, not claims about existing
functionality.

## Repository Contents

-   `src/` --- Java application source code and tests.
-   `pom.xml` --- Maven project configuration.
-   `.gitignore` --- Files excluded from version control.

## Security

Never upload Groq API keys, database passwords, personal credentials, or
local machine-specific configuration containing secrets. Use environment
variables or secure local configuration for sensitive values.

## Contributors

Update this section with the actual project contributors before
submission.

-   \[Team Leader Name\] --- \[Register Number\]
-   \[Team Member 2 Name\] --- \[Register Number\]
-   \[Team Member 3 Name\] --- \[Register Number\]
-   \[Team Member 4 Name\] --- \[Register Number\]

## Mentor

-   \[Mentor Name\]
-   \[Designation\]

## License

Specify the license required by your institution or project. If no
license has been selected, do not imply that the project has one.

------------------------------------------------------------------------

**SmartStock AI --- Inventory Management with AI-Assisted Business
Insights**
