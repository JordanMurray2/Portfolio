/* ------------
     VirtualKeyboard.ts
        TODO: BG
     ...

     I don't know if i need this class yet.

     I think it will depend on how events are handled.  Really the keyboards buffer would receive a character and raise
     the keyboard interrupt.  Then the CPU would wake up at the end of the cycle, push the current application to the
     stack and load up the Keyboard driver.

     Maybe another observer pattern on a Interrupt handler class.  All hardware assigned an interrupt will be assigned
     an IRQ number and a ISR which points to the driver.  Then when the Interrupt handler class receives
     ------------ */

import {Hardware} from "./Hardware";
import {Interrupt} from "./imp/Interrupt";
import {InterruptController} from "./InterruptController";
import {QueueMurray} from "./QueueMurray";

export class VirtualKeyboard extends Hardware implements Interrupt {

    constructor(interruptController: InterruptController) {
        super(0, "VKB", false);

        this.isExecuting = false;

        this.irq = -1;                                       // IRQ num is assigned by the controller
        this.priority = Interrupt.Priority.REGULAR;

        this.inputBuffer = null;
        this.outputBuffer = new QueueMurray;

        this.interruptController = interruptController;

        this.monitorKeys();
        //log the creation of this hardware
        this.log("Keyboard created");

    }

    public isExecuting : boolean;

    // reference to the interrupt controller
    private interruptController: InterruptController;

    irq: number;
    priority: Interrupt.Priority;
    name: String;

    inputBuffer: QueueMurray;
    outputBuffer: QueueMurray;

    private monitorKeys() {
        /*
        character stream from stdin code (most of the contents of this function) taken from here
        https://stackoverflow.com/questions/5006821/nodejs-how-to-read-keystrokes-from-stdin

        This takes care of the simulation we need to do to capture stdin from the console and retrieve the character.
        Then we can put it in the buffer and trigger the interrupt.
         */
        var stdin = process.stdin;

        // without this, we would only get streams once enter is pressed
        stdin.setRawMode( true );

        // resume stdin in the parent process (node app won't quit all by itself
        // unless an error or process.exit() happens)
        stdin.resume();

        // i don't want binary, do you?
        stdin.setEncoding( 'utf8' );

        // on any data into stdin
        stdin.on( 'data', function( key ){
            //print out the key the user pressed
            this.log(`Key Pressed - ${key}`);
            // ctrl-c ( end of text )
            // this let's us break out with ctrl-c
            if ( key.toString() === '\u0003' ) {
                process.exit();
            }
            //store the key pressed in the output buffer to be retrieved by the CPU
            this.outputBuffer.enqueue(key);
            //pass the interrupt to the IRQ
            this.interruptController.acceptInterrupt(this);
            // .bind(this) is required when running an asynchronous process in node that wishes to reference an
            // instance of an object.
        }.bind(this));
    }
}
