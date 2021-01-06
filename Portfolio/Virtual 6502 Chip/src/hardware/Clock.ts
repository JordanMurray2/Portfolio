import {Hardware} from "./Hardware";
import {ClockListener} from "./imp/ClockListener";
import {Cpu} from "./Cpu";
import {Memory} from "./Memory";
import {InterruptController} from "./InterruptController";


export class Clock extends Hardware {

    public runningClockId: NodeJS.Timeout;

    constructor(cpu : Cpu, memory: Memory, interruptController: InterruptController) {

        super(0, "CLK", true);
        this.clockListeningHardware = [];
        //used for break interrupt for now
        this.cpu = cpu;
        //log the creation of this hardware
        this.log("Clock created");
    }

    public clockCount: number = 0;
    public isExecuting: boolean = false;
    private clockListeningHardware: ClockListener[];
    private cpu: Cpu;


    /*
     * add all the hardware that will listen to the clock
     *
     * param clocklistener
     */
    public addHardware(clockListener: ClockListener) {
        this.clockListeningHardware.push(clockListener);
    }

    /*
     * send the pulse to all of the clock listening hardware
     */
    public sendPulse() {
        //log the initialization of a new clock pulse
        this.log("Clock Pulse Initialized");
        //map the clock listening hardware to the clocklistener interface to send the pulse
        this.clockListeningHardware.map((ClockListener, number, clockListeningHardware) => {ClockListener.pulse()});
        //stop the clock when the instruction register has the op code break (00)
        if(this.cpu.ir == 0x00){
            this.stopClock();
        }
    }

    /*
     * start the clock and begin to send pulses
     *
     * param: delay
     *
     * return: boolean
     */
    public startClock(delay:number) : boolean {
        let started: boolean = false;
        if (this.runningClockId == null) {
            // .bind(this) is required when running an asynchronous process in node that wishes to reference an
            // instance of an object.
            this.runningClockId = setInterval(this.sendPulse.bind(this), delay)
            started = true;
        }
        else {
            //this.log("Clock startup failed, clock already running!");
            started = false;
        }
            return started;
    }

    /*
     * stop the clock which stops sending pulses
     *
     * return: boolean
     */
    public stopClock() : boolean {
        clearInterval(this.runningClockId);
        return true;
    }

}
