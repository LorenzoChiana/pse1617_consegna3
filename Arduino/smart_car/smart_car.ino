#include "config.h"

Scheduler sched;
Envirnoment *env;

void setup() {
  Serial.begin(9600);
  /*
  	Global:
  	Classe che contiene la variabili dei vari stati che comunicano con apposite funzioni di controllo
  	attente a non far confillgere gli stati e attenta a restuire solo certi valori all'utente
  */
  Envirnoment = new Envirnoment();
  Envirnoment->init();
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
  
  Task* distanceTask = new CleaningTask(Global);
  distanceTask->init(3*CLOCK);
  sched.addTask(distanceTask);
  

}

void loop() {
  sched.schedule();
}
