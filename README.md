# Shortart Assessment - Content Sharing Android App

This is a simple content-sharing Android application built using **Firebase Authentication, Firebase Realtime Database, Firebase Dynamic Links, and Deep Links**. The app allows users to register, log in, upload images, and share them via **WhatsApp or a copyable link**.

---

## 🚀 Features

✅ **User Authentication** - Sign up and log in using Firebase Authentication.  
✅ **User Profile Management** - Store user profile details (name, profile picture, user ID) in Firebase Database.  
✅ **Image Upload & Display** - Post images and view them in a RecyclerView.  
✅ **Content Sharing** - Share images via **Firebase Dynamic Links** & **Deep Links** to other users or external platforms.  
✅ **Deep Link Handling** - Open shared images directly within the app.  
✅ **Logout Functionality** - Secure user session management with Firebase Authentication.

---

## 🛠️ Technologies Used

- **Kotlin** (Programming Language)
- **Android Studio**
- **Firebase Authentication**
- **Firebase Realtime Database**
- **Firebase Storage**
- **Firebase Dynamic Links**
- **Glide** (For Image Loading)
- **RecyclerView** (For Displaying Posts)

---

## 📸 Screenshots

| Login Screen | Sign Up Screen |
|-------------|--------------|
| ![s1](https://github.com/user-attachments/assets/206a610d-1dc2-41c1-9943-540856ebf5bd) | ![image](https://github.com/user-attachments/assets/3935743d-64e4-46d4-9b56-50d18a8cbedf) |

| Home Screen | Upload Screen |
|-------------|--------------|
| ![image](https://github.com/user-attachments/assets/ef3be351-08ca-4c89-b71f-069e5517e0e0)| ![image](https://github.com/user-attachments/assets/0b602d99-3bf2-4633-95c9-f581329a7c1d)
 |

---

## 📋 Setup Instructions

### 2️⃣ Open in Android Studio
- Open Android Studio.
- Click on "Open an existing project" and select the cloned folder.

### 3️⃣ Set Up Firebase
- Go to Firebase Console.
- Create a new project.
- Enable the following Firebase services:
  - Firebase Authentication (Email/Password)
  - Realtime Database
  - Firebase Storage
  - Firebase Dynamic Links
- Download `google-services.json` and place it inside the `/app` directory.

### 4️⃣ Run the App
- Click **Run ▶** in Android Studio to test the app.

---

## 🔗 How It Works

### User Registration & Login
- Users sign up with email & password.
- Profile details are stored in Firebase Database.

### Uploading Images
- Users select an image from the gallery.
- The image is uploaded to Firebase Storage.
- A new post is created in Realtime Database.

### Sharing Images
- A Firebase Dynamic Link is generated for each post.
- Users can share via WhatsApp , Email etc or copy the link.
- When clicked, the app opens and displays the shared image.

### Deep Link Handling
- The app detects Firebase Dynamic Links.
- Extracts image ID and opens it inside `ImageDetailActivity`.

---

## 🔥 Future Enhancements
✅ Push Notifications for shared content.  
✅ Implement Like & Comment features.  
✅ User Profile Editing.

---

## 💡 Contributors
**Shivam Verma** - GitHub



