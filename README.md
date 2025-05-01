# Retail Inventory App - Final Project

## Project Description
Huckster is a **Retail Inventory App** developed as the final project for our **Mobile Development** course. The app aims to help small businesses, such as sari-sari stores, manage their inventory efficiently with a user-friendly interface and essential features such as stock tracking, sales monitoring, and reporting.

Huckster is a business inventory management application designed specifically for small retail businesses. This platform enables business owners to efficiently manage, track, and optimize their product inventories. By providing an accessible interface and tools, Huckster empowers retailers to streamline operations, improve inventory accuracy, and make decisions based on data to improve business performance. Whether it's tracking product stock levels, adding or removing products, or staying on top of reordering needs, Huckster offers a comprehensive solution tailored to the everyday demands of small retail operations.

---

## Functional Requirements

### **1. Login**
- Secure user authentication to ensure that only authorized personnel can access and manage inventory data.
- Password recovery and reset functionality to enhance security and accessibility.

### **2. Product List**
- A detailed product list that can be organized into multiple categories, allowing for easy navigation and quick access to different items.
- Users can filter products based on categories, price, or product name.
- User-specific product visibility ensures that only the logged-in user's products are accessible.

### **3. Add Product**
- Users can easily add new products to their inventory with customizable fields, such as product name, category, price, and quantity.
- Allows users to upload product images for better visual identification.
- Tracks the associated user for each product, ensuring personalized inventory control.

### **4. Remove/Deduct Product**
- The application enables seamless removal or deduction of products when sales are made, returns occur, or stock is adjusted.
- Real-time updates to inventory ensure accurate stock levels across all user interactions.

### **5. Reordering Alerts**
- Huckster automatically tracks product quantities and sends alerts when stock levels are low.
- Customizable thresholds allow users to define when reordering alerts should be triggered.
- Notifications are displayed prominently in the app, ensuring immediate attention.

### **6. Product Trends**
- Huckster provides insightful data on product performance, helping users identify sales trends, popular items, and slow-moving products.
- Visual analytics such as charts and graphs are provided for better trend comprehension.
- Detailed metrics like sales history and quantity sold are available for each product.

### **7. User Profile Management**
- Users can update their personal details, such as name, email, and profile photo, directly within the app.
- Profile customization ensures a personalized experience for each user.

### **8. Inventory Value Calculation**
- Automatically calculates the total value of the inventory based on product prices and quantities.
- Real-time updates ensure that the inventory value reflects current stock levels.

### **9. Low Stock Notifications**
- Sends prompts to users when product stock levels fall below a predefined threshold.
- Notifications are tailored to each user's specific product list and threshold settings.

### **10. Multi-Device Support**
- Accessible across multiple devices to ensure convenience and flexibility for users on-the-go.
- Synchronization ensures that inventory data is always up-to-date, regardless of the device used.

### **11. Data Backup and Recovery**
- Regular automatic backups of inventory data ensure that critical information is not lost.
- Recovery options allow users to restore their inventory data in case of accidental deletion or app reinstallation.

---

## Labs Branch
The `labs` branch serves as a development environment where all code changes made during laboratory exercises are pushed before merging into the `main` branch. This ensures that all experimental features and updates are properly reviewed and tested before being deployed to the main codebase.

### Guidelines for Labs Branch:
1. **All new features and bug fixes must first be committed to the `labs` branch.**
2. Regular testing should be conducted before merging into `main`.
3. Code reviews are required before any pull request to the `main` branch.
4. Developers should document any major changes in the project to maintain clarity.

---

## Backup Branch - WARNING

## ⚠️ DO NOT TAMPER WITH THE BACKUP BRANCH ⚠️

### Important Guidelines:
1. **Do NOT modify, delete, or override files in the backup branch unless absolutely necessary.**
2. The backup branch should only be updated when everything in the `main` branch is confirmed to be working properly.
3. Before updating the backup, perform thorough testing to ensure no broken or unstable code is pushed.
4. If you need to update the backup, make sure you document the changes clearly.
5. Any unauthorized changes to this branch may cause data loss or rollback issues.

### How to Update the Backup:
1. Verify that the `main` branch is stable and has passed all necessary tests.
2. Merge the `main` branch into the backup branch following the correct versioning and documentation standards.
3. Double-check the integrity of the backup after merging.
4. Inform the team about the update to prevent conflicts.

**Failure to follow these guidelines may result in critical system failures. Please handle with caution.**

---

**For any concerns or required modifications, please contact the project maintainer before proceeding.**
