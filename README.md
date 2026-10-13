# 💳 Digital Wallet Simulator with AI Financial Insights

A console-based **Digital Wallet Simulator** built using **Core Java** that enables users to securely manage digital wallet accounts, perform peer-to-peer transactions, and generate **AI-powered financial insights** using the **Google Gemini 2.5 Flash API**. The project demonstrates strong Object-Oriented Programming principles, Java Collections Framework, File Handling, Exception Handling, and REST API integration without using a database or external frameworks.

---

## 🚀 Features

- 👤 User Registration
- 🔐 Secure Login using Phone Number & PIN
- 💰 Add Money to Wallet
- 💸 Transfer Money Between Users
- 📊 Check Wallet Balance
- 📜 Transaction History
- 🤖 AI Financial Insights using Gemini 2.5 Flash API
- 🔄 Change Wallet PIN
- 💾 Persistent Data Storage using Text Files
- ⚠️ Custom Exception Handling
- ✅ Input Validation using Regular Expressions (Regex)

---

## ⭐ Project Highlights

- Built entirely using **Core Java** without databases or external frameworks.
- Designed using a modular architecture with clear separation of UI, business logic, validation, and AI integration.
- Implemented persistent storage using Java File I/O.
- Integrated **Google Gemini 2.5 Flash API** to generate AI-powered financial insights from wallet transactions.
- Demonstrates practical implementation of OOP, Collections Framework, Exception Handling, File Handling, and REST API Integration.

---

## 🤖 AI Financial Insights

The application integrates the **Google Gemini 2.5 Flash API** to analyze a user's wallet transaction history and generate intelligent financial recommendations.

The AI provides:

- 📈 Spending Pattern Analysis
- 💡 Saving Suggestions
- ⚠️ Unusual Transaction Detection
- 💰 Budget Recommendations
- 📊 Wallet Usage Analysis

The application communicates with Gemini using Java's built-in **HttpClient**, sends HTTP POST requests in JSON format, and processes JSON responses without using third-party libraries.

---

## 🛠️ Technologies Used

### Core Java

- Java 17
- Object-Oriented Programming (OOP)
- Collections Framework
- Exception Handling
- File Handling
- Java HttpClient
- LocalDateTime API
- Regular Expressions (Regex)

### AI Integration

- Google Gemini 2.5 Flash API
- REST API Integration
- HTTP Communication
- JSON Request & Response Processing

---

## 📂 Project Structure

```text
DigitalWalletSimulator/
│
├── app/
│   └── Main.java
│
├── model/
│   ├── User.java
│   └── Transaction.java
│
├── service/
│   ├── WalletService.java
│   └── AIService.java
│
├── util/
│   └── Validator.java
│
├── exception/
│   ├── InvalidPinException.java
│   └── InsufficientBalanceException.java
│
└── data/
    ├── users.txt
    └── transactions.txt
```

---

## 🏗️ Project Architecture

```text
                    Main (Console UI)
                           │
          ┌────────────────┴────────────────┐
          ▼                                 ▼
   WalletService                     AIService
          │                                 │
          ▼                                 ▼
 HashMap<String, User>             Gemini REST API
          │
          ▼
         User
          │
          ▼
 List<Transaction>
          │
          ▼
     Validator
          │
          ▼
 Custom Exceptions
```

---

## 🔄 AI Workflow

```text
User selects AI Financial Insights
              │
              ▼
Retrieve User Transaction History
              │
              ▼
Generate AI Prompt
              │
              ▼
Create JSON Request
              │
              ▼
Java HttpClient
              │
              ▼
Gemini 2.5 Flash API
              │
              ▼
Receive JSON Response
              │
              ▼
Extract AI Insights
              │
              ▼
Display Financial Insights
```

---

## 📌 OOP Concepts Used

- Classes & Objects
- Encapsulation
- Constructor Chaining
- Composition
- Packages
- Method Overloading
- Separation of Concerns

---

## 📚 Java Concepts Used

- Collections Framework
  - HashMap
  - ArrayList
- File Handling
  - BufferedReader
  - BufferedWriter
  - FileReader
  - FileWriter
- Exception Handling
- Custom Exceptions
- Java HttpClient
- REST API Integration
- HTTP Communication
- JSON Request & Response Processing
- LocalDateTime
- DateTimeFormatter
- Scanner
- Generics
- Regular Expressions (Regex)
- Try-with-Resources

---

## 💾 Data Persistence

The application stores all user and transaction data locally.

- **users.txt** → Registered user details
- **transactions.txt** → Complete wallet transaction history

Data is automatically loaded during application startup and saved back to files when the application exits.

---

## ▶️ How to Run

### 1. Clone Repository

```bash
git clone https://github.com/Hussain-390/Digital-Wallet-Simulator.git
```

### 2. Configure Gemini API Key

Open:

```java
AIService.java
```

Replace:

```java
private static final String GEMINI_API_KEY = "YOUR_API_KEY";
```

with your own Gemini API key.

### 3. Compile and Run

Run:

```text
Main.java
```

---

## 📷 Sample Workflow

```text
1. Register
2. Login
3. Add Money
4. Send Money
5. Check Balance
6. Transaction History
7. AI Financial Insights
8. Change PIN
9. Logout
10. Exit
```

---

## 📊 Sample AI Response

```text
========================================
AI Financial Insights
========================================

• Your wallet activity is primarily focused on peer-to-peer transfers.
• Deposits are generally followed by transfers to the same recipient.
• No unusual transaction behaviour was detected.
• Consider maintaining a small wallet balance before making transfers.
• Your wallet usage appears consistent with regular fund transfers.
• Analysis is based on limited wallet activity.
```

---

## 🎯 Learning Outcomes

This project strengthened my understanding of:

- Object-Oriented Design
- Modular Java Application Development
- Java Collections Framework
- Exception Handling
- File Persistence
- REST API Integration
- Java HttpClient
- HTTP Communication
- JSON Request & Response Processing
- AI Integration using Google Gemini 2.5 Flash API
- Prompt Engineering for AI-powered Financial Insights
- Clean Code Practices

---

## 📈 Future Enhancements

- JDBC + MySQL Integration
- PIN Hashing
- Transaction Search & Filters
- Transaction ID Generation
- Monthly Wallet Analytics
- PDF Transaction Statement
- JavaFX Desktop GUI
- Multi-Wallet Support
- Unit Testing using JUnit

---

## 👨‍💻 Author

**Mohammad Hussain Shaik**

