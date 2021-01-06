// import statements for hardware
import {Cpu} from "./hardware/Cpu";
import {Memory} from "./hardware/Memory";
import Keyboard = require("./hardware/VirtualKeyboard");
import disk = require("./hardware/Disk");
import {Clock} from "./hardware/Clock";
import {Hardware} from "./hardware/Hardware";
import {InterruptController} from "./hardware/InterruptController";
import {VirtualKeyboard} from "./hardware/VirtualKeyboard";
import {Mmu} from "./hardware/Mmu";

// import statements for drivers

// import statements for kernel management components

// import statements for general kernel
const KERNEL: Kernel.Kernel = require("./kernel/Kernel");

/*
    Constants
 */
// Initialization Parameters for Hardware
// Clock cycle interval
const CLOCK_INTERVAL= 500;               // This is in ms (milliseconds) so 1000 = 1 second, 100 = 1/10 second
                                        // A setting of 100 is equivalent to 10hz, 1 would be 1,000hz or 1khz,
                                        // .001 would be 1,000,000 or 1mhz. Obviously you will want to keep this
                                        // small, I recommend a setting of 100, if you want to slow things down
                                        // make it larger.


export class System extends Hardware {

    private _CPU : Cpu = null;
    private _MEMORY: Memory = null;
    private _IRQ_CONTROLLER: InterruptController = null;
    private _CLOCK: Clock = null;
    private _KEYBOARD: VirtualKeyboard;
    private _MMU: Mmu;

    public running: boolean = false;

    constructor() {
        super(0, "SYS", true);
        console.log("Hello TSIRAM!");
        this.log("[****************** System Initialization started]");

        /*
        Initialize all the hardware to the system (analogous to you assembling the physical components together)
         */
        this.log("[****************** Hardware Initialization - Begin]");

        this._MEMORY = new Memory();
        this._MMU = new Mmu(this._MEMORY);
        this._CPU = new Cpu(this._MMU);
        this._IRQ_CONTROLLER = new InterruptController(this._CPU);
        // the clock gets passed all of the hardware listening for clock pulses
        this._CLOCK = new Clock(this._CPU, this._MEMORY, this._IRQ_CONTROLLER);

        // create IO Hardware
        this._KEYBOARD = new VirtualKeyboard(this._IRQ_CONTROLLER);

        // register the keyboard with the interrupt controller
        // create interrupts

        this.log("[****************** Hardware Initialization - Complete]");

        /*
        Start the system (Analogous to pressing the power button and having voltages flow through the components)
        When power is applied to the system clock (_CLOCK), it begins sending pulses to all clock observing hardware
        components so they can act on each clock cycle.
         */
        this.log("[****************** Starting System (applying power)]");
        this.startSystem();
        this.log("System running status: " + this.running);
        //start the clock for this system
        this._CLOCK.startClock(1000);
    }

    /*
     * Performs all tasks that need to be done when the system is started
     *
     * Returns: boolean
     */
    public startSystem(): boolean {
        //add all of the clock listening hardware to the array so that it can listen when a pulse is issued
        this._CLOCK.addHardware(this._CPU);
        this._CLOCK.addHardware(this._MEMORY);
        this._CLOCK.addHardware(this._IRQ_CONTROLLER);
        //add the hardware that can issue an interrupt to the CPU
        this._IRQ_CONTROLLER.addIrq(this._KEYBOARD);
        //initialize memory
        this._MEMORY.initializeMemory();

        //My created Program: Find Target number in a list of numbers and print out the memory location it was found
        //LDA xReg with target number
        this._MMU.writeImmediate(0x0000, 0xA2);
        this._MMU.writeImmediate(0x0001, 0x02);
        //Increment byte in memory to access/compare when branching
        this._MMU.writeImmediate(0x0002, 0xEE);
        this._MMU.writeImmediate(0x0003, 0x0B);
        this._MMU.writeImmediate(0x0004, 0x00);
        //Increment the value of byte to LDA the yReg
        this._MMU.writeImmediate(0x0005, 0x0EE);
        this._MMU.writeImmediate(0x0006, 0x09);
        this._MMU.writeImmediate(0x0007, 0x00);
        //LDA the yReg with the start of the list to search for target
        this._MMU.writeImmediate(0x0008, 0xA0);
        this._MMU.writeImmediate(0x0009, 0x4F);
        //Compare byte in memory to the xReg
        this._MMU.writeImmediate(0x000A, 0xEC);
        this._MMU.writeImmediate(0x000B, 0x4F);
        this._MMU.writeImmediate(0x000C, 0x00);
        //Branch back to 0x0002 until the match is found
        this._MMU.writeImmediate(0x000D, 0xD0);
        this._MMU.writeImmediate(0x000E, 0xF4);
        //LDA constant 1 into the xReg to make system call
        this._MMU.writeImmediate(0x000F, 0xA2);
        this._MMU.writeImmediate(0x0010, 0x01);
        //Make system call to print the value in yReg (memory address where target was found)
        this._MMU.writeImmediate(0x0011, 0xFF);
        //End the program
        this._MMU.writeImmediate(0x0012, 0x00);
        //Declare Global Variables -- list of numbers to search
        this._MMU.writeImmediate(0x0050, 0xF4);
        this._MMU.writeImmediate(0x0051, 0xA1);
        this._MMU.writeImmediate(0x0052, 0x02); //This is the target in the list
        this._MMU.writeImmediate(0x0053, 0x07);
        this._MMU.writeImmediate(0x0054, 0x9A);
        this._MMU.writeImmediate(0x0055, 0xB6);
        this._MMU.writeImmediate(0x0056, 0x6C);
        this._MMU.writeImmediate(0x0057, 0xE2);
        this._MMU.writeImmediate(0x0058, 0x22);
        this._MMU.writeImmediate(0x0059, 0xD4);

        //print the memory dump of all values in the program
        this._MMU.memoryDump(0x0000, 0x012);
        this.log("---------------------------");
        this._MMU.memoryDump(0x0050, 0x59);

        return false;
    }

    /*
     * stops the system
     *
     * returns: boolean
     */
    public stopSystem(): boolean {

        return false;

    }
}

let system: System = new System();
