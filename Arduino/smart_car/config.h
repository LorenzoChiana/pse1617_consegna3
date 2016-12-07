/*
 *  This header file stores symbols that concerns 
 *  the configuration of the system.
 *
 */
#ifndef __CONFIG__
#define __CONFIG__

#define T1_PIN 10
#define L1_PIN 4
#define L2_PIN 5
#define ECHO_PIN 7
#define TRIG_PIN 8

#define SERVO_PIN 9

#define PIN_TX 3
#define PIN_RX 4

#define SERVO_PIN 9

#define DMIN 0.2
#define DMAX 0.5

#define DELAY_LED 250

#define CLOCK 30


typedef enum {MOVEMENT, PARK, OFF} State;

#include "Environment.h"

#endif

