# **AeroDash – Aerodynamics Dashboard**

**Vanier College**  
**20-SF3-RE PROGRAM DEVELOPMENT IN A GRAPHICAL ENVIRONMENT**  
**Section 00001**

---

## **Aerodash – Project Proposal**

**Presented to:** Nagat Drawel  
**Students:** Andy Luo, Aminul Goldar  
**Date:** 2025-09-12  

---

# **2. Task Description**

## **Application Name:**  
### **AeroDash – The Aerodynamics Dashboard**

AeroDash is a JavaFX application that allows users to simulate and visualize basic aerodynamics concepts such as **lift** and **drag**. By adjusting variables like airspeed, wing angle of attack, and wing area, users can observe how these parameters affect the forces acting on an aircraft.

The application is designed as an **interactive educational tool**. It provides sliders and input fields for key flight parameters and displays real-time charts and visual indicators to help users understand how aerodynamics works.

### **Users Can:**
- Adjust airspeed, angle of attack, and wing area  
- View updated lift and drag values  
- See graphs (lift vs AoA, drag vs velocity)  
- Visualize the aircraft’s response with a simple animated model  

---

## **Scientific Theme / Domain**
- Simulation & Mathematical Modeling (lift/drag equations)  
- Data Visualization in Science/Technology  
- Educational Tool in Aerospace Engineering  

---

## **Physics Equations Used**

### **Lift Equation**
Lift = 0.5 × ρ × V² × S × Cl


### **Drag Equation**
Drag = 0.5 × ρ × V² × S × Cd


### **Where:**
- **ρ** = air density  
- **V** = velocity  
- **S** = wing area  
- **Cl** = coefficient of lift  
- **Cd** = coefficient of drag  

---

# **3. Interface Visualizations**

*(Mockups of the user interface: sliders, graphs, animated plane model, etc.)*

---

# **4. Proposed Implementation Approach**

## **Programming Language & Framework**
- Java 22+  
- JavaFX (for GUI, charts, animations)  

---

## **Project Structure Overview**

### **Controller file**  
Contains:
-MainPageController.java : Home Page with start button
-AeroDashContoller.java : takes in use inputed values, changes loudness of music
-PathViewController.java : showcasing of the rocket's trajectory


### **Image File**  
Contains images used for the project

### **Main File**  
Contains the Main Class

### **Sound File**  
Contains all music and sound used for the project

### **View File**  
Contains:
-MainView.fxml : Graphics for the Main Page
-Aerodash.fxml : Graphics for the Aero Dash page (user input page)
-pathView.fxml : Graphics for the Path View page
-style.css : styling of the pages

---

## **Libraries**
- JavaFX built-in charts (LineChart, NumberAxis)  
- JUnit 4.13.2  

---

# **README Information**

## **How to Run the Project**

### **Requirements**
- Java **22**  
- JavaFX SDK  
- Apache NetBeans (on any supporting IDE)  
- Git (optional)  

### **Steps**
1. Clone the repository:
   ```bash
   git clone https://github.com/AndyIsMyUsername/FINAL-PROJECT
2. Open in NetBean
3. Configure JavaFX (Libraries, VM Option, Scene Builder)
4. Run MainApp.java


## **Teamwork Summary**

### **Andy Luo**
- Created JavaFX project structure  
- JUnit testing  
- Added background music  
- Created PathView Controller (shared)  
- Added animations and interactivity (shared)  
- Polish & finalization (shared)  
- Added comments (shared)  

### **Aminul Goldar**
- Created AeroDash Controller  
- Modeled interface (buttons, graphs, plane graphic)  
- Added music slider in AeroDash Controller  
- Created PathView Controller (shared)  
- Added animations and interactivity (shared)  
- Polish & finalization (shared)  
- Added comments (shared)  

---

## **Links**

**Trello Board:**  
https://trello.com/invite/b/68bf6dfedce068bfc0d36646/ATTI69e98178e08edd50180ce6f4ebe241ca3D73D79A/finalproject  

**GitHub Repository:**  
https://github.com/AndyIsMyUsername/FINAL-PROJECT

