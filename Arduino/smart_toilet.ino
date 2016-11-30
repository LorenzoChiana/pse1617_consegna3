#include "config.h"

Scheduler sched;
GlobalState *Global;

void setup() {
  Serial.begin(9600);
  /*
  	Global:
  	Classe che contiene la variabili dei vari stati che comunicano con apposite funzioni di controllo
  	attente a non far confillgere gli stati e attenta a restuire solo certi valori all'utente
  */
  Global = new GlobalState();
  Global->init();
  /*
    Stabilita una gerarchia di priorità in base al numero (Lower is better)
    1: Allarmi (Ogni ciclo di clock - carico di lavoro pesante)
    2: Sensori di controllo e led annessi (ogni due cicli di clock - carico di lavoro medio)
    3: Task di controllo (ogni 3 cicli di clock - carico di lavoro basso)
  */
  sched.init(CLOCK);

  /*
    Cleaning Task:
    Task utilizzato per l'igenizzazione della toilette. Viene azionato quando 10 persone 
    entrano nel bagno e il task in questione conta le persone, avvia la pulizia e la ferma
    quando è terminata. 
  */
  
  Task* cleaningTask = new CleaningTask(Global);
  cleaningTask->init(3*CLOCK);
  sched.addTask(cleaningTask);
  
  /*
  	Led Cleaning Task_
  	Task che verifica che la pulizia sia in corso e pilota il led L3 che segnala che sta 
  	avvenendo la pulizia nel bagno. Questo task è attanto a non andare in conflitto con 
  	l'allarme visto che hanno lo stesso led
  */
  
  Task* ledCleaningTask = new LedCleaningTask(L3_PIN, Global);
  ledCleaningTask->init(3*CLOCK);
  sched.addTask(ledCleaningTask);
  
  /*
    Detect Motion:
    Questo task controlla la presenza o meno di una persona per pilotare la luce centrale
    L1 della toilette. La verifica viene effettuata con un PIR accompagnato dal sensore 
    a ultrasuoni.
  */
  
  Task* detectMotion = new DetectMotionTask(PIR_PIN,Global);
  detectMotion->init(2*CLOCK);
  sched.addTask(detectMotion);

  /*
    Fade Task:
    Questo task esegue un controllo sullo stato dello sciacquone e in base a quello fa pulsare 
    il led L2 in modo da simulare un eventuale sciaquone quando l'utente si allontana dal WC.
    Alla fine dell'operazione spegne lo sciaquone in modo da essere pronto per un'altro utente
  */

  Task* fadeTask = new FadeTask(L2_PIN, INTENSITY, Global);
  fadeTask->init(2*CLOCK);
  sched.addTask(fadeTask);

  /*
    Detect Presence Task:
    Questo task permette, grazie ad un sensore di prossimità ad ultrasuoni, di capire se un utente
    una volta dentro la toilette si è avvicinato abbastanza al wc in modo da permettere, una volta
    allontanto, di tirare lo sciaquone con l'opportuna comunicazione con gli altri task.
  */
  
  Task* detectPresenceTask = new DetectPresenceTask(ECHO_PIN, TRIG_PIN,Global);
  detectPresenceTask->init(2*CLOCK);
  sched.addTask(detectPresenceTask);

  
  /*
    Illuminate Task:
    E' un task che pilota L1 in modo da segnalare con un output luminoso se c'è una persona dentro
    la toilette e permette anche di segnalare, una volta finito il segnale del PIR, la presenza grazie
	al coontributo del sesnore di prossimità.
  */
  
  Task* illuminateTask = new IlluminateTask(L1_PIN, Global);
  illuminateTask->init(2*CLOCK);
  sched.addTask(illuminateTask);

  /*
  	Alarm Task:
	Questo task consente grazie a 3 input di pilotare il led di allarme. Il primo input è quello del
	sensore ultrasuoni che ti dice se una persona si è mossa no, in caso negativo per un tempo TMAX secondi
	allora scatta l'allarme assumendo che l'utente si sia sentito male all'interno.
	Il secondo input è quello del bottone, in base a quello e di conseguenza ad una pressione di piu di due 
	secondi viene azionato l'allarme che può essere disattivato tranquillamente con il bottone di stop.
  */
  
  Task* alarmTask = new AlarmTask(STILL_DISTANCE_THRESHOLD, Global);
  alarmTask->init(1*CLOCK);
  sched.addTask(alarmTask);  

  /*
  	Input Alarm Task:
  	Il task semplicemente controlla lo stato del bottone temporizzato. Controlla che venga eseguita una
  	pressione più lunga di due secondi prima di azionare il flag che indica di suonare l'allarme
  */
  Task* inputAlarmTask = new InputAlarmTask(T1_PIN,Global);
  inputAlarmTask->init(1*CLOCK);
  sched.addTask(inputAlarmTask);
  /*
  	Stop Alarm Task:
  	Questo task controlla il bottone di stop e lancia immediatamente il segnale di fermare l'allarme 

  */
  Task* stopAlarmTask = new StopAlarmTask(T2_PIN,Global);
  stopAlarmTask->init(1*CLOCK);
  sched.addTask(stopAlarmTask);

  /*
  	Alarm led task:
  	Questo task controlla il led in modo da non entrare in conflitto con il led di cleaning di far blinkare
  	in caso di allarme.
  */
  Task* alarmLedTask = new AlarmLedTask(L3_PIN,Global);
  alarmLedTask->init(1*CLOCK);
  sched.addTask(alarmLedTask);
  /*
  	Msg Task:
  	Questo task semplicemente esegue due operazioni principali: controllare che siano arrivati dei comandi, 
  	controllare che ci siano da mandare dei comandi al PC. I comandi sono due e sono quello degli utenti e 
  	quello dello stato del WC.
  	Dispone di un buffer logico di scrittura che contiene i messaggi di tutti i task (writingBuffer). In questo
  	modo gli altri task possono scrivere concorrentemente e quando tocca a questo task lo svuota scrivendo sul
  	seriale il resoconto dei messaggi.
  */
  Task* msgTask = new MsgTask(Global);
  msgTask->init(3*CLOCK);
  sched.addTask(msgTask);

}

void loop() {
  sched.schedule();
}
