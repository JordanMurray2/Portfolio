import {Hardware} from "./Hardware";
import {Interrupt} from "./imp/Interrupt";
import {ClockListener} from "./imp/ClockListener";
import {Cpu} from "./Cpu";
import sort from 'fast-sort';


export class InterruptController extends Hardware implements ClockListener{

    constructor(cpu: Cpu) {

        super(0, "IRC", false);
        this.irqHardware = [];
        this.irqRequests = [];

        this.cpu = cpu;
        //log the creation of this hardware
        this.log("Interrupt Controller created");

    }

    public isExecuting: boolean = false;

    // Contains all IRQ enabled hardware
    private irqHardware: Interrupt[];

    // Contains a buffer of IRQ hardware currently requesting a interrupt
    private irqRequests: Interrupt[];

    // The interrupt controller needs to know how to talk to the CPU, to send interrupts
    private cpu: Cpu;

    public init(): void {
        this.isExecuting = false;
    }

    /*
    Hardware that wishes to be assigned an IRQ number is added here.
     */
    public addIrq(irqHardware: Interrupt) {
        //assign the irq number based on the index in the irqHardware array assigned.
        irqHardware.irq = this.irqHardware.length;
        //add the hardware to the array so the IRQC knows that it can issue an interrupt
        this.irqHardware.push(irqHardware);
        //Print a confirmation that the hardware was added and what type of priority that hardware issues
        this.log(`IRQ: ${irqHardware.irq} Assigned to Name: ${irqHardware.name} Added - Priority ${irqHardware.priority}`);
    }

    /*
     * Accepts interrupt sent from hardware that uses the interface
     *
     * Param: interrupt
     */
    public acceptInterrupt(interrupt: Interrupt) {
        //add the interrupt sent from the hardware
        this.irqRequests.push(interrupt);
        //sort the array by priority
        let sortedInterrupts: Interrupt[] = this.irqRequests.sort(this.compare);
        //sent the interrupt to the CPU
        this.cpu.setInterrupt(sortedInterrupts[0]);
    }

    /*
     * pulse method used by clocklistener interface
     */
    pulse(): void {
        //log the response to the clock pulse
        this.log(`recieved Clock Pulse - Current Queue : ${this.irqRequests.length}`);
        //after the clock cycle, remove the interrupt that was just issued to the CPU
        this.irqRequests.pop();
    }

    /*
     * comparison method that is used by the sort to sort interrupts by their priority
     *
     * params: interrupt1, interrupt2
     *
     * return: number
     */
    public compare(interrupt1: Interrupt, interrupt2: Interrupt): number {
        //declare variables to compare
        let priority1: number = interrupt1.priority;
        let priority2: number = interrupt2.priority;
        let comparison: number = 0;

        //compare the priorities passed in
        if (priority1 > priority2) {
            comparison = 1;
        } else if (priority1 < priority2) {
            comparison = -1;
        }
        return comparison;
    }
}
