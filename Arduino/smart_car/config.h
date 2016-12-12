/*
 *  This header file stores symbols that concerns 
 *  the configuration of the system.
 *
 */
#ifndef __CONFIG__
#define __CONFIG__

#define T1_PIN 11
#define L1_PIN 10
#define L2_PIN 6
#define ECHO_PIN 7
#define TRIG_PIN 8

#define PIN_RX 3
#define PIN_TX 4

#define SERVO_PIN 9

#define DMIN 0.1
#define DMAX 0.1

#define DELAY_LED 250

#define CLOCK 50

#define STR_PARK "Spenta in parcheggio"
#define STR_MOVEMENT "Accesa in movimento"
#define STR_OFF "Spenta non in parcheggio"


typedef enum {MOVEMENT, PARK, OFF} State;

#include "Environment.h"

#endif

