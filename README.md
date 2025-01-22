# Automatic-Pet-Feeder 


This project is an automatic feeder designed to facilitate the feeding and care of pets such as dogs and cats. It combines hardware (Arduino, sensors, actuators and a bluetooth module) with an Android mobile application developed with Android Studio to automate and customize the feeding, as well as monitor in real time and provide statistics.

 ![1](https://github.com/inesperez03/Automatic-Pet-Feeder/blob/main/images/1.png?raw=true)

## 💾 Hardware
- Microcontrolador: Arduino Uno
- Modulo bluetooth: Bluetooth HC-05
- Sensores:
  - HC-SR04: Monitorización del nivel de agua.
  - Celda de carga (HX711): Peso de comida dispensada.
- Actuadores:
  - Bomba agua
  - Servomotor
 
  ![2](https://github.com/inesperez03/Automatic-Pet-Feeder/blob/main/images/2.png?raw=true)

## 💻 Software
- Arduino C++: Code to control sensors and actuators, and manage Bluetooth connectivity.
- Android mobile application: Developed in Java with Android Studio, to control and monitor the feeder.
- Firebase: Pet data storage and real-time statistics.

### Data transfer vía Bluetooth:
String message types:
- Sent by the android application:
  - “w”: add extra water
  - f": add extra food
  - “c, dailyfood, dailywater, autorefill”: indicates the daily target for each animal and if the autorefill option is enabled.
- Sent by arduino:
  - water_sensor: current amount of water
  - food_sensor: current amount of food 
  - dailyfoodcompleted: if the daily food goal has been reached
  - dailywatercompleted: if the daily water goal has been reached

### App Preview
![3](https://github.com/inesperez03/Automatic-Pet-Feeder/blob/main/images/3.png?raw=true)




## ✉️ Authors
 Inés Pérez Edo – github.com/inesperez03
 
 Joel Ramos Beltrán - github.com/JoelRamosBeltran
