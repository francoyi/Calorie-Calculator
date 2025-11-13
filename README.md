# Calorie-Calculator

**Team Name:** csc207-We-are-cooking234  
**Team Members:** 
   - Yan Lam (personal github username & github link)
   - Shourya Harsh Vardhan (personal github username & github link)
   - Haotian Xu (personal github username & github link)
   - Rasyid Rafi Pamuji (personal github username & github link)
   - [@HaoyingZhu](https://github.com/HaoyingZhu)
   - [@ZhengyuYi](https://github.com/francoyi)


## 1. Project Description

Calorie-Calculator is an intelligent food diary application designed to help users easily and efficiently track and analyze their daily calorie intake. Users can search for standard foods via the OpenFoodFacts API, log custom foods, set daily calorie goals, and review their dietary history.

## 2. Core Features

*   **API-Powered Food Search:** Instantly search a massive online database (OpenFoodFacts) for nutritional information.
*   **Manual Food Entry:** Log custom or special foods that are not available in the API.
*   **Daily Dashboard:** View a real-time summary of today's food intake and total calories.
*   **Personalized Goals:** Set and track daily calorie goals to stay on target.
*   **Historical Log:** Browse and analyze dietary records from any past date.
*   **Frequently Used Meals:** Combine multiple food items into a reusable "meal" for quick, one-click logging.

## 3. Tech Stack

*   **Language:** Java 20
*   **UI Framework:** Java Swing
*   **Build & Dependency Management:** Apache Maven
*   **External API:** [OpenFoodFacts API](https://world.openfoodfacts.net/api/v2/product/{barcode})
*   **JSON Processing:** Jackson Databind

## 4. Getting Started & How to Run

### Prerequisites
*   Java Development Kit (JDK) 20 or higher.
*   Apache Maven.

### Running the Application
1. Clone the repository: `git clone https://github.com/francoyi/Calorie-Calculator.git`
2. Navigate to the project directory: `cd Calorie-Calculator`
3. Build the project using Maven: `mvn clean install`
4. Run the application: `mvn exec:java -Dexec.mainClass="com.caloriecalc.Main"`

## 5. Architecture & Design Decisions

This project is built following the principles of **Clean Architecture** and **SOLID** to ensure a decoupled, maintainable, and testable codebase.

*   **Layered Architecture:** Our package structure (`model`, `port`, `service`, `repo`, `ui`) strictly follows the Clean Architecture layers, with dependencies always pointing inwards towards the core business logic.
*   **Dependency Inversion:** We make extensive use of interfaces (in the `port` package) to invert dependencies. For example, our `service` layer depends on the `FoodLogRepository` interface, not on the concrete `JsonFoodLogRepository` implementation.
*   **Single Responsibility Principle:** We refactored our `FoodLogService` into multiple, single-purpose Interactors, each corresponding to a specific Use Case.
*   **Polymorphism:** The `FoodItem` interface, with its `APIFoodItem` and `LocalFoodItem` implementations, allows our system to handle different food sources seamlessly.
*   **Value Objects:** We used Java `record`s for immutable value objects like `NutritionValues` and `Serving` to enhance data integrity.
*   **API Client Design:** Our `OpenFoodFactsClient` features a time-based caching mechanism to improve performance and reduce API call frequency.
*   **Key Interactors:**
    - **`FoodLogService`** – The main use-case class that manages user meals, goals, and calorie totals.
       - Depends only on the *interfaces* defined in `com.caloriecalc.port`.
       - Handles:
           - Adding and removing meals
           - Calculating total daily calories
           - Managing and validating user goal settings
           - Synchronizing with data repositories
    - **`OpenFoodFactsClient`** – Implements an external data access boundary (`NutritionDataProvider`), serving as a *gateway* between the app and the OpenFoodFacts API.
       - Includes request caching, timeouts, and JSON deserialization logic.

*   **Key Boundaries:**
    - **`FoodLogRepository`** – Interface for reading/writing daily meal logs.
    - **`UserSettingsRepository`** – Interface for managing user calorie goals and settings.
    - **`NutritionDataProvider`** – Interface for fetching nutrition data from external databases (e.g., OpenFoodFacts).

    - Boundaries are implemented in the `com.caloriecalc.repo` and `com.caloriecalc.service` packages:
      - **`JsonFoodLogRepository`**, **`JsonUserSettingsRepository`** → implement persistence using local JSON files.
      - **`OpenFoodFactsClient`** → acts as a gateway to an external API, implementing the `NutritionDataProvider` boundary.


### 6. Flow Overview

```text
┌──────────────────┐       ┌──────────────────────────┐
│    UI Layer      │──────▶│  Interactors (Services)  │
│ (Swing Panels)   │       │ e.g. FoodLogService      │
└──────────────────┘       └────────────┬─────────────┘
                                        │
                                        ▼
                              ┌─────────────────────┐
                              │   Boundaries (Ports)│
                              │ Interfaces only     │
                              └────────┬────────────┘
                                       │
              ┌────────────────────────┴────────────────────────┐
              │                 Implementations                 │
              │ Json Repositories + OpenFoodFactsClient (APIs)  │
              └─────────────────────────────────────────────────┘
```

## 7. Use Case Assignments

| Use Case                                                    | Lead Member             |
| ----------------------------------------------------------- | ----------------------- |
| UC 1: Search Food via External API                          | **Zhengyu Yi**          |
| UC 2: Add API Food to Today's Record                        | **Zhengyu Yi**          |
| UC 3: Create Local Food (Manual Entry)                      | **Janice Lam**          |
| UC 6: Edit or Remove an Item from Today                     | **Zhengyu Yi**          |
| UC 7: Set Daily Calorie Goal                                | **Haoying Zhu**         |
| UC 8: View Past Day by Date                                 | **Haoying Zhu**         |
| UC 10: Receive food recommendations/references              | **Max Xu**              |
| UC 11: Calculate the user's basal metabolic rate (BMR)      | **Shourya Harsh Vardhan** |
| UC 12: Login to the application                             | **Rasyid Rafi Pamuji**  |
