# Grama-Khata 📒

## Digital Micro-Finance Ledger for Village Shopkeepers

Grama-Khata is an Android-based digital ledger application developed for village grocery stores and small retailers to manage customer credit transactions digitally. The application provides real-time balance tracking, customer management, offline-first storage, and WhatsApp/SMS payment reminders through a simple and user-friendly interface.

---

# 📌 Problem Statement

Many small grocery stores in villages still maintain customer credit records in physical notebooks (“Vahis”). These manual systems are difficult to manage and often lead to:

- Financial loss
- Confusion between customers
- Damaged or missing records
- Difficulty tracking pending dues
- Social friction in the community

Shopkeepers need a simple and reliable digital solution without using complicated accounting software.

Grama-Khata solves this problem by digitizing the traditional credit system using a lightweight Android application specifically designed for small retailers and rural shopkeepers.

---

# 💡 Project Vision

The goal of Grama-Khata is to modernize the traditional trust-based credit system used in villages while keeping the application simple and easy to use.

Instead of complex accounting dashboards, the application provides a simple:

- Give (Credit) ➕
- Take (Payment) ➖

workflow for maintaining customer dues.

The application focuses on:

- Simplicity
- Offline accessibility
- Fast transaction management
- Easy due tracking
- Digital reminders through WhatsApp/SMS

---

# 🚀 Features

## 👤 Customer Profile Management

- Add customer details
- Store customer photos
- Avoid confusion between customers with similar names

---

## 💰 Transaction Management

- Add credit transactions
- Record customer payments
- Automatically calculate net balance
- Maintain transaction history

---

## 📊 Due Dashboard

- Displays pending customer balances
- Customers sorted by highest due amount
- Quick identification of unpaid customers

---

## 📲 WhatsApp / SMS Reminder

- One-tap reminder functionality
- Pre-filled reminder messages
- Uses Android Intent API for message sharing

### Example Reminder Message

```text
Namaskara, your due at [Shop Name] is ₹[Amount].
```

---

## 📶 Offline-First Application

- Uses Room Database for local data storage
- Works without internet connectivity
- Ensures reliable data persistence

---

# 🛠️ Tech Stack

| Technology | Purpose |
|------------|----------|
| Kotlin | Android Application Development |
| Android Studio | Development Environment |
| Room Database | Offline Local Storage |
| MVVM Architecture | Clean Architecture Pattern |
| LiveData | Real-Time UI Updates |
| RecyclerView | Dynamic List Display |
| XML | User Interface Design |
| Intent API | WhatsApp/SMS Integration |
| Git & GitHub | Version Control |

---

# 📂 Project Structure

```text
Grama-Khata/
│
├── app/
│   ├── activities/
│   ├── adapters/
│   ├── database/
│   ├── models/
│   ├── repository/
│   ├── viewmodel/
│   └── utils/
│
├── screenshots/
│
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

# 🔄 Application Workflow

## 1️⃣ Add Customer

- Enter customer details
- Add profile image

---

## 2️⃣ Record Transactions

- Press ➕ for credit transactions
- Press ➖ for payment collection

---

## 3️⃣ View Due Dashboard

- Displays all pending balances
- Customers sorted by due amount

---

## 4️⃣ Send Reminder

- Tap WhatsApp/SMS icon
- Reminder message opens automatically

---

# 🧠 Core Functionalities

## Real-Time Balance Calculation

The application instantly updates customer balances after every transaction using MVVM Architecture and LiveData observers.

---

## Offline Data Management

Room Database ensures:

- Fast local storage
- Reliable offline functionality
- Data persistence
- Improved application performance

---

## WhatsApp/SMS Integration

The application uses:

```kotlin
Intent.ACTION_SEND
```

to launch WhatsApp or SMS applications with pre-filled reminder messages.

---

# 🎯 Impact Goals

## 🌐 Financial Digitization

Digitizing the unorganized rural credit management system.

---

## 🏪 Support for Small Retailers

Helping shopkeepers reduce financial confusion and improve due tracking efficiency.

---

## 🤝 Trust-Based Technology

Using technology to strengthen community trust and simplify traditional credit systems.

---

# ✅ Success Criteria

The project is considered successful if:

- Net balance updates instantly after transactions
- Customer records are stored securely
- Due reminders work successfully
- The application works offline
- The UI remains simple and easy to use
- Daily transaction tracking becomes easier for shopkeepers

---

# ⚙️ Installation & Setup

## Clone Repository

```bash
git clone https://github.com/varshashankar0704-blip/GramaKhata.git
```

---

## Open Project

1. Open Android Studio
2. Select **Open Existing Project**
3. Choose the cloned repository folder

---

## Run the Application

1. Connect Android device or start emulator
2. Click ▶ Run in Android Studio

---

# 📦 Dependencies

Main Android dependencies used in the project:

- Room Database
- LiveData
- ViewModel
- RecyclerView
- Material Design Components

---

# 📜 License

This project is developed for educational and academic purposes.

---

# 🙌 Contributors

- Akshan B
- Varsha Shankar

---

# 📬 Contact

For suggestions or collaboration:

- GitHub: varshashankar0704-blip
